package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Carrera;
import com.metauni.proyecto6.model.Materia;
import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.CarreraRepository;
import com.metauni.proyecto6.repository.MateriaRepository;
import com.metauni.proyecto6.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaRepository materiaRepo;
    private final CarreraRepository carreraRepo;
    private final UsuarioRepository usuarioRepo;

    //  listar materias de una carrera del usuario logueado
    @GetMapping("/carrera/{carreraId}")
    public List<Materia> listarPorCarrera(@PathVariable Long carreraId, Principal principal) {
        Carrera carrera = carreraRepo.findById(carreraId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada"));

        // seguridad: validar dueño de la carrera
        if (!carrera.getUsuario().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta carrera");
        }

        return carrera.getMaterias();
    }

    //  crear materia en una carrera específica
    @PostMapping("/{carreraId}")
    public Materia crear(@PathVariable Long carreraId, @RequestBody Materia materia, Principal principal) {
        Carrera carrera = carreraRepo.findById(carreraId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada"));

        if (!carrera.getUsuario().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta carrera");
        }

        materia.setCarrera(carrera);
        return materiaRepo.save(materia);
    }

    // editar materia (validando dueño de la carrera)
    @PutMapping("/{id}")
    public Materia editar(@PathVariable Long id, @RequestBody Materia materia, Principal principal) {
        Materia existente = materiaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia no encontrada"));

        if (!existente.getCarrera().getUsuario().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta materia");
        }

        materia.setId(id);
        materia.setCarrera(existente.getCarrera()); //  mantener misma carrera
        return materiaRepo.save(materia);
    }

    //  eliminar materia (validando dueño)
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id, Principal principal) {
        Materia existente = materiaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia no encontrada"));

        if (!existente.getCarrera().getUsuario().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta materia");
        }

        materiaRepo.delete(existente);
    }
}
