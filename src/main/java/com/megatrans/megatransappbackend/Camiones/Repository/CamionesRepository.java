package com.megatrans.megatransappbackend.Camiones.Repository;

import com.megatrans.megatransappbackend.Camiones.Entity.Camiones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CamionesRepository extends JpaRepository <Camiones, Integer> {
    Optional<Camiones> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
}
