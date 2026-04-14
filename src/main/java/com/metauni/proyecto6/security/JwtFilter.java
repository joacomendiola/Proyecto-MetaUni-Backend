package com.metauni.proyecto6.security;

import io.jsonwebtoken.Claims;
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

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("🔍 JwtFilter - " + request.getMethod() + " " + request.getServletPath());

        // 1. Dejar pasar preflight OPTIONS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("✅ Pasando OPTIONS preflight");
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getServletPath();

        // 2. Dejar libre endpoints públicos
        if (path.startsWith("/api/auth")) {
            System.out.println("✅ Pasando ruta pública: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Para rutas protegidas, verificar token PERO si no hay, DEJAR SEGUIR
        String header = request.getHeader("Authorization");
        System.out.println("🔍 Authorization header: " + (header != null ? "PRESENTE" : "FALTANTE"));

        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("ℹ️  No hay token, dejando seguir");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = jwtUtil.extractClaims(token);

            String email = claims.getSubject();
            String rol = claims.get("rol", String.class);

            if (rol == null) {
                rol = "ROLE_USER";
            }
            if (!rol.startsWith("ROLE_")) {
                rol = "ROLE_" + rol;
            }

            if (email != null) {
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(rol));
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

