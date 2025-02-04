package com.megatrans.megatransappbackend.Transporte_Mudanza.Entity;

import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "detalle_transporte")
public class DetalleTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numOrden;

    @ManyToOne
    @JoinColumn(name = "unidad_id")
    private Unidad unidad;

    private String estado;

    private Date fecha;

    private Boolean estibaje;

    private Integer cantidadEstibaje;

    @ManyToOne
    @JoinColumn(name = "id_dir_origen")
    private Direccion dirOrigen;

    @ManyToOne
    @JoinColumn(name = "id_dir_destino")
    private Direccion dirDestino;

    private String descripcionProducto;

    private String pago;
    private String tipoServicio;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
