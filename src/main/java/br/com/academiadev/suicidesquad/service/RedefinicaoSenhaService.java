package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.InvalidTokenRedefinicaoSenhaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class RedefinicaoSenhaService {

    @Value("#{new Long(${security.jwt.token.expire-length-ms:3600000})}")
    private Long expireLengthMs = 3600000L;

    private final UsuarioService usuarioService;

    private final EmailService emailService;

    private final TokenService tokenService;

    @Autowired
    public RedefinicaoSenhaService(UsuarioService usuarioService, EmailService emailService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    private Optional<Usuario> getUsuarioFromToken(String token) {
        String email = tokenService.getEmailFromRedefinicaoSenhaToken(token);
        if (email == null) {
            return Optional.empty();
        }
        return usuarioService.findByEmail(email);
    }

    @Transactional
    public void iniciarRedefinicao(Usuario usuario) {
        final String token = tokenService.generateRedefinicaoSenhaToken(usuario);

        emailService.enviarParaUsuario(
                usuario,
                "Redefinição de senha",
                token);
    }

    @Transactional
    public void completarRedefinicao(String token, String novaSenha) {
        Usuario usuario = getUsuarioFromToken(token)
                .orElseThrow(InvalidTokenRedefinicaoSenhaException::new);
        if (!tokenService.validateRedefinicaoSenhaToken(usuario, token)) {
            throw new InvalidTokenRedefinicaoSenhaException();
        }
        usuario.setSenha(PasswordService.encoder().encode(novaSenha));
        usuarioService.save(usuario);
    }
}
