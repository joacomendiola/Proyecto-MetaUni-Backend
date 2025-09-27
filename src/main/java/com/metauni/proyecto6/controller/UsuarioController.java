package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    public Usuario editar(@PathVariable Long id, @RequestBody Usuario usuarioActualizado, Authentication authentication) {
        System.out.println("🔍 UsuarioController - PUT /usuarios/" + id);
        System.out.println("🔍 Usuario autenticado: " + authentication.getName());
        System.out.println("🔍 Datos recibidos: " + usuarioActualizado);

        try {
            // CON LOGS DE BÚSQUEDA
            System.out.println("🔍 Buscando usuario con ID: " + id);
            Usuario usuarioExistente = usuarioRepo.findById(id)
                    .orElseThrow(() -> {
                        System.out.println("❌ NO se encontró usuario con ID: " + id);
                        return new RuntimeException("Usuario no encontrado");
                    });
            System.out.println("✅ Usuario encontrado: " + usuarioExistente.getEmail());

            // VERIFICACIÓN DE SEGURIDAD - USUARIO SOLO PUEDE EDITAR SU PROPIO PERFIL
            if (!usuarioExistente.getEmail().equals(authentication.getName())) {
                System.out.println("❌ Intento de editar perfil ajeno: " + authentication.getName() + " intenta editar " + usuarioExistente.getEmail());
                throw new RuntimeException("No tienes permisos para editar este perfil");
            }

            // Actualizar campos
            if (usuarioActualizado.getNombre() != null) {
                System.out.println("📝 Actualizando nombre: " + usuarioActualizado.getNombre());
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
            }
            if (usuarioActualizado.getEmail() != null) {
                System.out.println("📝 Actualizando email: " + usuarioActualizado.getEmail());
                usuarioExistente.setEmail(usuarioActualizado.getEmail());
            }

            Usuario guardado = usuarioRepo.save(usuarioExistente);
            System.out.println("✅ Usuario guardado exitosamente");
            return guardado;

        } catch (Exception e) {
            System.out.println("❌ Error en UsuarioController: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioRepo.deleteById(id);
    }
}
