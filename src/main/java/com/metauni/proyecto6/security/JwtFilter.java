package com.metauni.proyecto6.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final SecretKey SECRET = Keys.hmacShaKeyFor(
            "metauni_secret_key_metauni_secret_key_extra_chars".getBytes()
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("🔍 JwtFilter - " + request.getMethod() + " " + request.getServletPath());

        // Dejar pasar preflight OPTIONS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("✅ Pasando OPTIONS preflight");
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getServletPath();

        // Dejar libre SOLO endpoints públicos
        if (path.startsWith("/api/auth")) {
            System.out.println("✅ Pasando ruta pública: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        // Las demás rutas requieren autenticación
        String header = request.getHeader("Authorization");
        System.out.println("🔍 Authorization header: " + (header != null ? "PRESENTE" : "FALTANTE"));


        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("ℹ️  No hay token, dejando seguir");
            filterChain.doFilter(request, response);
            return;
        }
        // =============================================

        String token = header.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String rol = claims.get("rol", String.class);

            // Forzar prefijo ROLE_
            if (rol != null && !rol.startsWith("ROLE_")) {
                rol = "ROLE_" + rol;
            }
            if (rol == null) {
                rol = "ROLE_USER";
            }

            if (email != null) {
                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority(rol));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("✅ Usuario autenticado: " + email + " con rol: " + rol);
            }

        } catch (Exception e) {
            System.out.println("❌ Token inválido: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        filterChain.doFilter(request, response);
    }
}



