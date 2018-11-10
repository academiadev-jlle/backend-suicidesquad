package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.SexoUsuario;
import br.com.academiadev.suicidesquad.service.FacebookService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@ConditionalOnProperty(prefix = "app.social.facebook", name = "enabled")
@RequestMapping("/facebook")
public class FacebookAuthController {

    private final FacebookService facebookService;

    private final UsuarioService usuarioService;

    @Value("${app.social.facebook.frontend-redirect-uri:}")
    private String frontendRedirectUri;

    @Autowired
    public FacebookAuthController(FacebookService facebookService, UsuarioService usuarioService) {
        this.facebookService = facebookService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/authorization")
    public void getFacebookAuthorization(HttpServletResponse response) throws IOException {
        response.sendRedirect(facebookService.createAuthorizationURL());
    }

    @GetMapping("/callback")
    public void facebookCallback(HttpServletResponse response, @RequestParam("code") String code) throws IOException {
        Optional<User> facebookUser = facebookService.getAccessToken(code).flatMap(facebookService::getUser);

        if (facebookUser.isPresent()) {
            Usuario usuario = buildUsuarioFromFacebookUser(facebookUser.get());
            if (usuario.getEmail() == null) {
                // TODO: Tratar quando o usuário não tem email
            }
            // TODO: Gerar token de sessão colocar em um cookie
            final Cookie tokenCookie = new Cookie("token", "eu_sou_um_token");
            tokenCookie.setHttpOnly(true);
            response.addCookie(tokenCookie);
            usuarioService.save(usuario);
        }

        response.sendRedirect(frontendRedirectUri);
    }

    private Usuario buildUsuarioFromFacebookUser(User facebookUser) {
        return usuarioService.findByFacebookUserId(facebookUser.getId())
                .orElseGet(() -> Usuario.builder()
                        .nome(facebookUser.getName())
                        .email(facebookUser.getEmail())
                        .facebookUserId(facebookUser.getId())
                        .sexo(determinarSexoUsuario(facebookUser.getGender()))
                        .build());

    }

    private SexoUsuario determinarSexoUsuario(String facebookUserGender) {
        if (facebookUserGender == null) {
            return SexoUsuario.NAO_INFORMADO;
        } else if (facebookUserGender.equals("masculino")) {
            return SexoUsuario.MASCULINO;
        } else if (facebookUserGender.equals("feminino")) {
            return SexoUsuario.FEMININO;
        } else {
            return SexoUsuario.NAO_INFORMADO;
        }
    }
}
