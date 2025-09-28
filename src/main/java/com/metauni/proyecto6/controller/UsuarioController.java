package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
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
        System.out.println("✅ PUT /api/usuarios/" + id + " - Editando perfil");

        try {
            // 1. Buscar usuario
            Usuario usuarioExistente = usuarioRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 2. Actualizar campos permitidos
            if (usuarioActualizado.getNombre() != null) {
                System.out.println("📝 Actualizando nombre: " + usuarioActualizado.getNombre());
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
            }
            if (usuarioActualizado.getEmail() != null) {
                System.out.println("📝 Actualizando email: " + usuarioActualizado.getEmail());
                usuarioExistente.setEmail(usuarioActualizado.getEmail());
            }

            // 3. Guardar cambios
            Usuario guardado = usuarioRepo.save(usuarioExistente);
            System.out.println("✅ Perfil actualizado exitosamente");
            return guardado;

        } catch (Exception e) {
            System.out.println("❌ Error actualizando perfil: " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioRepo.deleteById(id);
    }
}