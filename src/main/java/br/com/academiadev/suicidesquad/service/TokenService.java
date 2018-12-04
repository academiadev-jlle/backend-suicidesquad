package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    private String generateToken(String content, Long expireLengthMs, String secretKey) {
        Claims claims = Jwts.claims()
                .setSubject(content);

        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expireLengthMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private String unverifiedParseToken(String token) throws InvalidTokenException {
        String[] splitToken = token.split("\\.");
        if (splitToken.length < 2) {
            throw new InvalidTokenException();
        }
        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";

        DefaultJwtParser parser = new DefaultJwtParser();
        Jwt<?, ?> jwt = parser.parse(unsignedToken);
        return ((Claims) jwt.getBody()).getSubject();
    }

    private String parseToken(String token, String secretKey) throws InvalidTokenException {
        Jws<Claims> claims;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            final Date now = new Date();
            if (claims.getBody().getExpiration().before(now)) {
                throw new InvalidTokenException();
            }
        } catch (JwtException | IllegalArgumentException ignored) {
            throw new InvalidTokenException();
        }
        return claims.getBody().getSubject();
    }

    private String getRedefinicaoSenhaSecretKey(Usuario usuario) {
        return usuario.getSenha() + usuario.getCreatedDate().toEpochSecond(ZoneOffset.UTC);
    }

    public String generateRedefinicaoSenhaToken(Usuario usuario) {
        // Uma hora
        final long validadeMs = 3600000L;
        return generateToken(usuario.getEmail(), validadeMs, getRedefinicaoSenhaSecretKey(usuario));
    }

    String getEmailFromRedefinicaoSenhaToken(String token) {
        String subject;
        try {
            subject = unverifiedParseToken(token);
        } catch (InvalidTokenException e) {
            return null;
        }
        return subject;

    }

    public boolean validateRedefinicaoSenhaToken(Usuario usuario, String token) {
        String subject;
        try {
            subject = parseToken(token, getRedefinicaoSenhaSecretKey(usuario));
        } catch (InvalidTokenException e) {
            return false;
        }
        return subject != null && subject.equals(usuario.getEmail());
    }
}
