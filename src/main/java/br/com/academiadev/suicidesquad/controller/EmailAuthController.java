package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.mapper.UsuarioMapper;
import br.com.academiadev.suicidesquad.security.AuthenticationRequest;
import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/auth/email")
public class EmailAuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;

    private final UsuarioMapper usuarioMapper;

    @Autowired
    public EmailAuthController(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, ObjectMapper objectMapper, UsuarioMapper usuarioMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public ResponseEntity login(@RequestBody AuthenticationRequest data) throws JsonProcessingException {
        try {
            String email = data.getEmail();
            Usuario usuarioLogado = (Usuario) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, data.getPassword())).getPrincipal();
            String token = jwtTokenProvider.getToken(email, Collections.emptyList());

            final ObjectNode retorno = objectMapper.createObjectNode();
            retorno.put("token", token);
            retorno.set("usuario", objectMapper.valueToTree(usuarioMapper.toDto(usuarioLogado)));
            return ResponseEntity.ok(retorno);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email/password supplied");
        }
    }

}
