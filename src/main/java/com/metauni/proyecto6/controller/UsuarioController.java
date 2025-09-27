package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")  // ← CORREGIR: agregar /api/
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepo.findAll();
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return usuarioRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PutMapping("/{id}")
    public Usuario editar(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        System.out.println("🔍 PUT /usuarios/" + id);

        try {
            // 1. Buscar usuario
            Usuario usuarioExistente = usuarioRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 2. Actualizar campos
            if (usuarioActualizado.getNombre() != null) {
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
            }
            if (usuarioActualizado.getEmail() != null) {
                usuarioExistente.setEmail(usuarioActualizado.getEmail());
            }

            // 3. Guardar
            return usuarioRepo.save(usuarioExistente);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioRepo.deleteById(id);
    }
}
