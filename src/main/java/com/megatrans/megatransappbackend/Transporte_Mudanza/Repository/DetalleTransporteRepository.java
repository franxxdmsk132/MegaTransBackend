package com.megatrans.megatransappbackend.Transporte_Mudanza.Repository;

import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetalleTransporteRepository extends JpaRepository<DetalleTransporte, Long> {
    Optional<DetalleTransporte> findByNumOrden(String numOrden);

    @Query("SELECT d.numOrden FROM DetalleTransporte d ORDER BY d.numOrden DESC LIMIT 1")
    Optional<String> findLastNumOrden();}
