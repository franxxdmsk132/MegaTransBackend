package com.megatrans.megatransappbackend.Pedido.Entity;

import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Pedidos {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Usuario usuario;
    @ManyToOne
    private Unidad unidad;
    @Column(nullable = false)
    private double latitud;

    @Column(nullable = false)
    private double longitud;

    private String direccion;
    private LocalDateTime fecha_pededo;
    private String estado;

}
