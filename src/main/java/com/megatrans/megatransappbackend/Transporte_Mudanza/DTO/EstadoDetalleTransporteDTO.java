package com.megatrans.megatransappbackend.Transporte_Mudanza.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EstadoDetalleTransporteDTO {
    private Integer id;
    private Integer transporte;
    private Boolean estado;
    private String numOrden;
    private String pago;
    private String username;
    private String email;
    private String tipoServicio;
    private String tipo_unidad;
    private String telf;
    private String fechaCreacion;
    private Integer cantidad_estibaje;
    private Double latitudO;
    private Double longitudO;
    private Double latitudD;
    private Double longitudD;

    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return "{}";
        }
    }

}
