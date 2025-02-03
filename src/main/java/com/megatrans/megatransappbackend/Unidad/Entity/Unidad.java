package com.megatrans.megatransappbackend.Unidad.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Unidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imagenUrl;
    private Double altura;
    private Double largo;
    private Double ancho;
    private String tipo;
    private String tipo_cajon;


    public Unidad(String imagenUrl, Double altura, Double largo, Double ancho, String tipo, String tipo_cajon) {
        this.imagenUrl = imagenUrl;
        this.altura = altura;
        this.largo = largo;
        this.ancho = ancho;
        this.tipo = tipo;
        this.tipo_cajon = tipo_cajon;
    }
}
