package com.megatrans.megatransappbackend.Encomiendas.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoEncomiendaRespDTO {
    private String estado;
    private String fechaCreacion;
    private String username;
    private Integer idEncomienda;
    private Integer idEstadoEncomienda;
}
