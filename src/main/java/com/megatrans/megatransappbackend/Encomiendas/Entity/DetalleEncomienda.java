package com.megatrans.megatransappbackend.Encomiendas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate fecha;

    private String dirRemitente;
    private Double latitudOrg;
    private Double longitudOrg;
    private String nombreD;
    private String apellidoD;
    private String identificacionD;
    private String telfBeneficiario;
    private String telfEncargado;
    private String correoD;
    private String referenciaD;
    private String dirDestino;
    private Double latitudDestino;
    private Double longitudDestino;
    private String tipoEntrega;
    private String ruta;
    private String estado;

    @OneToMany(mappedBy = "detalleEncomienda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();
    @Column(name = "qr_code_path")
    private String qrCodePath;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lote lote;
}

