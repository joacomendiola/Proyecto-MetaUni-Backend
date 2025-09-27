package com.metauni.proyecto6.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore // ← EVITA REFERENCIA A CARRERA
    private Carrera carrera;
}
