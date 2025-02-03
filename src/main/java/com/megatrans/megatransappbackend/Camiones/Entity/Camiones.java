package com.megatrans.megatransappbackend.Camiones.Entity;

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
public class Camiones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tipo;
    private String marca;
    private String modelo;
    private String placa;
    private Double capacidad;
    private String tipo_cajon;
    private Double altura;
    private Double ancho;
    private Double largo;
    private String imagenUrl; // Campo para la URL de la imagen
    private Boolean estado;

    public Camiones(String tipo, String marca, String modelo, String placa, Double capacidad, String tipo_cajon, Double altura, Double ancho, Double largo, String imagenUrl, Boolean estado) {
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.capacidad = capacidad;
        this.tipo_cajon = tipo_cajon;
        this.altura = altura;
        this.ancho = ancho;
        this.largo = largo;
        this.imagenUrl = imagenUrl;
        this.estado = estado;
    }

}
