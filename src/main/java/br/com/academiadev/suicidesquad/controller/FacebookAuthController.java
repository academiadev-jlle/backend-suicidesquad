package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
import br.com.academiadev.suicidesquad.service.FacebookService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@RestController
@ConditionalOnProperty(prefix = "app.social.facebook", name = "enabled")
@RequestMapping("/auth/facebook")
public class FacebookAuthController {

    private final FacebookService facebookService;

    private final UsuarioService usuarioService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.social.facebook.frontend-redirect-uri:}")
    private String frontendRedirectUri;

    @Autowired
    public FacebookAuthController(FacebookService facebookService, UsuarioService usuarioService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.facebookService = facebookService;
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/authorization")
    public void getFacebookAuthorization(HttpServletResponse response) throws IOException {
        response.sendRedirect(facebookService.createAuthorizationURL());
    }

    @GetMapping("/callback")
    public void facebookCallback(HttpServletResponse response, @RequestParam("code") String code) throws IOException {
        Optional<User> facebookUser = facebookService.getAccessToken(code).flatMap(facebookService::getUser);

        if (facebookUser.isPresent()) {
            Usuario usuario = facebookService.buildUsuarioFromFacebookUser(facebookUser.get());
            final String email = usuario.getEmail();
            if (email == null) {
                // TODO: Tratar quando o usuário não tem email
            }

            String token = jwtTokenProvider.getToken(email, Collections.emptyList());
            Cookie tokenCookie = new Cookie("token", token);
            tokenCookie.setPath("/");
            tokenCookie.setDomain("localhost");
            response.addCookie(tokenCookie);
            usuarioService.save(usuario);
        }

        response.sendRedirect(frontendRedirectUri);
    }

}
