package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.enums.SexoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FacebookService {
    @Value("${app.social.facebook.app-id:}")
    private String appId;

    @Value("${app.social.facebook.app-secret:}")
    private String appSecret;

    @Value("${app.social.facebook.backend-redirect-uri:}")
    private String backendRedirectUri;

    private final UsuarioService usuarioService;

    @Autowired
    public FacebookService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public static SexoUsuario determinarSexoUsuario(String facebookUserGender) {
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

    public Usuario buildUsuarioFromFacebookUser(User facebookUser) {
        return usuarioService.findByFacebookUserId(facebookUser.getId())
                .orElseGet(() -> Usuario.builder()
                        .nome(facebookUser.getName())
                        .email(facebookUser.getEmail())
                        .facebookUserId(facebookUser.getId())
                        .sexo(determinarSexoUsuario(facebookUser.getGender()))
                        .build());
    }

    public String createAuthorizationURL() {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(appId, appSecret);
        OAuth2Operations ops = connectionFactory.getOAuthOperations();

        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(backendRedirectUri);
        params.setScope("public_profile,email,user_gender");
        return ops.buildAuthorizeUrl(params);
    }

    public Optional<String> getAccessToken(String code) {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(appId, appSecret);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(
                code,
                backendRedirectUri,
                null);
        return Optional.ofNullable(accessGrant.getAccessToken());
    }

    public Optional<User> getUser(String accessToken) {
        Facebook facebook = new FacebookTemplate(accessToken);
        String[] fields = {
                "id",
                "first_name",
                "last_name",
                "middle_name",
                "name",
                "name_format",
                "picture",
                "short_name",
                "email",
                "gender"
        };
        return Optional.ofNullable(facebook.fetchObject("me", User.class, fields));
    }
}
