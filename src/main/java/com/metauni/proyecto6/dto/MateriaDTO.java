package com.metauni.proyecto6.dto;

import lombok.Data;

@Data
public class MateriaDTO {
    private Long id;
    private String nombre;
    private Double notaFinal;
    private Long carreraId; // Solo el ID, no el objeto completo

    public MateriaDTO() {}

    public MateriaDTO(Long id, String nombre, Double notaFinal, Long carreraId) {
        this.id = id;
        this.nombre = nombre;
        this.notaFinal = notaFinal;
        this.carreraId = carreraId;
    }
}
