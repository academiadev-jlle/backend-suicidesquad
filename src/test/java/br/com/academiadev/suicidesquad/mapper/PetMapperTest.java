package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.LocalizacaoDTO;
import br.com.academiadev.suicidesquad.dto.PetCreateDTO;
import br.com.academiadev.suicidesquad.dto.PetDTO;
import br.com.academiadev.suicidesquad.dto.PetDetailDTO;
import br.com.academiadev.suicidesquad.entity.Localizacao;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.*;
import br.com.academiadev.suicidesquad.repository.LocalizacaoRepository;
import br.com.academiadev.suicidesquad.service.PetService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    private Pet buildPet(Usuario usuario) {
        return Pet.builder()
                .usuario(usuario)
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .raca(Raca.GATO_SRD)
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

    @Test
    public void dadoPet_quandoToDTO_entaoDeuCerto() {
        Usuario usuario = buildUsuario();
        Pet pet = buildPet(usuario);

        PetDTO petDTO = petMapper.toDto(pet);

        assertThat(petDTO.getId(), equalTo(pet.getId()));
        assertThat(petDTO.getPorte(), equalTo(pet.getPorte().toString()));
        assertThat(petDTO.getCategoria(), equalTo(pet.getCategoria().toString()));
    }

    @Test
    public void dadoPet_quandoToDetailDTO_entaoDeuCerto(){
        Usuario usuario = buildUsuario();
        Pet pet = buildPet(usuario);

        PetDetailDTO petDetailDTO = petMapper.toDetailDto(pet);

        assertThat(petDetailDTO.getUsuario().getId(), equalTo(usuario.getId()));
        assertThat(petDetailDTO.getRegistros().size(), equalTo(pet.getRegistros().size()));
        assertThat(petDetailDTO.getEmail(), equalTo(usuario.getEmail()));
        assertThat(petDetailDTO.getN_visitas(), equalTo(pet.getNumeroDeVisitas()));
        assertThat(petDetailDTO.getDescricao(), equalTo(pet.getDescricao()));
    }

    @Test
    public void dadoPetCreateDTO_quandoToEntity_entaoDeuCerto(){
        Localizacao localizacaoA = localizacaoRepository.save(new Localizacao(
                "Centro",
                "São Francisco do Sul",
                "Santa Catarina"));

        List<String> cores = new ArrayList<>();

        Usuario usuario = buildUsuario();
        Pet pet = buildPet(usuario);
        pet.setLocalizacao(localizacaoA);
        PetCreateDTO petCreateDTO = new PetCreateDTO();

        petCreateDTO.setCastracao(pet.getCastracao().toString());
        petCreateDTO.setNome(pet.getNome());
        petCreateDTO.setCategoria(pet.getCategoria().toString());
        petCreateDTO.setTipo(pet.getTipo().toString());
        petCreateDTO.setPorte(pet.getPorte().toString());
        petCreateDTO.setComprimento_pelo(pet.getComprimentoPelo().toString());
        petCreateDTO.setRaca(pet.getRaca().toString());
        petCreateDTO.setCores(cores);

        LocalizacaoDTO localizacaoA_DTO = new LocalizacaoDTO();
        localizacaoA_DTO.setBairro(localizacaoA.getBairro());
        localizacaoA_DTO.setCidade(localizacaoA.getCidade());
        localizacaoA_DTO.setUf(localizacaoA.getUf());

        petCreateDTO.setLocalizacao(localizacaoA_DTO);

        Pet petRecebido = petMapper.toEntity(petCreateDTO);
        petRecebido.setUsuario(usuario);

        assertThat(petRecebido, equalTo(pet));
    }
}