package com.megatrans.megatransappbackend.Encomiendas.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;


    private String tipo_producto;
    private Double alto;
    private Double ancho;
    private Double largo;
    private Double peso;
    private boolean fragil;
}
