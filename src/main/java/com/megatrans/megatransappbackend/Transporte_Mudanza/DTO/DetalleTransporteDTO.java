package com.megatrans.megatransappbackend.Transporte_Mudanza.DTO;

import com.megatrans.megatransappbackend.Security.entity.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DetalleTransporteDTO {


    public enum EstadoTransporte {
        PENDIENTE, PROCESANDO, MOVIMIENTO, FINALIZADO, CANCELADO
    }

    public enum Pago {
        EFECTIVO, TRANSFERENCIA
    }
    public enum TipoServicio{
        MERCADERIA,DISTRIBUCION,MUDANZA
    }

    private Integer cantidadEstibaje;
    private String descripcionProducto;
    private EstadoTransporte estado;
    private TipoServicio tipoServicio;
    private Boolean estibaje;
    private LocalDate fecha;
    private String numOrden;
    private Pago pago;
    private DireccionDTO direccionOrigen;  // DireccionOrigen como DTO
    private DireccionDTO direccionDestino; // DireccionDestino como DTO

    private Integer unidadId;
    private Usuario usuarioId;
}
