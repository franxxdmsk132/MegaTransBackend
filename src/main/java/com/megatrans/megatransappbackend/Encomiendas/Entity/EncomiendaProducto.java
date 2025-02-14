package com.megatrans.megatransappbackend.Encomiendas.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "encomienda_producto")
public class EncomiendaProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_detalle_encomienda", nullable = false)
    private DetalleEncomienda detalleEncomienda;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

//    @Column(nullable = false)
//    private Integer cantidad;
}