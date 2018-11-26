package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.LocalizacaoDTO;
import br.com.academiadev.suicidesquad.dto.PetCreateDTO;
import br.com.academiadev.suicidesquad.entity.Localizacao;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.Categoria;
import br.com.academiadev.suicidesquad.enums.ComprimentoPelo;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Tipo;
import br.com.academiadev.suicidesquad.repository.LocalizacaoRepository;
import br.com.academiadev.suicidesquad.service.PetService;
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
public class PetMapperTest {
    @Autowired
    private PetMapper petMapper;

    @Autowired
    private PetService petService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    private Usuario buildUsuario() {
        return Usuario.builder()
                .nome("Exemplo")
                .email("exemplo@example.com")
                .senha("hunter2")
                .build();
    }

    @Test
    public void editarPet_quandoNovaLocalizacao_entaoCriarNovaLocalizacao() {
        final Usuario usuario = usuarioService.save(buildUsuario());

        Localizacao localizacaoA = localizacaoRepository.save(new Localizacao(
                "Centro",
                "São Francisco do Sul",
                "Santa Catarina"));
        Pet petA = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .usuario(usuario)
                .localizacao(localizacaoA)
                .build());
        Pet petB = petService.save(Pet.builder()
                .tipo(Tipo.CACHORRO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.CURTO)
                .categoria(Categoria.ACHADO)
                .usuario(usuario)
                .build());

        PetCreateDTO petB_EditDTO = new PetCreateDTO();
        petB_EditDTO.setTipo(petB.getTipo().toString());
        petB_EditDTO.setPorte(petB.getPorte().toString());
        petB_EditDTO.setComprimento_pelo(petB.getComprimentoPelo().toString());
        petB_EditDTO.setCategoria(petB.getCategoria().toString());

        LocalizacaoDTO localizacaoB_DTO = new LocalizacaoDTO();
        localizacaoB_DTO.setBairro("Floresta");
        localizacaoB_DTO.setCidade("Joinville");
        localizacaoB_DTO.setUf("Santa Catarina");
        petB_EditDTO.setLocalizacao(localizacaoB_DTO);

        petMapper.updateEntity(petB_EditDTO, petB);

        assertThat(petB.getTipo(), equalTo(Tipo.CACHORRO));
        assertThat(petB.getLocalizacao().getId(), not(localizacaoA.getId()));
        assertThat(localizacaoRepository.findAll(), hasSize(2));
    }

    @Test
    public void editarPet_quandoLocalizacaoExistente_entaoUsaLocalizacaoExistente() {
        final Usuario usuario = usuarioService.save(buildUsuario());

        Localizacao localizacaoA = localizacaoRepository.save(new Localizacao(
                "Centro",
                "São Francisco do Sul",
                "Santa Catarina"));
        Pet petA = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .usuario(usuario)
                .localizacao(localizacaoA)
                .build());
        Pet petB = petService.save(Pet.builder()
                .tipo(Tipo.CACHORRO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.CURTO)
                .categoria(Categoria.ACHADO)
                .usuario(usuario)
                .build());

        PetCreateDTO petB_EditDTO = new PetCreateDTO();
        petB_EditDTO.setTipo(petB.getTipo().toString());
        petB_EditDTO.setPorte(petB.getPorte().toString());
        petB_EditDTO.setComprimento_pelo(petB.getComprimentoPelo().toString());
        petB_EditDTO.setCategoria(petB.getCategoria().toString());

        LocalizacaoDTO localizacaoA_DTO = new LocalizacaoDTO();
        localizacaoA_DTO.setBairro(localizacaoA.getBairro());
        localizacaoA_DTO.setCidade(localizacaoA.getCidade());
        localizacaoA_DTO.setUf(localizacaoA.getUf());
        petB_EditDTO.setLocalizacao(localizacaoA_DTO);

        petMapper.updateEntity(petB_EditDTO, petB);

        assertThat(petB.getTipo(), equalTo(Tipo.CACHORRO));
        assertThat(petB.getLocalizacao().getId(), equalTo(localizacaoA.getId()));
        assertThat(localizacaoRepository.findAll(), hasSize(1));
    }
}