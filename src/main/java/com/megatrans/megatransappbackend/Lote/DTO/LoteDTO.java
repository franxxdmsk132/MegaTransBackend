package com.megatrans.megatransappbackend.Lote.DTO;

import com.megatrans.megatransappbackend.Encomiendas.DTO.DetalleEncomiendaDTO;
import com.megatrans.megatransappbackend.Unidad.DTO.UnidadDTO;
import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
public class LoteDTO {

    private Integer id;
    private String numLote;
    private LocalDate fecha;
    private String estado;
    private Integer unidad; // ID de la unidad
    private List<Integer> encomiendaIds; // ✅ Cambiado a lista de IDs
    private List<String> numerosGuia; // Lista de números de guía de las encomiendas
    private String ruta;
}
