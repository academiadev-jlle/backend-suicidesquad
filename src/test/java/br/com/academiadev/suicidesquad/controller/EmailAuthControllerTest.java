package br.com.academiadev.suicidesquad.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.academiadev.suicidesquad.security.AuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmailAuthControllerTest {
	@Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void efetuarLogin_quandoCredenciasInvalidas_entaoFalha() throws Exception {
    	mvc.perform(
    			post("/auth/email")
    			.content(objectMapper.writeValueAsString(new AuthenticationRequest()))
    			.contentType(MediaType.APPLICATION_JSON)
    			)
    	.andExpect(status().isUnauthorized());
    }
    
    @Test
    public void efetuarLogin_quandoCredenciasValias_entaoSucesso() throws JsonProcessingException, Exception {
    	final Map<String, String> usuarioJson = new HashMap<>();
        usuarioJson.put("nome", "Fulano");
        usuarioJson.put("email", "fulano@example.com");
        usuarioJson.put("senha", "hunter2");
        
    	mvc
    	.perform(
    			post("/usuarios")
    			.content(objectMapper.writeValueAsString(usuarioJson))
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isCreated());
    	
    	AuthenticationRequest data = new AuthenticationRequest();
    	data.setEmail(usuarioJson.get("email"));
    	data.setPassword(usuarioJson.get("senha"));
    	
    	mvc
    	.perform(
    			post("/auth/email")
    			.content(objectMapper.writeValueAsString(data))
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk());
    	
    }
}
