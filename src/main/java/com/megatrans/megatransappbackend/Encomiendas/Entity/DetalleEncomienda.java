package com.megatrans.megatransappbackend.Encomiendas.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name ="detalle_encomienda")
public class DetalleEncomienda {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (unique = true, nullable = false)
    private String numGuia;




}
