package com.megatrans.megatransappbackend.Transporte_Mudanza.Repository;

import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetalleTransporteRepository extends JpaRepository<DetalleTransporte, Long> {
    Optional<DetalleTransporte> findByNumOrden(String numOrden);
}
