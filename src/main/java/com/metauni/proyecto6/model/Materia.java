package com.metauni.proyecto6.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double notaFinal;

    @ManyToOne
    @JoinColumn(name = "carrera_id")
    private Carrera carrera;
}
