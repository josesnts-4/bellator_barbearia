package com.bellator.bellator_barbearia.repository;

import com.bellator.bellator_barbearia.model.Role;
import com.bellator.bellator_barbearia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByRole(Role role);
}
