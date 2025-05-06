package com.bookstore.users.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final long JWT_TOKEN_VALIDITY = 1000L * 60 * 60;
    private static final long JWT_REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 30;
    private static final String ROLES = "role";

    @Value("{jwt.secret}")
    private String secret;

    public String generateAccessToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .withIssuedAt(Instant.now())
                .withClaim(ROLES, userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(this.getAlgorithm());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
                .withIssuedAt(Instant.now())
                .sign(this.getAlgorithm());
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
    }


    public boolean isVerified(String token) {
        try {
            this.verifyAndDecode(token);
            return true;

        } catch (Exception e){
            return false;
        }
    }

    private DecodedJWT verifyAndDecode(String token) {
        return JWT.require(this.getAlgorithm()).build().verify(token);
    }

    public String getUsername(String token) {
        return this.verifyAndDecode(token).getSubject();
    }

    public String[] getAuthorities(String token) {
        return this.verifyAndDecode(token).getClaim(ROLES).asArray(String.class);
    }
}
