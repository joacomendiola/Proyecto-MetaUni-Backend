package com.metauni.proyecto6.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String rol;

    // Sin password por seguridad
}
