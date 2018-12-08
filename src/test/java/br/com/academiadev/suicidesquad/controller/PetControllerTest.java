package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Registro;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.*;
import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
import br.com.academiadev.suicidesquad.service.PetService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        return Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .cores(cores)
                .build();
    }

    private Usuario buildUsuario() {
        return Usuario.builder()
                .nome("Exemplo")
                .email("example@example.com")
                .senha("hunter2")
                .build();
    }

    private Usuario buildUsuarioA() {
        return Usuario.builder()
                .nome("Usuário A")
                .email("usuario.a@example.com")
                .senha("hunter2")
                .build();
    }

    private Usuario buildUsuarioB() {
        return Usuario.builder()
                .nome("Usuário B")
                .email("usuario.b@example.com")
                .senha("hunter2")
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
                .andExpect(jsonPath("$.id", equalTo(pet.getId().toString())))
                .andExpect(jsonPath("$.tipo", equalTo(pet.getTipo().toString())))
                .andExpect(jsonPath("$.cores[0]", equalTo(pet.getCores().toArray()[0].toString())))
                .andExpect(jsonPath("$.cores[1]", equalTo(pet.getCores().toArray()[1].toString())));
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
        final Usuario usuario = usuarioService.save(buildUsuarioA());
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
        final Usuario usuarioA = usuarioService.save(buildUsuarioA());
        final Usuario usuarioB = usuarioService.save(buildUsuarioB());
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
    public void dadoUsuarioEmailPrivado_buscarUsuarioPublicador_entaoEmailPublico() throws Exception {
        final Usuario usuario = buildUsuarioA();
        final Pet pet = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .usuario(usuario)
                .build());

        mvc.perform(get(String.format("/pets/%d", pet.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", equalTo(pet.getTipo().toString())))
                .andExpect(jsonPath("$.usuario.email", equalTo(usuario.getEmailPublico())))
                .andExpect(jsonPath("$.email", equalTo(usuario.getEmail())));
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
        final Pet pet = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .usuario(usuario)
                .build());

        mvc.perform(get(String.format("/pets/%d", pet.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", equalTo(pet.getTipo().toString())))
                .andExpect(jsonPath("$.usuario.email", equalTo(usuario.getEmailPublico())))
                .andExpect(jsonPath("$.email", equalTo(usuario.getEmail())));

    }

    @Test
    public void dadoUsuarioEmailPrivado_buscarUsuarioPublicador_entaoEmailNull() throws Exception {
        Usuario usuario = Usuario.builder()
                .nome("um usuario")
                .email("umemail@example.com")
                .emailPublico(false)
                .senha("hunter2")
                .build();
        final Pet pet = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .usuario(usuario)
                .build());
        mvc.perform(get(String.format("/pets/%d", pet.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", equalTo(pet.getTipo().toString())))
                .andExpect(jsonPath("$.email", equalTo(null)))
                .andExpect(jsonPath("$.usuario.email", equalTo(null)));
        Assertions.assertThat(usuario.getEmailPublico()).isEqualTo(null);
    }

    @Test
    public void editarPet_quandoInvalidoEAutorizado_entaoErro() throws Exception {
        Usuario usuario = usuarioService.save(Usuario.builder()
                .nome("Fulano")
                .email("fulano@example.com")
                .build());
        Set<Cor> cores = new HashSet<>();
        cores.add(Cor.BRANCO);
        cores.add(Cor.PRETO);
        final Pet pet = petService.save(Pet.builder()
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .comprimentoPelo(ComprimentoPelo.MEDIO)
                .categoria(Categoria.ACHADO)
                .cores(cores)
                .build());
        pet.addRegistro(new Registro(pet, Situacao.PROCURANDO));
        usuario.addPet(pet);

        String petJson = "{" +
                "   \"tipo\": \"CACHORRO\"," +
                "   \"porte\": \"MEDIO\"," +
                "   \"comprimento_pelo\": \"MEDIO\"," +
                "   \"categoria\": \"ACHADO\"," +
                "   \"cores\": [\"BRANCO\", \"PRETO\"]" +
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
