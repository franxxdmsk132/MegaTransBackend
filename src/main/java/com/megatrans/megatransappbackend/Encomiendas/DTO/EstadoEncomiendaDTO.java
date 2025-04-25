package com.megatrans.megatransappbackend.Encomiendas.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EstadoEncomiendaDTO {
    private Integer id;
    private Integer encomienda;
    private Boolean estado;
    private String username;
    private String email;
    private String fechaCreacion;

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // <-- Change this to your preferred logging method
            return "{}";
        }
    }
}
