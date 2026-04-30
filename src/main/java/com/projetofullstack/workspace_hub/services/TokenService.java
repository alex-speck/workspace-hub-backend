package com.projetofullstack.workspace_hub.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.projetofullstack.workspace_hub.exceptions.InvalidSessionException;
import com.projetofullstack.workspace_hub.model.entities.Token;
import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.repository.TokenRepository;
import com.projetofullstack.workspace_hub.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.issuer}")
    private String emissor;

    @Value("${spring.security.jwt.expires-at-min}")
    private Long tempoDeExpiracao;

    @Autowired
    private TokenRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(emissor)
                    .build();
            verifier.verify(token);

            return repository.findTokenByToken(token)
                    .orElseThrow(()-> new InvalidSessionException("Token invalido!"))
                    .getUsuario();
        } catch (Exception e) {
            throw new InvalidSessionException("Token invalido ou expirado!");
        }

    }

    public String gerarToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer(emissor)
                    .withSubject(email)
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);

            var usuario = usuarioRepository.findAll()
                    .stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst()
                    .orElse(null);


            repository.save(new Token(token, usuario));
            return token;
        } catch (Exception e) {
            return null;
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusMinutes(tempoDeExpiracao).toInstant(ZoneOffset.of("-03:00"));
    }

}
