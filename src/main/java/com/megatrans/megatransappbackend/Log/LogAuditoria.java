package com.megatrans.megatransappbackend.Log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_auditoria")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
    private String nombre;
    private String apellido;
    private String accion;
    private String url;
    private LocalDateTime fecha;

    public LogAuditoria(String usuario, String nombre, String apellido, String accion, String url, LocalDateTime fecha) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.accion = accion;
        this.url = url;
        this.fecha = fecha;
    }

    public LogAuditoria(String accion, String url, LocalDateTime fecha) {
        this.accion = accion;
        this.url = url;
        this.fecha = fecha;
    }
}