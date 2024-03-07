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
    private final Key refreshSecretKey;

    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.mail.verification.secret}") String mailSecretKey,
                   @Value("${jwt.refresh.secret}") String refreshSecretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.mailSecretKey = Keys.hmacShaKeyFor(mailSecretKey.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey =
                Keys.hmacShaKeyFor(refreshSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, Secret secret) {
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + getExpiration(secret)))
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
        if (secret.equals(Secret.REFRESH)) {
            return refreshSecretKey;
        }
        return mailSecretKey;
    }

    private long getExpiration(Secret secret) {
        if (secret.equals(Secret.REFRESH)) {
            return refreshExpiration;
        }
        return expiration;
    }

    public enum Secret {
        AUTH,
        MAIL,
        REFRESH
    }
}
