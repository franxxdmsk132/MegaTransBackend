package com.megatrans.megatransappbackend.Security.repository;


import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
    List<Usuario> findByRoles_RolNombre(RolNombre rolNombre);
}
