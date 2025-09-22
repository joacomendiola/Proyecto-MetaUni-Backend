package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Usuario editar(@PathVariable Long id, @RequestBody Usuario u) {
        u.setId(id);
        return usuarioRepo.save(u);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioRepo.deleteById(id);
    }
}
