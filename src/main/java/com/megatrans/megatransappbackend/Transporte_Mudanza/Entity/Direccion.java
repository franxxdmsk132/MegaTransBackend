package com.megatrans.megatransappbackend.Transporte_Mudanza.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "direccion")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calle_principal", nullable = false)
    private String callePrincipal;

    @Column(name = "calle_secundaria")
    private String calleSecundaria;

    @Column(nullable = false)
    private String barrio;

    @Column(nullable = false)
    private String ciudad;

    private String referencia;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;


}
