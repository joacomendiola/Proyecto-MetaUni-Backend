package com.metauni.proyecto6.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta segura (mínimo 32 caracteres para HS256)
    private final SecretKey SECRET = Keys.hmacShaKeyFor(
            "metauni_secret_key_metauni_secret_key_extra_chars".getBytes()
    );

    // Generar token
    public String generateToken(String email, String rol) {
        //  Asegurar prefijo ROLE_
        if (rol != null && !rol.startsWith("ROLE_")) {
            rol = "ROLE_" + rol;
        }
        if (rol == null) {
            rol = "ROLE_USER"; // valor por defecto
        }

        return Jwts.builder()
                .setSubject(email)
                .claim("rol", rol)  // Guardamos el rol con formato correcto
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(SECRET, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraer claims de un token
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
