package com.metauni.proyecto6.dto;

import lombok.Data;
import java.util.List;

@Data
public class CarreraDTO {
    private Long id;
    private String nombre;
    private Integer totalMaterias;
    private String colorBarra;
    private Long usuarioId; // Solo el ID del usuario
    private List<MateriaDTO> materias; // Lista de DTOs, no entidades

    public CarreraDTO() {}

    public CarreraDTO(Long id, String nombre, Integer totalMaterias, String colorBarra, Long usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.totalMaterias = totalMaterias;
        this.colorBarra = colorBarra;
        this.usuarioId = usuarioId;
    }
}
