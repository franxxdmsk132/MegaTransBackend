package com.megatrans.megatransappbackend.Encomiendas.Repository;

import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleEncomiendaRepository extends JpaRepository<DetalleEncomienda, Integer> {

    @Query("SELECT d.numGuia FROM DetalleEncomienda d ORDER BY d.numGuia DESC LIMIT 1")
    Optional<String> findLastNumGuia();
    List<DetalleEncomienda> findByEstadoAndLoteIsNull(String estado);
    Optional<DetalleEncomienda> findByNumGuia(String numGuia);

    List<DetalleEncomienda> findByUsuario(Usuario usuario);
    List<DetalleEncomienda>findByEstado(String estado);
    List<DetalleEncomienda>findByLote(Lote lote);

    List<DetalleEncomienda> findByLoteId(Integer loteId);
}
