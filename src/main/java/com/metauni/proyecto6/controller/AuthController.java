package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.UsuarioRepository;
import com.metauni.proyecto6.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol("ROLE_USER");
        return usuarioRepo.save(usuario);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Usuario usuario) {
        Usuario u = usuarioRepo.findByEmail(usuario.getEmail());
        if (u != null && passwordEncoder.matches(usuario.getPassword(), u.getPassword())) {
            String token = jwtUtil.generateToken(u.getEmail(), u.getRol());
            return Map.of("token", token);
        }
        throw new RuntimeException("Credenciales inválidas");
    }
}
