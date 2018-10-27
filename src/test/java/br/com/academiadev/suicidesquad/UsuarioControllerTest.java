package br.com.academiadev.suicidesquad;

import br.com.academiadev.suicidesquad.controller.UsuarioController;
import br.com.academiadev.suicidesquad.entity.Localizacao;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.repository.UsuarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@WebMvcTest(UsuarioController.class)
@EnableSpringDataWebSupport
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioRepository usuarioRepository;


/*    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(usu).build();
    }*/

    @Test
    public void givenGetUsuariosThenUsuariosEncontrados() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("primeiroemail@gmail.com");
        usuario1.setNome("Nelson Nunes Guimarães");
        usuario1.setSenha("senha1");
        usuario1.setId(1l);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("segundoemail@gmail.com");
        usuario2.setNome("Salvador Di Bernardi");
        usuario2.setId(2l);
        usuario2.setSenha("senha2");

        List<Usuario> allUsuarios = new ArrayList<>();

        allUsuarios.add(usuario1);
        allUsuarios.add(usuario2);

        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);



        this.mvc.perform(get("/usuarios"))
                .andExpect(status().isOk());
         //       .andExpect(jsonPath("$.content[0].email", is("primeiroemail@gmail.com")));
        //.andExpect(jsonPath("$.content[0].nome", is("Nelson Nunes Guimarães")))
        //.andExpect(jsonPath("$.content[1].nome", is("Salvador Di Bernardi")));
    }


}
