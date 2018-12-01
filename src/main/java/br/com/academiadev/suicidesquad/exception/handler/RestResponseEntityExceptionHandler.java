package br.com.academiadev.suicidesquad.exception.handler;

import br.com.academiadev.suicidesquad.exception.EmailExistenteException;
import br.com.academiadev.suicidesquad.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @Autowired
    public RestResponseEntityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest request) throws JsonProcessingException {
        final ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("code", 500);
        errorNode.put("error", "Internal server error");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        e.printStackTrace();
        return handleExceptionInternal(e, objectMapper.writeValueAsString(errorNode), headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({
            ResourceNotFoundException.class
    })
    protected ResponseEntity<Object> handleResouceNotFound(ResourceNotFoundException e, WebRequest request) throws JsonProcessingException {
        final ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("code", 404);
        errorNode.put("error", e.getMessage());
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(e, objectMapper.writeValueAsString(errorNode), headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            EmailExistenteException.class
    })
    private ResponseEntity<Object> handleEmailExistete(EmailExistenteException e, WebRequest request) throws JsonProcessingException {
        final ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("code", 400);
        errorNode.put("error", "Este email já foi usado. Por favor, use outro endereço.");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(e, objectMapper.writeValueAsString(errorNode), headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@Nonnull MethodArgumentNotValidException e, @Nonnull HttpHeaders headers, @Nonnull HttpStatus status, @Nonnull WebRequest request) {
        List<ObjectNode> fieldErrorNodes = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> {
                    final ObjectNode fieldErrorNode = objectMapper.createObjectNode();
                    if (objectError instanceof FieldError) {
                        fieldErrorNode.put("field", ((FieldError) objectError).getField());
                        final Object rejectedValue = ((FieldError) objectError).getRejectedValue();
                        fieldErrorNode.put("value", rejectedValue == null ? null : rejectedValue.toString());
                        fieldErrorNode.put("error", objectError.getDefaultMessage());
                    } else {
                        fieldErrorNode.put("error", objectError.toString());
                    }
                    return fieldErrorNode;
                })
                .collect(Collectors.toList());

        final ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("code", 400);
        errorNode.put("error", "Campos inválidos");
        errorNode.putArray("field_errors").addAll(fieldErrorNodes);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(errorNode);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(e, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({
            BadCredentialsException.class
    })
    protected ResponseEntity<Object> handleBadCredentials(BadCredentialsException e, WebRequest request) throws JsonProcessingException {
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("code", 401);
        errorNode.put("error", "Email ou senha inválidos");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(e, objectMapper.writeValueAsString(errorNode), headers, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            AuthenticationException.class
    })
    protected ResponseEntity<Object> handleAuthenticationException(RuntimeException e, WebRequest request) throws JsonProcessingException {
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("code", 403);
        errorNode.put("error", "Não autorizado");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(e, objectMapper.writeValueAsString(errorNode), headers, HttpStatus.FORBIDDEN, request);
    }
}
