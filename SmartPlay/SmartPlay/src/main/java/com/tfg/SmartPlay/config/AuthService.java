package com.tfg.SmartPlay.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.util.Date;

import javax.crypto.SecretKey;
@Service
public class AuthService {

    static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String authenticateUser(String email) {
        // Generación del JWT
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Expira en 1 hora
                .signWith(SignatureAlgorithm.HS256, key) // Usa el objeto SecretKey directamente
                .compact();
    }
}

