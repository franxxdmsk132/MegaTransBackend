package com.megatrans.megatransappbackend.Transporte_Mudanza.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class DetalleTransporteDTO {


    public enum EstadoTransporte {
        PENDIENTE, PROCESANDO, MOVIMIENTO, FINALIZADO
    }

    public enum Pago {
        EFECTIVO, TRANSFERENCIA
    }
    public enum TipoServicio{
        MERCADERIA,DISTRIBUCION,MUDANZA
    }

    private int cantidadEstibaje;
    private String descripcionProducto;
    private EstadoTransporte estado;
    private TipoServicio tipoServicio;
    private Boolean estibaje;
    private LocalDate fecha;
    private String numOrden;
    private Pago pago;
    private DireccionDTO direccionOrigen;  // DireccionOrigen como DTO
    private DireccionDTO direccionDestino; // DireccionDestino como DTO

    private int unidadId;
    private int usuarioId;
}
