package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Materia;
import com.metauni.proyecto6.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaRepository materiaRepo;

    @GetMapping
    public List<Materia> listar() {
        return materiaRepo.findAll();
    }

    @PostMapping
    public Materia crear(@RequestBody Materia m) {
        return materiaRepo.save(m);
    }

    @PutMapping("/{id}")
    public Materia editar(@PathVariable Long id, @RequestBody Materia m) {
        m.setId(id);
        return materiaRepo.save(m);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        materiaRepo.deleteById(id);
    }
}