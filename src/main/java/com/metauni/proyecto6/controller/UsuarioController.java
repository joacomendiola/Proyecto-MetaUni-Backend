package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.dto.UsuarioDTO;
import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtener(@PathVariable Long id) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return toDTO(usuario);
    }

    @PutMapping("/{id}")
    public UsuarioDTO editar(@PathVariable Long id, @RequestBody Usuario usuarioActualizado, Principal principal) {
        System.out.println("✅ PUT /api/usuarios/" + id + " - Editando perfil");

        Usuario usuarioExistente = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Usuario solicitante = usuarioRepo.findByEmail(principal.getName());
        boolean esAdmin = solicitante != null && "ROLE_ADMIN".equals(solicitante.getRol());
        if (!usuarioExistente.getEmail().equals(principal.getName()) && !esAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para editar este usuario");
        }

        if (usuarioActualizado.getNombre() != null) {
            System.out.println("📝 Actualizando nombre: " + usuarioActualizado.getNombre());
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
        }
        if (usuarioActualizado.getEmail() != null) {
            System.out.println("📝 Actualizando email: " + usuarioActualizado.getEmail());
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
        }

        Usuario guardado = usuarioRepo.save(usuarioExistente);
        System.out.println("✅ Perfil actualizado exitosamente");
        return toDTO(guardado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id, Principal principal) {
        Usuario usuarioExistente = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Usuario solicitante = usuarioRepo.findByEmail(principal.getName());
        boolean esAdmin = solicitante != null && "ROLE_ADMIN".equals(solicitante.getRol());
        if (!usuarioExistente.getEmail().equals(principal.getName()) && !esAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este usuario");
        }

        usuarioRepo.deleteById(id);
    }

    private UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setEmail(u.getEmail());
        dto.setRol(u.getRol());
        return dto;
    }
}
