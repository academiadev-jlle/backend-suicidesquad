package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.dto.AlterarSenhaDTO;
import br.com.academiadev.suicidesquad.dto.IniciarRedefinicaoSenhaDTO;
import br.com.academiadev.suicidesquad.dto.RedefinirSenhaDTO;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.UsuarioNotFoundException;
import br.com.academiadev.suicidesquad.service.PasswordService;
import br.com.academiadev.suicidesquad.service.RedefinicaoSenhaService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/senhas/")
public class SenhaController {

    private final UsuarioService usuarioService;

    private final RedefinicaoSenhaService redefinicaoSenhaService;

    @Autowired
    public SenhaController(RedefinicaoSenhaService redefinicaoSenhaService, UsuarioService usuarioService) {
        this.redefinicaoSenhaService = redefinicaoSenhaService;
        this.usuarioService = usuarioService;
    }

    @PutMapping("alterar")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Senha alterada"),
            @ApiResponse(code = 400, message = "Campos inválidos"),
            @ApiResponse(code = 401, message = "Senha atual incorreta"),
            @ApiResponse(code = 403, message = "Não autorizado"),
    })
    public void alterarSenha(@Valid @RequestBody AlterarSenhaDTO alterarSenhaDTO, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (!PasswordService.encoder().matches(alterarSenhaDTO.getSenha_atual(), usuarioLogado.getPassword())) {
           throw new BadCredentialsException("Senha atual incorreta");
        }
        usuarioLogado.setSenha(PasswordService.encoder().encode(alterarSenhaDTO.getSenha_nova()));
    }

    @PutMapping("requisitar_redefinicao")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Token enviado para o email"),
            @ApiResponse(code = 400, message = "Campos inválidos"),
            @ApiResponse(code = 404, message = "Usuário com este email não foi encontrado")
    })
    public void requisitarRedefinicaoSenha(@Valid @RequestBody IniciarRedefinicaoSenhaDTO iniciarRedefinicaoSenhaDTO) {
        Usuario usuario = usuarioService.findByEmail(iniciarRedefinicaoSenhaDTO.getEmail())
                .orElseThrow(UsuarioNotFoundException::new);
        redefinicaoSenhaService.iniciarRedefinicao(usuario);
    }

    @PutMapping("redefinir")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Senha redefinida"),
            @ApiResponse(code = 400, message = "Token inválido")
    })
    public void redefinirSenha(@Valid @RequestBody RedefinirSenhaDTO redefinirSenhaDTO) {
        redefinicaoSenhaService.completarRedefinicao(redefinirSenhaDTO.getToken(), redefinirSenhaDTO.getSenha_nova());
    }
}
