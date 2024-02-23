package com.example.beautybook.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Key secretKey;
    private final Key mailSecretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.mail.verification.secret}") String mailSecretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.mailSecretKey = Keys.hmacShaKeyFor(mailSecretKey.getBytes(StandardCharsets.UTF_8));

    }

    public String generateToken(String username, Secret secret) {
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + expiration))
                   .signWith(getSecretKey(secret))
                   .compact();
    }

    public boolean isValidToken(String token, Secret secret) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                                        .setSigningKey(getSecretKey(secret))
                                        .build()
                                        .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Expired or invalid JWT token", e);
        }
    }

    public String getUsername(String token, Secret secret) {
        return getClaimsFromToken(token, Claims::getSubject, secret);
    }

    private <T> T getClaimsFromToken(
            String token,
            Function<Claims, T> claimsResolver,
            Secret secret) {
        final Claims claims = Jwts.parserBuilder()
                                  .setSigningKey(getSecretKey(secret))
                                  .build()
                                  .parseClaimsJws(token)
                                  .getBody();
        return claimsResolver.apply(claims);
    }

    private Key getSecretKey(Secret secret) {
        if (secret.equals(Secret.AUTH)) {
            return secretKey;
        }
        return mailSecretKey;
    }

    public enum Secret {
        AUTH,
        MAIL
    }
}
