package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Carrera;
import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.CarreraRepository;
import com.metauni.proyecto6.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/carreras") //  mismo prefijo que materias
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraRepository carreraRepo;
    private final UsuarioRepository usuarioRepo;

    //  crear carrera asociada al usuario logueado
    @PostMapping
    public Carrera crear(@RequestBody Carrera carrera, Principal principal) {
        Usuario usuario = usuarioRepo.findByEmail(principal.getName());
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
        }

        carrera.setUsuario(usuario);
        if (carrera.getTotalMaterias() == null) {
            carrera.setTotalMaterias(0);
        }
        if (carrera.getColorBarra() == null) {
            carrera.setColorBarra("#6366f1");
        }
        if (carrera.getMaterias() == null) {
            carrera.setMaterias(new ArrayList<>()); //  evita null
        }

        return carreraRepo.save(carrera);
    }

    //  listar carreras del usuario logueado
    @GetMapping
    public List<Carrera> listar(Principal principal) {
        Usuario usuario = usuarioRepo.findByEmail(principal.getName());
        return carreraRepo.findByUsuario(usuario);
    }

    //  obtener carrera por id (validando usuario)
    @GetMapping("/{id}")
    public Carrera obtener(@PathVariable Long id, Principal principal) {
        Carrera carrera = carreraRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada"));

        if (!carrera.getUsuario().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta carrera");
        }

        return carrera;
    }

    //  editar carrera (ej: cambiar nombre, colorBarra)
    @PutMapping("/{id}")
    public Carrera editar(@PathVariable Long id, @RequestBody Carrera cambios, Principal principal) {
        Carrera existente = carreraRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada"));

        if (!existente.getUsuario().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta carrera");
        }

        // aplicar cambios permitidos
        existente.setNombre(cambios.getNombre());
        existente.setColorBarra(cambios.getColorBarra());
        existente.setTotalMaterias(cambios.getTotalMaterias());

        return carreraRepo.save(existente);
    }

    //  eliminar carrera (y sus materias por cascade)
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id, Principal principal) {
        Carrera carrera = carreraRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada"));

        if (!carrera.getUsuario().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta carrera");
        }

        carreraRepo.delete(carrera);
    }
}
