package com.megatrans.megatransappbackend.Encomiendas.Entity;

import com.megatrans.megatransappbackend.Security.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Entity
@Table(name ="detalle_encomienda")
public class DetalleEncomienda {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (unique = true, nullable = false)
    private String numGuia;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @CreationTimestamp // Se generará automáticamente
    @Column(updatable = false)
    private LocalDate fecha;

    private String dir_remitente;
    private String nombre_d;
    private String apellido_d;
    private String identificacion_d;
    private String telf_beneficiario;
    private String telf_encargado;
    private String correo_d;
    private String referencia_d;
    private String tipo_entrega;
    private String ruta;




}
