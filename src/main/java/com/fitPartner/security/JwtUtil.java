package com.fitPartner.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secret;

    public String generateToken(String email) {
        return Jwts
                .builder ()
                .setSubject (email)
                .setIssuedAt (new Date ())
                .setExpiration (new Date (System.currentTimeMillis () + 1000L * 60 * 60 * 24 * 30 ))
                .signWith (getKey(secret),SignatureAlgorithm.HS256)
                .compact ();
    }
//
    private Key getKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public Claims getTokenBody(String token) {
        return Jwts
                .parserBuilder ()
                .setSigningKey (getKey (secret))
                .build ()
                .parseClaimsJws (token)
                .getBody ();
    }

    public String extractName(String token) {
        return getTokenBody (token).getSubject ();
    }

    public Date extractExpDate(String token) {
        return getTokenBody (token).getExpiration ();
    }

    public boolean isTokenValid(String token, UserDetails details) {
        return extractName (token).equals (details.getUsername ())
                && new Date ().before (extractExpDate (token));
    }
}
