package com.megatrans.megatransappbackend.Encomiendas.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProductoDTO {
    private Integer id;
    private String tipoProducto;
    private Double alto;
    private Double ancho;
    private Double largo;
    private Double peso;
    private boolean fragil;

}

