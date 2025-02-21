package com.megatrans.megatransappbackend.Encomiendas.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tipoProducto;
    private Double alto;
    private Double ancho;
    private Double largo;
    private Double peso;
    private boolean fragil;

    @ManyToOne
    @JoinColumn(name = "detalle-encomienda_id")
    private DetalleEncomienda detalleEncomienda;
}