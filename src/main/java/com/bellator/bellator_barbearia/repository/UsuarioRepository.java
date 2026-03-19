package com.bellator.bellator_barbearia.repository;

import com.bellator.bellator_barbearia.role.Role;
import com.bellator.bellator_barbearia.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuarios> findByRole(Role role);
}
