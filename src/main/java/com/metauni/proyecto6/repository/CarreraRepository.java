package com.metauni.proyecto6.repository;

import com.metauni.proyecto6.model.Carrera;
import com.metauni.proyecto6.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    //  obtener carreras por usuario
    List<Carrera> findByUsuario(Usuario usuario);
}