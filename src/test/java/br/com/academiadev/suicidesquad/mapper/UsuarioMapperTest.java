package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.LocalizacaoDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioEditDTO;
import br.com.academiadev.suicidesquad.entity.Localizacao;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.repository.LocalizacaoRepository;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UsuarioMapperTest {
    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    private Localizacao buildLocalizacao() {
        return new Localizacao(
                "Centro",
                "São Francisco do Sul",
                "Santa Catarina");
    }

    private Usuario buildUsuario(String desc, Localizacao localizacao) {
        return Usuario.builder()
                .nome(String.format("Usuário %s", desc))
                .email(String.format("usuario.%s@example.com", desc))
                .localizacao(localizacao)
                .build();
    }

    @Test
    public void editarUsuario_quandoNovaLocalizacao_entaoCriarNovaLocalizacao() {
        Localizacao localizacaoA  = localizacaoRepository.save(buildLocalizacao());
        Usuario usuarioA = usuarioService.save(buildUsuario("A", localizacaoA));
        Usuario usuarioB = usuarioService.save(buildUsuario("B", null));

        UsuarioEditDTO usuarioB_EditDTO = new UsuarioEditDTO();
        usuarioB_EditDTO.setNome(usuarioB.getNome());

        LocalizacaoDTO localizacaoB_DTO = new LocalizacaoDTO();
        localizacaoB_DTO.setBairro("Floresta");
        localizacaoB_DTO.setCidade("Joinville");
        localizacaoB_DTO.setUf("Santa Catarina");
        usuarioB_EditDTO.setLocalizacao(localizacaoB_DTO);

        usuarioMapper.updateEntity(usuarioB_EditDTO, usuarioB);

        assertThat(usuarioB.getNome(), equalTo("Usuário B"));
        assertThat(usuarioB.getLocalizacao().getId(), not(localizacaoA.getId()));
        assertThat(localizacaoRepository.findAll(), hasSize(2));
    }

    @Test
    public void editarUsuario_quandoLocalizacaoExistente_entaoUsaLocalizacaoExistente() {
        Localizacao localizacaoA  = localizacaoRepository.save(buildLocalizacao());
        Usuario usuarioA = usuarioService.save(buildUsuario("A", localizacaoA));
        Usuario usuarioB = usuarioService.save(buildUsuario("B", null));

        UsuarioEditDTO usuarioB_EditDTO = new UsuarioEditDTO();
        usuarioB_EditDTO.setNome(usuarioB.getNome());

        LocalizacaoDTO localizacaoA_DTO = new LocalizacaoDTO();
        localizacaoA_DTO.setBairro(localizacaoA.getBairro());
        localizacaoA_DTO.setCidade(localizacaoA.getCidade());
        localizacaoA_DTO.setUf(localizacaoA.getUf());
        usuarioB_EditDTO.setLocalizacao(localizacaoA_DTO);

        usuarioMapper.updateEntity(usuarioB_EditDTO, usuarioB);

        assertThat(usuarioB.getNome(), equalTo("Usuário B"));
        assertThat(usuarioB.getLocalizacao().getId(), equalTo(localizacaoA.getId()));
        assertThat(localizacaoRepository.findAll(), hasSize(1));
    }
}