package com.megatrans.megatransappbackend.Security.repository;


import com.megatrans.megatransappbackend.Security.entity.Rol;
import com.megatrans.megatransappbackend.Security.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByRolNombre(RolNombre rolNombre);
}
