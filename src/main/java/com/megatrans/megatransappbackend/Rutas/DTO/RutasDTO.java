package com.megatrans.megatransappbackend.Rutas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutasDTO {

    private Integer id;
    private String origen;
    private String destino;

}
