package com.metauni.proyecto6.repository;


import com.metauni.proyecto6.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Metodo existente
    Usuario findByEmail(String email);

    // Metodo con log
    default Usuario findByIdWithLog(Long id) {
        System.out.println("🔍 Buscando usuario con ID: " + id);
        return findById(id).orElse(null);
    }
}
