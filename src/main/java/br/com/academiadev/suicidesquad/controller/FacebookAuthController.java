package br.com.academiadev.suicidesquad.controller;

import br.com.academiadev.suicidesquad.dto.UsuarioFacebookCreateDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioFacebookLoginDTO;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.EmailExistenteException;
import br.com.academiadev.suicidesquad.exception.InvalidAccessTokenException;
import br.com.academiadev.suicidesquad.exception.UsuarioExistenteException;
import br.com.academiadev.suicidesquad.mapper.UsuarioMapper;
import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
import br.com.academiadev.suicidesquad.service.FacebookService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    private final UsuarioMapper usuarioMapper;

    private final ObjectMapper objectMapper;

    @Value("${app.social.facebook.frontend-redirect-uri:}")
    private String frontendRedirectUri;

    @Autowired
    public FacebookAuthController(FacebookService facebookService, UsuarioService usuarioService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper, UsuarioMapper usuarioMapper) {
        this.facebookService = facebookService;
        this.usuarioService = usuarioService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping("/authorization")
    @ApiOperation(
            value = "Redireciona para a URL de autorização do Facebook",
            notes = "Inicia a autenticação com um perfil do Facebook, através de um redirecionamento que requisitará " +
                    "as permissões para acessar as informações do perfil do usuário, e retornará um código por meio " +
                    "do endpoint de callback."
    )
    @ApiResponses({
            @ApiResponse(code = 302, message = "Redirecionamento para o facebook"),
    })
    public void getFacebookAuthorization(HttpServletResponse response) throws IOException {
        response.sendRedirect(facebookService.createAuthorizationURL());
    }

    @GetMapping("/cadastrar_via_callback")
    @ApiOperation(
            value = "Cadastrar via callback",
            notes = "Cadastra um usuário através do código devolvido pelo Facebook. " +
                    "Quando o usuário tem o email públicamente compartilhado no Facebook, esta etapa é " +
                    "suficiente para cadastrar o usuário."
    )
    @ApiResponses({
            @ApiResponse(code = 302, message = "Redirecionamento com access token"),
            @ApiResponse(code = 302, message = "Redirecionamento sem nada (falha na autenticação)"),
    })
    public void cadastrarViaCallback(HttpServletResponse response, @RequestParam("code") String code) throws IOException {
        Optional<String> accessToken = facebookService.getAccessToken(code);
        Optional<User> facebookUser = accessToken.flatMap(facebookService::getUser);

        if (!facebookUser.isPresent()) {
            // Falha no login
            // Retorna para o frontend access token
            response.sendRedirect(frontendRedirectUri);
            return;
        }

        Usuario usuario = facebookService.buildUsuarioFromFacebookUser(facebookUser.get());
        boolean usuarioHasEmail = usuario.getEmail() != null;
        if (usuarioHasEmail) {
            usuarioService.save(usuario);
        }

        response.sendRedirect(UriComponentsBuilder
                .fromHttpUrl(frontendRedirectUri)
                .queryParam("accessToken", accessToken.get())
                .queryParam("hasEmail", usuarioHasEmail)
                .build()
                .toUriString());
    }

    @PostMapping("/cadastrar_com_email_suplementar")
    @ApiOperation(
            value = "Cadastrar com email suplementar",
            notes = "Cadastra um usuário através de um access token e um email suplementar. " +
                    "Usado quando o usuário não compartilhou o email na autenticação. O access token é retornado no " +
                    "redirecionamento do callback."
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastrado com sucesso"),
            @ApiResponse(code = 400, message = "Usuário já cadastrado"),
            @ApiResponse(code = 400, message = "Email já usado"),
            @ApiResponse(code = 400, message = "Access token inválido"),
    })
    @ResponseStatus(HttpStatus.CREATED)
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
    }

    @PostMapping("/logar_com_access_token")
    @ApiOperation(
            value = "Logar com access token",
            notes = "Inicia uma acessão através de um access token."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Logado com sucesso"),
            @ApiResponse(code = 400, message = "Token inválido")
    })
    public ResponseEntity loginComAccessToken(@Valid @RequestBody UsuarioFacebookLoginDTO dto) throws JsonProcessingException {
        Usuario usuario = facebookService.getUser(dto.getAccess_token())
                .flatMap(user -> usuarioService.findByFacebookUserId(user.getId()))
                .orElseThrow(InvalidAccessTokenException::new);

        String token = jwtTokenProvider.getToken(usuario.getEmail(), Collections.emptyList());
        ObjectNode tokenJson = objectMapper.createObjectNode();
        tokenJson.put("token", token);
        tokenJson.set("usuario", objectMapper.valueToTree(usuarioMapper.toDto(usuario)));
        return ResponseEntity.ok(objectMapper.writeValueAsString(tokenJson));
    }
}
