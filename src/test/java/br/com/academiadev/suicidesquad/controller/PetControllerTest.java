package br.com.academiadev.suicidesquad.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Registro;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.Categoria;
import br.com.academiadev.suicidesquad.enums.ComprimentoPelo;
import br.com.academiadev.suicidesquad.enums.Cor;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Raca;
import br.com.academiadev.suicidesquad.enums.Situacao;
import br.com.academiadev.suicidesquad.enums.Tipo;
import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
import br.com.academiadev.suicidesquad.service.PetService;
import br.com.academiadev.suicidesquad.service.UsuarioService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PetControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PetService petService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Pet buildPet() {
        Set<Cor> cores = new HashSet<>();
        cores.add(Cor.PRETO);
        cores.add(Cor.BRANCO);
        
        List<String> fotos = new ArrayList<>();
        fotos.add("link1");
        fotos.add("link2");
        return Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .cores(cores)
                .fotos(fotos)
                .build();
    }

    private Usuario buildUsuario() {
    	List<String> fotos = new ArrayList<>();
    	fotos.add("link1");
    	fotos.add("link2");
    	return Usuario.builder()
                .nome("Exemplo")
                .email("example@example.com")
                .senha("hunter2")
                .fotos(fotos)
                .build();
    }

    @Test
    public void criarPet_quandoValido_entaoSucesso() throws Exception {
        Usuario usuario = usuarioService.save(Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .senha("hunter2")
                .build());

        Map<String, String> petJson = new HashMap<>();
        petJson.put("tipo", "GATO");
        petJson.put("porte", "MEDIO");
        petJson.put("comprimento_pelo", "MEDIO");
        petJson.put("categoria", "ACHADO");

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(post("/pets")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(petJson))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Pet petCriado = petService.findAll().get(0);
        assertThat(petCriado.getTipo().toString(), equalTo(petJson.get("tipo")));
        assertThat(petCriado.getUsuario().getUsername(), equalTo(usuario.getUsername()));
    }

    @Test
    @WithMockUser
    public void criarPet_quandoInvalido_entaoErro() throws Exception {
        Map<String, String> petJson = new HashMap<>();
        petJson.put("tipo", "GATO");

        mvc.perform(post("/pets")
                .content(objectMapper.writeValueAsString(petJson))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void obterPet_quantoExiste_entaoRetorna() throws Exception {
        Pet pet = petService.save(buildPet());

        mvc.perform(get(String.format("/pets/%d", pet.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", equalTo(pet.getTipo().toString())))
                .andExpect(jsonPath("$.cores[0]", equalTo(pet.getCores().toArray()[0].toString())))
                .andExpect(jsonPath("$.cores[1]", equalTo(pet.getCores().toArray()[1].toString())))
                .andExpect(jsonPath("$.fotos[0]", equalTo(pet.getFotos().toArray()[0].toString())))
                .andExpect(jsonPath("$.fotos[1]", equalTo(pet.getFotos().toArray()[1].toString())));
    }

    @Test
    public void obterPet_quandoNaoExiste_entaoErro() throws Exception {
        mvc.perform(get("/pets/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void buscarPets_quandoEncontra_entaoRetorna() throws Exception {
        List<Pet> pets = new ArrayList<>();
        Set<Cor> coresA = new HashSet<>();
        coresA.add(Cor.BRANCO);
        coresA.add(Cor.MARROM);
        coresA.add(Cor.PRETO);
        Set<Cor> coresB = new HashSet<>();
        coresB.add(Cor.BRANCO);
        coresB.add(Cor.MARROM);
        pets.add(Pet.builder()
                .tipo(Tipo.CACHORRO)
                .porte(Porte.PEQUENO)
                .comprimentoPelo(ComprimentoPelo.SEM_PELO)
                .categoria(Categoria.PERDIDO)
                .cores(coresA)
                .build());
        pets.add(Pet.builder()
                .tipo(Tipo.CACHORRO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.SEM_PELO)
                .categoria(Categoria.PERDIDO)
                .raca(Raca.PITBULL)
                .cores(coresB)
                .build());
        pets.add(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.SEM_PELO)
                .categoria(Categoria.PERDIDO)
                .raca(Raca.SIAMES)
                .build());
        pets.forEach(petService::save);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("tipo", "CACHORRO");
        params.add("racas", "PITBULL");
        params.add("cores", "BRANCO");
        params.add("cores", "MARROM");

        Pet petEsperado = pets.get(1);

        mvc.perform(get("/pets/search")
                .params(params))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tipo", equalTo(petEsperado.getTipo().toString())))
                .andExpect(jsonPath("$[0].porte", equalTo(petEsperado.getPorte().toString())))
                .andExpect(jsonPath("$[0].cores[0]", equalTo(petEsperado.getCores().toArray()[0].toString())));
    }

    @Test
    public void deletarPet_quandoAutorizado_entaoSucesso() throws Exception {
        final Usuario usuario = usuarioService.save(Usuario.builder()
                .nome("Usuário")
                .email("usuario@example.com")
                .senha("hunter2")
                .build());
        final Pet pet = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .usuario(usuario)
                .build());

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(delete(String.format("/pets/%d", pet.getId()))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertThat(petService.findAll(), hasSize(0));
    }

    @Test
    public void deletarPet_quandoNaoAutorizado_entaoErro() throws Exception {
        final Usuario usuarioA = usuarioService.save(Usuario.builder()
                .nome("Usuário A")
                .email("usuario.a@example.com")
                .senha("hunter2")
                .build());
        final Usuario usuarioB = usuarioService.save(Usuario.builder()
                .nome("Usuário B")
                .email("usuario.b@example.com")
                .senha("hunter2")
                .build());
        final Pet pet = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .usuario(usuarioA)
                .build());

        String tokenB = jwtTokenProvider.getToken(usuarioB.getUsername(), Collections.emptyList());
        mvc.perform(delete(String.format("/pets/%d", pet.getId()))
                .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isForbidden());

        assertThat(petService.findAll(), hasSize(1));
    }

    @Test
    public void editarPet_quandoValidoEAutorizado_entaoSucesso() throws Exception {
        Usuario usuario = usuarioService.save(Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .build());
        Set<Cor> cores = new HashSet<>();
        cores.add(Cor.BRANCO);
        cores.add(Cor.PRETO);
        List<String> fotos = new ArrayList<>();
        fotos.add("link1");
        fotos.add("link2");
        final Pet pet = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .cores(cores)
                .fotos(fotos)
                .build());
        pet.addRegistro(new Registro(pet, Situacao.PROCURANDO));
        usuario.addPet(pet);

        String petJson = "{" +
                "   \"tipo\": \"CACHORRO\"," +
                "   \"porte\": \"MEDIO\"," +
                "   \"comprimento_pelo\": \"MEDIO\"," +
                "   \"categoria\": \"ACHADO\"," +
                "   \"cores\": [\"BRANCO\", \"PRETO\"]," +
                "	\"fotos\": [\"link1\", \"link2\"]" +
                "}";

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(put(String.format("/pets/%d", pet.getId()))
                .header("Authorization", "Bearer " + token)
                .content(petJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(pet.getTipo().toString(), equalTo("CACHORRO"));
        assertThat(pet.getPorte().toString(), equalTo("MEDIO"));
        assertThat(pet.getSituacaoAtual().toString(), equalTo("PROCURANDO"));
        assertThat(pet.getRaca().toString(), equalTo("CACHORRO_SRD"));
        assertThat(pet.getUsuario().getId(), equalTo(usuario.getId()));
    }

    @Test
    public void editarPet_quandoInvalidoEAutorizado_entaoErro() throws Exception {
        Usuario usuario = usuarioService.save(Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .build());
        final Pet pet = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .build());

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(put(String.format("/pets/%d", pet.getId()))
                .header("Authorization", "Bearer " + token)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editarPet_quandoNaoAutorizado_entaoErro() throws Exception {
        final Usuario usuarioA = usuarioService.save(Usuario.builder()
                .nome("Usuário A")
                .email("usuario.a@example.com")
                .senha("hunter2")
                .build());
        final Usuario usuarioB = usuarioService.save(Usuario.builder()
                .nome("Usuário B")
                .email("usuario.b@example.com")
                .senha("hunter2")
                .build());
        final Pet pet = petService.save(buildPet());
        usuarioB.addPet(pet);

        String petJson = "{" +
                "   \"tipo\": \"CACHORRO\"," +
                "   \"porte\": \"MEDIO\"," +
                "   \"comprimento_pelo\": \"MEDIO\"," +
                "   \"categoria\": \"ACHADO\"," +
                "   \"cores\": [\"BRANCO\", \"PRETO\"]" +
                "}";
        String tokenA = jwtTokenProvider.getToken(usuarioA.getUsername(), Collections.emptyList());
        mvc.perform(put(String.format("/pets/%d", pet.getId()))
                .header("Authorization", "Bearer " + tokenA)
                .content(petJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void adicionarRegistro_quandoValido_entaoSucesso() throws Exception {
        final Usuario usuario = usuarioService.save(buildUsuario());
        final Pet pet = petService.save(buildPet());
        pet.addRegistro(new Registro(pet, Situacao.PROCURANDO));
        usuario.addPet(pet);

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(post(String.format("/pets/%d/registros", pet.getId()))
                .header("Authorization", "Bearer " + token)
                .content("{\"situacao\": \"ENCONTRADO\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(pet.getSituacaoAtual().toString(), equalTo("ENCONTRADO"));
    }

    @Test
    public void adicionarRegistro_quandoInvalido_entaoErro() throws Exception {
        final Usuario usuario = usuarioService.save(buildUsuario());
        final Pet pet = petService.save(buildPet());
        usuario.addPet(pet);

        String token = jwtTokenProvider.getToken(usuario.getUsername(), Collections.emptyList());
        mvc.perform(post(String.format("/pets/%d/registros", pet.getId()))
                .header("Authorization", "Bearer " + token)
                .content("{\"situacao\": \"isso não é uma situação\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertThat(pet.getSituacaoAtual(), nullValue());
    }
}
