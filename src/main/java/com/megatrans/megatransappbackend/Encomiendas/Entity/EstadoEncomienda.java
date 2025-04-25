package com.megatrans.megatransappbackend.Encomiendas.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "estado_encomienda")
public class EstadoEncomienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private Boolean estado;

    @OneToOne(targetEntity = DetalleEncomienda.class)
    private DetalleEncomienda detalleEncomienda;
}
