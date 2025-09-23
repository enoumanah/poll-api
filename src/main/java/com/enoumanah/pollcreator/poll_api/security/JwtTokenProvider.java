package com.enoumanah.pollcreator.poll_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        return generateTokenFromUsername(username);
    }

    public String generateTokenFromUsername(String username) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key(), SignatureAlgorithm.HS512) // Use the key with an algorithm
                .compact();
    }

    private SecretKey key() {
        // This helper method is still correct
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // THIS METHOD IS NOW CORRECTED
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key()) // Use setSigningKey() instead of verifyWith()
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // THIS METHOD IS NOW CORRECTED
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key()) // Use setSigningKey() instead of verifyWith()
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

