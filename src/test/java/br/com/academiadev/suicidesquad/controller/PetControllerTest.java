package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.enums.Porte;
import br.com.academiadev.suicidesquad.enums.Tipo;
import br.com.academiadev.suicidesquad.repository.PetRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    private PetRepository petRepository;

    @Test
    public void getPets() throws Exception {
        Pet pet1 = new Pet();
        pet1.setTipo(Tipo.CACHORRO);
        pet1.setPorte(Porte.MEDIO);
        Pet pet2 = new Pet();
        pet2.setTipo(Tipo.EQUINO);
        pet2.setPorte(Porte.GRANDE);

        List<Pet> allPets = new ArrayList<>();
        allPets.add(pet1);
        allPets.add(pet2);

        Page<Pet> pagedResponse = new PageImpl<>(allPets);
        when(petRepository.findAll(any(Pageable.class))).thenReturn(pagedResponse);

        this.mvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tipo", is("CACHORRO")))
                .andExpect(jsonPath("$.content[0].porte", is("MEDIO")))
                .andExpect(jsonPath("$.content[1].tipo", is("EQUINO")));
    }

    @Test
    public void getPet() throws Exception {
        Pet pet = new Pet();
        pet.setTipo(Tipo.CACHORRO);
        pet.setPorte(Porte.PEQUENO);

        when(petRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(pet));

        this.mvc.perform(get("/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("CACHORRO")))
                .andExpect(jsonPath("$.porte", is("PEQUENO")));
    }

    @Test
    public void createPet() throws Exception {
        this.mvc
            .perform(post("/pets")
                    .content("{\"tipo\": \"CACHORRO\",  \"porte\": \"PEQUENO\"}")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        ArgumentCaptor<Pet> argument = ArgumentCaptor.forClass(Pet.class);
        verify(petRepository, times(1)).save(argument.capture());
        assertThat(argument.getValue().getTipo(), equalTo(Tipo.CACHORRO));
        assertThat(argument.getValue().getPorte(), equalTo(Porte.PEQUENO));
    }
}