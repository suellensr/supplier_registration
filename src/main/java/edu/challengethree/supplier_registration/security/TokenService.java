package edu.challengethree.supplier_registration.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import edu.challengethree.supplier_registration.model.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("supplier-registration-api")
                    .withSubject(user.getUsername())
                    .withClaim("userId", user.getId())
                    .withExpiresAt(generateExpirateDate())
                    .sign(algorithm);

        } catch (JWTCreationException ex) {
            throw new RuntimeException("Error while generating token");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("supplier-registration-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException ex) {
            return "";
        }
    }

    public String validateTokenAndGetUserId(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("supplier-registration-api")
                    .build()
                    .verify(token)
                    .getClaim("userId").asString();
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Invalid token");
        }
    }

    private Instant generateExpirateDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
