package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.dto.CarreraDTO;
import com.metauni.proyecto6.dto.MateriaDTO;
import com.metauni.proyecto6.model.Carrera;
import com.metauni.proyecto6.model.Materia;
import com.metauni.proyecto6.repository.CarreraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carreras")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraRepository carreraRepo;

    //  POST - Crear carrera (COMPATIBLE con el frontend actual)
    @PostMapping
    public ResponseEntity<?> createCarrera(@RequestBody Carrera carrera) {
        try {
            Carrera saved = carreraRepo.save(carrera);

            // Convertir a DTO para evitar relaciones circulares
            CarreraDTO dto = new CarreraDTO(
                    saved.getId(),
                    saved.getNombre(),
                    saved.getTotalMaterias(),
                    saved.getColorBarra(),
                    saved.getUsuario() != null ? saved.getUsuario().getId() : null
            );

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear carrera: " + e.getMessage());
        }
    }

    //  GET - Todas las carreras (COMPATIBLE)
    @GetMapping
    public ResponseEntity<?> getCarreras() {
        try {
            List<CarreraDTO> carreras = carreraRepo.findAll().stream()
                    .map(c -> {
                        CarreraDTO dto = new CarreraDTO(
                                c.getId(),
                                c.getNombre(),
                                c.getTotalMaterias(),
                                c.getColorBarra(),
                                c.getUsuario() != null ? c.getUsuario().getId() : null
                        );

                        // Opcional: incluir materias como DTOs
                        if (c.getMaterias() != null) {
                            dto.setMaterias(c.getMaterias().stream()
                                    .map(m -> new MateriaDTO(
                                            m.getId(),
                                            m.getNombre(),
                                            m.getNotaFinal(),
                                            m.getCarrera() != null ? m.getCarrera().getId() : null
                                    ))
                                    .collect(Collectors.toList()));
                        }

                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(carreras);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener carreras: " + e.getMessage());
        }
    }

    //  GET - Carrera por ID (COMPATIBLE)
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarrera(@PathVariable Long id) {
        try {
            Carrera carrera = carreraRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));

            CarreraDTO dto = new CarreraDTO(
                    carrera.getId(),
                    carrera.getNombre(),
                    carrera.getTotalMaterias(),
                    carrera.getColorBarra(),
                    carrera.getUsuario() != null ? carrera.getUsuario().getId() : null
            );

            // Incluir materias si existen
            if (carrera.getMaterias() != null) {
                dto.setMaterias(carrera.getMaterias().stream()
                        .map(m -> new MateriaDTO(
                                m.getId(),
                                m.getNombre(),
                                m.getNotaFinal(),
                                m.getCarrera() != null ? m.getCarrera().getId() : null
                        ))
                        .collect(Collectors.toList()));
            }

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //  PUT - Actualizar carrera (COMPATIBLE)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarrera(@PathVariable Long id, @RequestBody Carrera carreraDetails) {
        try {
            Carrera carrera = carreraRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));

            carrera.setNombre(carreraDetails.getNombre());
            carrera.setColorBarra(carreraDetails.getColorBarra());
            carrera.setTotalMaterias(carreraDetails.getTotalMaterias());

            Carrera updated = carreraRepo.save(carrera);

            CarreraDTO dto = new CarreraDTO(
                    updated.getId(),
                    updated.getNombre(),
                    updated.getTotalMaterias(),
                    updated.getColorBarra(),
                    updated.getUsuario() != null ? updated.getUsuario().getId() : null
            );

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar carrera: " + e.getMessage());
        }
    }

    //  DELETE - Eliminar carrera (COMPATIBLE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarrera(@PathVariable Long id) {
        try {
            carreraRepo.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar carrera: " + e.getMessage());
        }
    }
}
