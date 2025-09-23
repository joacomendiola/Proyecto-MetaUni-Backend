package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.model.Carrera;
import com.metauni.proyecto6.repository.CarreraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carreras")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraRepository carreraRepo;

    @GetMapping
    public List<Carrera> listar() { return carreraRepo.findAll(); }

    @PostMapping
    public Carrera crear(@RequestBody Carrera c) { return carreraRepo.save(c); }

    @PutMapping("/{id}")
    public Carrera editar(@PathVariable Long id, @RequestBody Carrera c) {
        c.setId(id);
        return carreraRepo.save(c);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { carreraRepo.deleteById(id); }
}
