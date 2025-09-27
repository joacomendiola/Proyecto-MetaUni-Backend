package com.metauni.proyecto6.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer totalMaterias;
    private String colorBarra;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore // ← EVITA REFERENCIA A USUARIO
    private Usuario usuario;

    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // ← EVITA REFERENCIA A MATERIAS
    private List<Materia> materias;
}
