package com.megatrans.megatransappbackend.Transporte_Mudanza.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DireccionDTO {
    private Long id;
    private String callePrincipal;
    private String calleSecundaria;
    private String barrio;
    private String ciudad;
    private String referencia;
    private String telefono;
    private Double latitud;
    private Double longitud;

}
