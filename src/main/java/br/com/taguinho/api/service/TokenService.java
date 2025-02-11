package br.com.taguinho.api.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.taguinho.api.model.Tutor;

@Configuration
public class TokenService {

  @Value("${spring.security.token.secret}")
  private String secret;

  public String generateToken(Tutor tutor) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      String token = JWT.create()
          .withIssuer("taguinho_api")
          .withSubject(tutor.getEmail())
          .withExpiresAt(this.getExpirationDate())
          .sign(algorithm);

      return token;

    } catch (JWTCreationException exception) {
      throw new RuntimeException("Error while generating token", exception);
    }
  }

  private Instant getExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.require(algorithm)
          .withIssuer("taguinho_api")
          .build()
          .verify(token)
          .getSubject();

    } catch (JWTVerificationException exception) {
      return "";
    }
  }
}
