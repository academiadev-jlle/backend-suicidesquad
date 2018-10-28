package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.enums.Cor;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Raca;
import br.com.academiadev.suicidesquad.enums.Tipo;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
        Pet pet = new Pet(Tipo.CACHORRO, Porte.PEQUENO, Raca.CACHORRO_SRD);
        pet.addCor(Cor.MARROM);
        pet.addCor(Cor.BRANCO);
        return pet;
    }

    private List<Pet> buildPets() {
        Pet petSemRaca = new Pet(Tipo.GATO, Porte.MEDIO);

        Pet petComRaca = new Pet(Tipo.EQUINO, Porte.GRANDE, Raca.LUSITANO);

        Pet petComCor = new Pet(Tipo.CACHORRO, Porte.PEQUENO, Raca.LABRADOR);
        petComCor.addCor(Cor.BRANCO);

        return Arrays.asList(
                petSemRaca,
                petComRaca,
                petComCor
        );
    }

    @Test
    public void getPets_ComPets() throws Exception {
        List<Pet> pets = buildPets();

        Page<Pet> pagedResponse = new PageImpl<>(pets);
        when(petService.findAll(any(Pageable.class))).thenReturn(pagedResponse);

        ResultActions result = this.mvc.perform(get("/pets"))
                .andExpect(status().isOk());

        int idx = 0;
        for (Pet pet : pets) {
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
        when(petService.findAll(any(Pageable.class))).thenReturn(pagedResponse);

        this.mvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    public void getPet_Existente() throws Exception {
        Pet pet = buildPet();

        when(petService.findById(1L)).thenReturn(Optional.of(pet));

        this.mvc.perform(get("/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", equalTo(pet.getTipo().toString())))
                .andExpect(jsonPath("$.porte", equalTo(pet.getPorte().toString())))
                .andExpect(jsonPath("$.raca", equalTo(pet.getRaca().toString())))
                .andExpect(jsonPath("$.cores", hasSize(pet.getCores().size())));
    }

    @Test
    public void getPet_Inexistente() throws Exception {
        when(petService.findById(1L)).thenReturn(Optional.empty());

        this.mvc.perform(get("/pets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createPet_Valido() throws Exception {
        JSONObject petJson = new JSONObject();
        petJson.put("tipo", "CACHORRO");
        petJson.put("porte", "PEQUENO");
        petJson.put("raca", "CACHORRO_SRD");

        this.mvc
            .perform(post("/pets")
                    .content(petJson.toString())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        ArgumentCaptor<Pet> argument = ArgumentCaptor.forClass(Pet.class);
        verify(petService, times(1)).save(argument.capture());

        assertThat(argument.getValue().getTipo(), equalTo(Tipo.valueOf(petJson.getString("tipo"))));
        assertThat(argument.getValue().getPorte(), equalTo(Porte.valueOf(petJson.getString("porte"))));
        assertThat(argument.getValue().getRaca(), equalTo(Raca.valueOf(petJson.getString("raca"))));
    }

    @Test
    public void createPet_Invalido() throws Exception {
        this.mvc
                .perform(post("/pets")
                         .content("{}")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        verify(petService, never()).save(any(Pet.class));
    }
}