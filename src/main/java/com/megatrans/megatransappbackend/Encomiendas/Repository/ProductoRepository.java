package com.megatrans.megatransappbackend.Encomiendas.Repository;

import com.megatrans.megatransappbackend.Encomiendas.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
