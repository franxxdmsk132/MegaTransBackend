package com.megatrans.megatransappbackend.Lote.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
@Entity
@Table(name = "lote")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String numLote;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate fecha;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_unidad")
    private Unidad unidad;

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleEncomienda> detalleEncomiendas = new ArrayList<>();

    private String ruta;
}

