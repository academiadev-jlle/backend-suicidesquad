package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;


    @GetMapping("/usuarios")
    @ResponseBody
    public List<Usuario> buscarTodos(){
        Iterable<Usuario> todos = repository.findAll();
        System.out.println(toList(todos).toString());
        return toList(todos);
    }

    public <E> List<Usuario> toList(Iterable<E> iterable) {
        return (List<Usuario>) StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

}
