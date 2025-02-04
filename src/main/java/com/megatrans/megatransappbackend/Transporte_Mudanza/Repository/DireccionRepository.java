package com.megatrans.megatransappbackend.Transporte_Mudanza.Repository;

import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
}
