package com.megatrans.megatransappbackend.Transporte_Mudanza.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "estado_transporte")
public class EstadoDetalleTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private Boolean estado;

    @OneToOne(targetEntity = DetalleTransporte.class)
    private DetalleTransporte detalleTransporte;
}
