package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.dto.UsuarioFacebookCreateDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioFacebookLoginDTO;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.EmailExistenteException;
import br.com.academiadev.suicidesquad.exception.InvalidAccessTokenException;
import br.com.academiadev.suicidesquad.exception.UsuarioExistenteException;
import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
import br.com.academiadev.suicidesquad.service.FacebookService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@RestController
@ConditionalOnProperty(prefix = "app.social.facebook", name = "enabled")
@RequestMapping("/auth/facebook")
@Api(value = "Autenticação por meio do Facebook")
public class FacebookAuthController {

    private final FacebookService facebookService;

    private final UsuarioService usuarioService;

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.social.facebook.frontend-redirect-uri:}")
    private String frontendRedirectUri;

    @Autowired
    public FacebookAuthController(FacebookService facebookService, UsuarioService usuarioService, JwtTokenProvider jwtTokenProvider) {
        this.facebookService = facebookService;
        this.usuarioService = usuarioService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/authorization")
    @ApiOperation(
            value = "Redireciona para a URL de autorização do Facebook",
            notes = "Inicia a autenticação com um perfil do Facebook, através de um redirecionamento que requisitará " +
                    "as permissões para acessar as informações do perfil do usuário, e retornará um código por meio " +
                    "do endpoint de callback."
    )
    @ApiResponses({
            @ApiResponse(code = 302, message = "Redirecionamento com o cookie do token de sessão"),
            @ApiResponse(code = 302, message = "Redirecionamento sem nada (falha na autenticação"),
    })
    public void getFacebookAuthorization(HttpServletResponse response) throws IOException {
        response.sendRedirect(facebookService.createAuthorizationURL());
    }

    private Cookie buildTokenCookie(String email) {
        String token = jwtTokenProvider.getToken(email, Collections.emptyList());
        Cookie tokenCookie = new Cookie("token", token);
        tokenCookie.setPath("/");
        tokenCookie.setDomain("localhost");
        return tokenCookie;
    }

    @GetMapping("/cadastrar_e_logar_via_callback")
    @ApiOperation(
            value = "Cadastrar e logar via callback",
            notes = "Cadastra um usuário e inicia uma sessão através do código devolvido pelo Facebook. " +
                    "Quando o usuário tem o email públicamente compartilhado no Facebook, esta etapa é " +
                    "suficiente para autenticar o usuário."
    )
    @ApiResponses({
            @ApiResponse(code = 302, message = "Redirecionamento com o cookie do token de sessão"),
            @ApiResponse(code = 302, message = "Redirecionamento sem o cookie"),
            @ApiResponse(code = 302, message = "Redirecionamento sem nada (falha na autenticação)"),
    })
    public void cadastrarViaCallback(HttpServletResponse response, @RequestParam("code") String code) throws IOException {
        Optional<String> accessToken = facebookService.getAccessToken(code);
        Optional<User> facebookUser = accessToken.flatMap(facebookService::getUser);

        if (!facebookUser.isPresent()) {
            // Falha no login
            // Retorna para o frontend sem token de sessão
            response.sendRedirect(frontendRedirectUri);
            return;
        }

        Usuario usuario = facebookService.buildUsuarioFromFacebookUser(facebookUser.get());

        if (usuario.getEmail() != null) {
            Cookie tokenCookie = buildTokenCookie(usuario.getEmail());
            response.addCookie(tokenCookie);
        }

        response.sendRedirect(UriComponentsBuilder
                .fromHttpUrl(frontendRedirectUri)
                .queryParam("accessToken", accessToken.get())
                .build()
                .toUriString());
    }

    @PostMapping("/cadastrar_e_logar_com_email_suplementar")
    @ApiOperation(
            value = "Cadastrar e logar com email suplementar",
            notes = "Cadastra um usuário e inicia uma sessão através de um access token e um email suplementar. " +
                    "Usado quando o usuário não compartilhou o email na autenticação. O access token é retornado no " +
                    "redirecionamento do callback."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Retorna cookie com o token de sessão"),
            @ApiResponse(code = 400, message = "Usuário já cadastrado"),
            @ApiResponse(code = 400, message = "Email já usado"),
            @ApiResponse(code = 400, message = "Access token inválido"),
    })
    public void cadastrarComEmailSuplementar(HttpServletResponse response, @Valid @RequestBody UsuarioFacebookCreateDTO dto) {
        User facebookUser = facebookService.getUser(dto.getAccess_token())
                .orElseThrow(InvalidAccessTokenException::new);

        if (usuarioService.existsByFacebookUserId(facebookUser.getId())) {
            // Usuário já cadastrado
            throw new UsuarioExistenteException();
        }

        String emailSuplementar = dto.getEmail_suplementar();
        if (usuarioService.existsByEmail(emailSuplementar)) {
            // Email já usado
            throw new EmailExistenteException();
        }

        Usuario usuario = facebookService.buildUsuarioFromFacebookUser(facebookUser);
        usuario.setEmail(emailSuplementar);
        usuarioService.save(usuario);

        final Cookie tokenCookie = buildTokenCookie(usuario.getEmail());
        response.addCookie(tokenCookie);
    }

    @PostMapping("/logar_com_access_token")
    @ApiOperation(
            value = "Logar com access token",
            notes = "Inicia uma acessão através de um access token."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Retorna cookie com o token de sessão"),
            @ApiResponse(code = 400, message = "Token inválido")
    })
    public void loginComAccessToken(HttpServletResponse response, @Valid @RequestBody UsuarioFacebookLoginDTO dto) {
        Usuario usuario = facebookService.getUser(dto.getAccess_token())
                .flatMap(user -> usuarioService.findByFacebookUserId(user.getId()))
                .orElseThrow(InvalidAccessTokenException::new);

        Cookie tokenCookie = buildTokenCookie(usuario.getEmail());
        response.addCookie(tokenCookie);
    }
}
