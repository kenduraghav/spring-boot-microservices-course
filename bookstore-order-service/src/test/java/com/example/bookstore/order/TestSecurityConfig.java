package com.example.bookstore.order;

import java.time.Instant;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    JwtDecoder jwtDecoder() {
        return token -> {
            Instant now = Instant.now();

            return Jwt.withTokenValue(token)
                    .header("alg", "none")
                    .claim("sub", "123456")
                    .claim("preferred_username", "testUser")
                    .claim("scope", "openid profile email")
                    .claim("scp", List.of("openid", "profile", "email"))
                    .claim("iss", "https://mock-issuer.test")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(3600))
                    .build();
        };
    }
}
