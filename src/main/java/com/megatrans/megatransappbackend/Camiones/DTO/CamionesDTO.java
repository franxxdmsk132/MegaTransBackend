package com.megatrans.megatransappbackend.Camiones.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class CamionesDTO {

    @NotBlank
    private String tipo;
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @NotBlank
    private String placa;
    @Min(0)
    private Double capacidad;
    @NotBlank
    private String tipo_cajon;
    @Min(0)
    private Double altura;
    @Min(0)
    private Double ancho;
    @Min(0)
    private Double largo;
    private MultipartFile imagenUrl;
    @NotBlank
    private Boolean estado;

    public CamionesDTO() {
    }

    public CamionesDTO(String tipo, String marca, String modelo, String placa, Double capacidad, String tipo_cajon, Double altura, Double ancho, Double largo, MultipartFile imagenUrl, Boolean estado) {
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
