package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.UsuarioRepository;
import com.metauni.proyecto6.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @RequestMapping(value = "/register", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> registerOptions() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> loginOptions() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuario.setRol("ROLE_USER");
            Usuario saved = usuarioRepo.save(usuario);

            String token = jwtUtil.generateToken(saved.getEmail(), saved.getRol());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", saved.getEmail(),
                    "rol", saved.getRol(),
                    "nombre", saved.getNombre() != null ? saved.getNombre() : "",
                    "id", saved.getId().toString()
            ));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya está registrado");
        }
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Usuario usuario) {
        Usuario u = usuarioRepo.findByEmail(usuario.getEmail());

        if (u != null && passwordEncoder.matches(usuario.getPassword(), u.getPassword())) {
            String token = jwtUtil.generateToken(u.getEmail(), u.getRol());
            return Map.of(
                    "token", token,
                    "email", u.getEmail(),
                    "rol", u.getRol(),
                    "nombre", u.getNombre() != null ? u.getNombre() : "",
                    "id", u.getId().toString() // Devolver el ID
            );
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
    }

    @GetMapping("/test-cors")
    public ResponseEntity<String> testCors() {
        return ResponseEntity.ok("CORS OK");
    }
}
