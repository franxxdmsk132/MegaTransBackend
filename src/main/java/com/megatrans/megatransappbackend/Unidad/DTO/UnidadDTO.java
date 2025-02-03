package com.megatrans.megatransappbackend.Unidad.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class UnidadDTO {

    private MultipartFile imagenUrl;
    @Min(0)
    private Double altura;
    @Min(0)
    private Double largo;
    @Min(0)
    private Double ancho;
    @NotBlank
    private String tipo;
    @NotBlank
    private String tipo_cajon;

    public UnidadDTO() {
    }

    public UnidadDTO(MultipartFile imagenUrl, Double altura, Double largo, Double ancho, String tipo, String tipo_cajon) {
        this.imagenUrl = imagenUrl;
        this.altura = altura;
        this.largo = largo;
        this.ancho = ancho;
        this.tipo = tipo;
        this.tipo_cajon = tipo_cajon;
    }
}
