package com.megatrans.megatransappbackend.Pedido.Repository;

import com.megatrans.megatransappbackend.Pedido.Entity.Pedidos;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Integer> {
}
