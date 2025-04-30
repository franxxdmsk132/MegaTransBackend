package com.megatrans.megatransappbackend.Transporte_Mudanza.Repository;

import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.EstadoDetalleTransporte;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EstadoDetalleTransporteRepository extends CrudRepository<EstadoDetalleTransporte, Integer> {
    Optional<EstadoDetalleTransporte> findByDetalleTransporte_Id(Integer id);

}
