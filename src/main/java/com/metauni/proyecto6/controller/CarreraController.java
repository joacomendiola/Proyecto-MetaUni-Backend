package com.metauni.proyecto6.controller;

import com.metauni.proyecto6.dto.CarreraDTO;
import com.metauni.proyecto6.dto.MateriaDTO;
import com.metauni.proyecto6.model.Carrera;
import com.metauni.proyecto6.model.Materia;
import com.metauni.proyecto6.model.Usuario;
import com.metauni.proyecto6.repository.CarreraRepository;
import com.metauni.proyecto6.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carreras")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraRepository carreraRepo;
    private final UsuarioRepository usuarioRepo;

    //  POST - Crear carrera (asigna usuario desde token)
    @PostMapping
    public ResponseEntity<?> createCarrera(@RequestBody Carrera carrera, Principal principal) {
        try {
            Usuario usuario = usuarioRepo.findByEmail(principal.getName());
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }
            carrera.setUsuario(usuario);
            Carrera saved = carreraRepo.save(carrera);

            CarreraDTO dto = new CarreraDTO(
                    saved.getId(),
                    saved.getNombre(),
                    saved.getTotalMaterias(),
                    saved.getColorBarra(),
                    saved.getUsuario().getId()
            );

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear carrera: " + e.getMessage());
        }
    }

    //  GET - Todas las carreras del usuario autenticado
    @GetMapping
    public ResponseEntity<?> getCarreras(Principal principal) {
        try {
            Usuario usuario = usuarioRepo.findByEmail(principal.getName());
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }
            List<CarreraDTO> carreras = carreraRepo.findByUsuario(usuario).stream()
                    .map(c -> {
                        CarreraDTO dto = new CarreraDTO(
                                c.getId(),
                                c.getNombre(),
                                c.getTotalMaterias(),
                                c.getColorBarra(),
                                c.getUsuario() != null ? c.getUsuario().getId() : null
                        );

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

    //  GET - Carrera por ID (validando dueño)
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarrera(@PathVariable Long id, Principal principal) {
        try {
            Carrera carrera = carreraRepo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada"));

            if (carrera.getUsuario() == null || !carrera.getUsuario().getEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta carrera");
            }

            CarreraDTO dto = new CarreraDTO(
                    carrera.getId(),
                    carrera.getNombre(),
                    carrera.getTotalMaterias(),
                    carrera.getColorBarra(),
                    carrera.getUsuario() != null ? carrera.getUsuario().getId() : null
            );

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
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //  PUT - Actualizar carrera (validando dueño)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarrera(@PathVariable Long id, @RequestBody Carrera carreraDetails, Principal principal) {
        try {
            Carrera carrera = carreraRepo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada"));

            if (carrera.getUsuario() == null || !carrera.getUsuario().getEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta carrera");
            }

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
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar carrera: " + e.getMessage());
        }
    }

    // DELETE - Eliminar carrera (validando dueño)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarrera(@PathVariable Long id, Principal principal) {
        try {
            System.out.println("🎯 DELETE /api/carreras/" + id);

            Carrera carrera = carreraRepo.findById(id).orElse(null);
            if (carrera == null) {
                System.out.println("❌ Carrera no encontrada: " + id);
                return ResponseEntity.notFound().build();
            }

            if (carrera.getUsuario() == null || !carrera.getUsuario().getEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta carrera");
            }

            carreraRepo.delete(carrera);
            System.out.println("✅ Carrera eliminada: " + id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("❌ Error eliminando carrera: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error al eliminar carrera");
        }
    }
}

