package com.app.gamereview.util;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static String SECRET_KEY;
    @Value("${SECRET_KEY}")
    private String secret_key; //
    @PostConstruct
    private void init() {
        SECRET_KEY = secret_key;
    }
    private static final long EXPIRATION_TIME = 864000000; // 10 day

    public static String generateToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            if (expirationDate.after(now)) {
                return true;
            }
        } catch (SignatureException | ExpiredJwtException | IllegalArgumentException e) {
            // SignatureException: Token signature is invalid
            // ExpiredJwtException: Token has expired
            // IllegalArgumentException: Token is not correctly formatted
            // In any of these cases, the token is considered invalid
        }

        return false; // Token is not valid
    }
}
