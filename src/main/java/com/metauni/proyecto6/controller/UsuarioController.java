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
@RequestMapping("/usuarios")
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
    public ResponseEntity<?> editar(@PathVariable Long id,
                                    @RequestBody Usuario usuarioActualizado,
                                    Authentication authentication) {

        System.out.println("🔍 PUT /usuarios/" + id);
        System.out.println("🔍 Usuario autenticado: " + authentication.getName());

        try {
            // 1. Buscar usuario existente
            Usuario usuarioExistente = usuarioRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 2. VERIFICAR que el usuario autenticado edite solo su perfil
            if (!usuarioExistente.getEmail().equals(authentication.getName())) {
                System.out.println("❌ No autorizado: " + authentication.getName() + " intenta editar " + usuarioExistente.getEmail());
                return ResponseEntity.status(403).body("No autorizado");
            }

            // 3. Verificar si el email cambió
            boolean emailCambiado = false;
            String emailViejo = usuarioExistente.getEmail();

            if (usuarioActualizado.getNombre() != null) {
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
            }

            if (usuarioActualizado.getEmail() != null &&
                    !usuarioActualizado.getEmail().equals(emailViejo)) {
                usuarioExistente.setEmail(usuarioActualizado.getEmail());
                emailCambiado = true;
            }

            // 4. Guardar cambios
            Usuario usuarioGuardado = usuarioRepo.save(usuarioExistente);

            // 5. Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("usuario", usuarioGuardado);

            // 6. Si cambió el email, avisar al frontend que necesita nuevo token
            if (emailCambiado) {
                response.put("emailCambiado", true);
                response.put("mensaje", "Email actualizado - debes volver a login");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error interno");
        }
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioRepo.deleteById(id);
    }
}
