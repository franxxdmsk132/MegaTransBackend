package com.megatrans.megatransappbackend.Unidad.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UnidadDTO {
    private Integer id;
    private String imagenUrl;
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

    public UnidadDTO(String imagenUrl, Double altura, Double largo, Double ancho, String tipo, String tipo_cajon) {
        this.imagenUrl = imagenUrl;
        this.altura = altura;
        this.largo = largo;
        this.ancho = ancho;
        this.tipo = tipo;
        this.tipo_cajon = tipo_cajon;
    }
}
