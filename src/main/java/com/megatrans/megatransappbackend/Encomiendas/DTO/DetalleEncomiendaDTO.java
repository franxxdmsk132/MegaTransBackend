package com.megatrans.megatransappbackend.Encomiendas.DTO;

import com.megatrans.megatransappbackend.Encomiendas.Entity.Producto;
import com.megatrans.megatransappbackend.Lote.DTO.LoteDTO;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class DetalleEncomiendaDTO {

    public enum EstadoEncomienda {
        RECOLECCION,RECOLECTADO,TRASLADO,BODEGA,ENTREGADO,CANCELADO
    }

    private Integer id;
    private String numGuia;
    private Usuario usuario;
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
    private String qrCodePath;
    private String ruta;
    private EstadoEncomienda estado;
    private List<ProductoDTO> productosDto;
    private LoteDTO lote;
}