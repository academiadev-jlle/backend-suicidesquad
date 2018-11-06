package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Registro;
import br.com.academiadev.suicidesquad.enums.*;
import br.com.academiadev.suicidesquad.service.PetService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PetController.class)
@EnableSpringDataWebSupport
public class PetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PetService petService;

    private Pet buildPet() {
        return Pet.builder()
                .categoria(Categoria.ACHADO)
                .tipo(Tipo.CACHORRO)
                .porte(Porte.PEQUENO)
                .raca(Raca.CACHORRO_SRD)
                .cor(Cor.MARROM)
                .cor(Cor.BRANCO)
                .pelo(Pelo.CURTO)
                .sexo(SexoPet.MACHO)
                .registro(new Registro(Situacao.PROCURANDO))
                .build();
    }

    private List<Pet> buildPets() {
        Pet petSemRaca = Pet.builder()
                .categoria(Categoria.ACHADO)
                .tipo(Tipo.GATO)
                .porte(Porte.MEDIO)
                .build();

        Pet petComRaca = Pet.builder()
                .categoria(Categoria.PERDIDO)
                .tipo(Tipo.EQUINO)
                .porte(Porte.GRANDE)
                .raca(Raca.LUSITANO)
                .sexo(SexoPet.MACHO)
                .vacinacao(Vacinacao.PARCIAL)
                .castracao(Castracao.NAO_INFORMADO)
                .build();

        Pet petComCor = Pet.builder()
                .categoria(Categoria.PARA_ADOCAO)
                .tipo(Tipo.CACHORRO)
                .porte(Porte.PEQUENO)
                .raca(Raca.LABRADOR)
                .cor(Cor.BRANCO)
                .build();

        return Arrays.asList(
                petSemRaca,
                petComRaca,
                petComCor
        );
    }
    
    @Test
    public void getPetsComParametrosDeUrl() throws Exception {
    	List<Pet> pets = buildPets();
    	
    	when(petService.findAll(any(String.class), any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(pets.get(1))));
    	this.mvc.perform(get("/v1/pet/find?tipo=equino")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].tipo", equalTo("EQUINO")));
    	this.mvc.perform(get("/v1/pet/find?categoria=perdido")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].categoria", equalTo("PERDIDO")));
    	this.mvc.perform(get("/v1/pet/find?porte=grande")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].porte", equalTo("GRANDE")));
    	this.mvc.perform(get("/v1/pet/find?raca=lusitano")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].raca", equalTo("LUSITANO")));
    	this.mvc.perform(get("/v1/pet/find?sexo=macho")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].sexo", equalTo("MACHO")));
    	this.mvc.perform(get("/v1/pet/find?vacinacao=parcial")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].vacinacao", equalTo("PARCIAL")));
    	this.mvc.perform(get("/v1/pet/find?castracao=nao_informado")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].castracao", equalTo("NAO_INFORMADO")));
    }
    
    @Test
    public void getPets_ComPets() throws Exception {
        List<Pet> pets = buildPets();

        Page<Pet> pagedResponse = new PageImpl<>(pets);
        when(petService.findAll(any(String.class), any(Pageable.class))).thenReturn(pagedResponse);

        ResultActions result = this.mvc.perform(get("/v1/pet/find"))
                .andExpect(status().isOk());

        int idx = 0;
        for (Pet pet : pets) {
            result = result.andExpect(jsonPath(String.format("$.content[%d].categoria", idx), equalTo(pet.getCategoria().toString())));
            result = result.andExpect(jsonPath(String.format("$.content[%d].tipo", idx), equalTo(pet.getTipo().toString())));
            result = result.andExpect(jsonPath(String.format("$.content[%d].porte", idx), equalTo(pet.getPorte().toString())));
            if (pet.getRaca() != null) {
                result = result.andExpect(jsonPath(String.format("$.content[%d].raca", idx), equalTo(pet.getRaca().toString())));
            }
            result = result.andExpect(jsonPath(String.format("$.content[%d].cores", idx), hasSize(pet.getCores().size())));
            idx++;
        }
    }

    @Test
    public void getPets_SemPets() throws Exception {
        Page<Pet> pagedResponse = new PageImpl<>(new ArrayList<>());
        when(petService.findAll(any(String.class), any(Pageable.class))).thenReturn(pagedResponse);

        this.mvc.perform(get("/v1/pet/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    public void getPet_Existente() throws Exception {
        Pet pet = buildPet();

        when(petService.findById(1L)).thenReturn(Optional.of(pet));

        this.mvc.perform(get("/v1/pet/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria", equalTo(pet.getCategoria().toString())))
                .andExpect(jsonPath("$.tipo", equalTo(pet.getTipo().toString())))
                .andExpect(jsonPath("$.porte", equalTo(pet.getPorte().toString())))
                .andExpect(jsonPath("$.raca", equalTo(pet.getRaca().toString())))
                .andExpect(jsonPath("$.cores", hasSize(pet.getCores().size())))
                .andExpect(jsonPath("$.pelo", equalTo(pet.getPelo().toString())))
                .andExpect(jsonPath("$.sexo", equalTo(pet.getSexo().toString())))
                .andExpect(jsonPath("$.registros[0].situacao", equalTo(pet.getRegistros().get(0).getSituacao().toString())));
    }

    @Test
    public void getPet_Inexistente() throws Exception {
        when(petService.findById(1L)).thenReturn(Optional.empty());

        this.mvc.perform(get("v1/pet/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createPet_Valido() throws Exception {
        JSONObject petJson = new JSONObject();
        petJson.put("categoria", "ACHADO");
        petJson.put("tipo", "CACHORRO");
        petJson.put("porte", "PEQUENO");
        petJson.put("raca", "CACHORRO_SRD");
        petJson.put("pelo", "CURTO");

        this.mvc
                .perform(post("/v1/pet/add")
                        .content(petJson.toString())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        ArgumentCaptor<Pet> argument = ArgumentCaptor.forClass(Pet.class);
        verify(petService, times(1)).save(argument.capture());

        assertThat(argument.getValue().getCategoria(), equalTo(Categoria.valueOf(petJson.getString("categoria"))));
        assertThat(argument.getValue().getTipo(), equalTo(Tipo.valueOf(petJson.getString("tipo"))));
        assertThat(argument.getValue().getPorte(), equalTo(Porte.valueOf(petJson.getString("porte"))));
        assertThat(argument.getValue().getRaca(), equalTo(Raca.valueOf(petJson.getString("raca"))));
    }

    @Test
    public void createPet_Invalido() throws Exception {
        this.mvc
                .perform(post("/v1/pet/add")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        verify(petService, never()).save(any(Pet.class));
    }
}