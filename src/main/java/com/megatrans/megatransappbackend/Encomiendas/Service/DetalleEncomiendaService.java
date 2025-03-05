package com.megatrans.megatransappbackend.Encomiendas.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.megatrans.megatransappbackend.Encomiendas.DTO.DetalleEncomiendaDTO;
import com.megatrans.megatransappbackend.Encomiendas.DTO.ProductoDTO;
import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Encomiendas.Entity.Producto;
import com.megatrans.megatransappbackend.Encomiendas.Repository.DetalleEncomiendaRepository;
import com.megatrans.megatransappbackend.Encomiendas.Repository.ProductoRepository;
import com.megatrans.megatransappbackend.Lote.DTO.LoteDTO;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.repository.UsuarioRepository;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetalleEncomiendaService {

    @Autowired
    private DetalleEncomiendaRepository detalleEncomiendaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;



    // Crear una nueva encomienda con productos
    @Transactional
    public DetalleEncomiendaDTO crearEncomienda(DetalleEncomiendaDTO detalleEncomiendaDTO) {
        // Obtener el usuario de la sesión
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear la entidad DetalleEncomienda
        DetalleEncomienda detalleEncomienda = new DetalleEncomienda();
        detalleEncomienda.setNumGuia(generarNuevoNumGuia());
        detalleEncomienda.setFecha(detalleEncomiendaDTO.getFecha());
        detalleEncomienda.setDirRemitente(detalleEncomiendaDTO.getDirRemitente());
        detalleEncomienda.setLatitudOrg(detalleEncomiendaDTO.getLatitudOrg());
        detalleEncomienda.setLongitudOrg(detalleEncomiendaDTO.getLongitudOrg());
        detalleEncomienda.setNombreD(detalleEncomiendaDTO.getNombreD());
        detalleEncomienda.setApellidoD(detalleEncomiendaDTO.getApellidoD());
        detalleEncomienda.setIdentificacionD(detalleEncomiendaDTO.getIdentificacionD());
        detalleEncomienda.setTelfBeneficiario(detalleEncomiendaDTO.getTelfBeneficiario());
        detalleEncomienda.setTelfEncargado(detalleEncomiendaDTO.getTelfEncargado());
        detalleEncomienda.setCorreoD(detalleEncomiendaDTO.getCorreoD());
        detalleEncomienda.setDirDestino(detalleEncomiendaDTO.getDirDestino());
        detalleEncomienda.setLatitudDestino(detalleEncomiendaDTO.getLatitudDestino());
        detalleEncomienda.setLongitudDestino(detalleEncomiendaDTO.getLongitudDestino());
        detalleEncomienda.setReferenciaD(detalleEncomiendaDTO.getReferenciaD());
        detalleEncomienda.setTipoEntrega(detalleEncomiendaDTO.getTipoEntrega());
        detalleEncomienda.setRuta(detalleEncomiendaDTO.getRuta());
        detalleEncomienda.setEstado(detalleEncomiendaDTO.getEstado().name());
        // Asignar el usuario a la encomienda
        detalleEncomienda.setUsuario(usuario);

        // Crear los productos y asociarlos a la encomienda
        List<Producto> productos = detalleEncomiendaDTO.getProductosDto().stream()
                .map(productoDTO -> {
                    Producto producto = new Producto();
                    producto.setTipoProducto(productoDTO.getTipoProducto());
                    producto.setAlto(productoDTO.getAlto());
                    producto.setAncho(productoDTO.getAncho());
                    producto.setLargo(productoDTO.getLargo());
                    producto.setPeso(productoDTO.getPeso());
                    producto.setFragil(productoDTO.isFragil());
                    producto.setDetalleEncomienda(detalleEncomienda);  // Asociar el producto con la encomienda
                    return producto;
                })
                .collect(Collectors.toList());

        // Guardar los productos
        productoRepository.saveAll(productos);
        // Guardar la encomienda en el repositorio
        detalleEncomiendaRepository.save(detalleEncomienda);
// Generar el código QR basado en el número de guía o cualquier dato relevante
        String contenidoQR = detalleEncomienda.getNumGuia();  // O cualquier otro dato relevante
        try {
            BufferedImage qrCode = generarQRCode(contenidoQR);

            // Guardar el QR en la ruta deseada
            String qrCodeUrl = guardarQRCode(qrCode, detalleEncomienda.getNumGuia());
            detalleEncomienda.setQrCodePath(qrCodeUrl);

            System.out.println("Código QR guardado en: " + qrCodeUrl);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al generar o guardar el código QR: " + e.getMessage());
        }




        // Mapear y devolver el DTO con los datos de la encomienda creada
        return convertirADetalleEncomiendaDTO(detalleEncomienda);
    }

    public List<DetalleEncomiendaDTO> obtenerEncomiendasPendientes() {
        List<DetalleEncomienda> encomiendasPendientes = detalleEncomiendaRepository.findByEstado("RECOLECCION");

        return encomiendasPendientes.stream()
                .map(this::convertirADetalleEncomiendaDTO)
                .collect(Collectors.toList());
    }
    public List<DetalleEncomiendaDTO> obtenerEncomiendasRecolectadas() {
        List<DetalleEncomienda> encomiendasPendientes = detalleEncomiendaRepository.findByEstado("RECOLECTADO");

        return encomiendasPendientes.stream()
                .map(this::convertirADetalleEncomiendaDTO)
                .collect(Collectors.toList());
    }

    // Obtener todos los detalles de encomienda con sus productos
    public List<DetalleEncomiendaDTO> getAllDetalleEncomiendas() {
        List<DetalleEncomienda> detallesEncomienda = detalleEncomiendaRepository.findAll();
        return detallesEncomienda.stream()
                .map(this::convertirADetalleEncomiendaDTO)
                .collect(Collectors.toList());
    }
    // Obtener detalle de encomienda por ID
    public DetalleEncomiendaDTO getDetalleEncomiendaById(Integer id) {
        // Buscar el detalle de la encomienda por ID
        DetalleEncomienda detalleEncomienda = detalleEncomiendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encomienda no encontrada"));

        // Convertir y devolver el DTO correspondiente
        return convertirADetalleEncomiendaDTO(detalleEncomienda);
    }
    // Obtener detalle de encomienda por numGuia
    public DetalleEncomiendaDTO getDetalleEncomiendaByNumGuia(String numGuia) {
        // Buscar el detalle de la encomienda por numGuia
        DetalleEncomienda detalleEncomienda = detalleEncomiendaRepository.findByNumGuia(numGuia)
                .orElseThrow(() -> new RuntimeException("Encomienda con #Guia " + numGuia + " no encontrada"));

        // Convertir y devolver el DTO correspondiente
        return convertirADetalleEncomiendaDTO(detalleEncomienda);
    }

    // Actualizar el estado de una encomienda por numGuia
    @Transactional
    public DetalleEncomiendaDTO actualizarEstadoPorNumGuia(String numGuia, DetalleEncomiendaDTO.EstadoEncomienda nuevoEstado) {
        // Buscar el detalle de la encomienda por numGuia
        DetalleEncomienda detalleEncomienda = detalleEncomiendaRepository.findByNumGuia(numGuia)
                .orElseThrow(() -> new RuntimeException("Encomienda con #Guia " + numGuia + " no encontrada"));

        // Actualizar el estado
        detalleEncomienda.setEstado(nuevoEstado.name());

        // Guardar la encomienda con el estado actualizado
        detalleEncomiendaRepository.save(detalleEncomienda);

        // Convertir y devolver el DTO actualizado
        return convertirADetalleEncomiendaDTO(detalleEncomienda);
    }

    // Convertir DetalleEncomienda a DTO
    private DetalleEncomiendaDTO convertirADetalleEncomiendaDTO(DetalleEncomienda detalleEncomienda) {
        DetalleEncomiendaDTO dto = new DetalleEncomiendaDTO();
        dto.setId(detalleEncomienda.getId());
        dto.setNumGuia(detalleEncomienda.getNumGuia());
        dto.setUsuario(detalleEncomienda.getUsuario());
        dto.setDirRemitente(detalleEncomienda.getDirRemitente());
        dto.setLongitudOrg(detalleEncomienda.getLongitudOrg());
        dto.setLatitudOrg(detalleEncomienda.getLatitudOrg());
        dto.setNombreD(detalleEncomienda.getNombreD());
        dto.setApellidoD(detalleEncomienda.getApellidoD());
        dto.setIdentificacionD(detalleEncomienda.getIdentificacionD());
        dto.setTelfBeneficiario(detalleEncomienda.getTelfBeneficiario());
        dto.setTelfEncargado(detalleEncomienda.getTelfEncargado());
        dto.setCorreoD(detalleEncomienda.getCorreoD());
        dto.setReferenciaD(detalleEncomienda.getReferenciaD());
        dto.setDirDestino(detalleEncomienda.getDirDestino());
        dto.setLatitudDestino(detalleEncomienda.getLatitudDestino());
        dto.setLongitudDestino(detalleEncomienda.getLongitudDestino());
        dto.setTipoEntrega(detalleEncomienda.getTipoEntrega());
        dto.setRuta(detalleEncomienda.getRuta());
        dto.setEstado(DetalleEncomiendaDTO.EstadoEncomienda.valueOf(detalleEncomienda.getEstado()));
        dto.setFecha(detalleEncomienda.getFecha());
        dto.setQrCodePath(detalleEncomienda.getQrCodePath());
        // Verifica que lote no sea null y convierte a LoteDTO
        if (detalleEncomienda.getLote() != null) {
            LoteDTO loteDTO = convertirALoteDTO(detalleEncomienda.getLote()); // Convierte Lote a LoteDTO
            dto.setLote(loteDTO); // Establece el objeto LoteDTO en el DTO de Encomienda
        }

        // Convertir la lista de productos a DTOs
        List<ProductoDTO> productosDTO = detalleEncomienda.getProductos().stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());
        dto.setProductosDto(productosDTO);

        return dto;
    }

    // Convertir Producto a ProductoDTO
    private ProductoDTO convertirAProductoDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setTipoProducto(producto.getTipoProducto());
        dto.setAlto(producto.getAlto());
        dto.setAncho(producto.getAncho());
        dto.setLargo(producto.getLargo());
        dto.setPeso(producto.getPeso());
        dto.setFragil(producto.isFragil());
        return dto;
    }
    private LoteDTO convertirALoteDTO(Lote lote) {
        LoteDTO loteDTO = new LoteDTO();
        loteDTO.setNumLote(lote.getNumLote()); // Asigna el numLote al DTO
        loteDTO.setId(lote.getId());
        loteDTO.setFecha(lote.getFecha());
        loteDTO.setEstado(lote.getEstado());
        loteDTO.setIdUnidad(lote.getUnidad().getId());
        loteDTO.setRuta(lote.getRuta());
        return loteDTO;
    }


    public String generarNuevoNumGuia() {
        Optional<String> lastNumGuia = detalleEncomiendaRepository.findLastNumGuia();

        if (lastNumGuia.isPresent()) {
            // Extraer el número actual y aumentar en 1
            String lastGuia = lastNumGuia.get();
            int lastNumber = Integer.parseInt(lastGuia.substring(2)); // Extrae el número ignorando 'TM'
            int nextNumber = lastNumber + 1;

            // Formatear con ceros a la izquierda (mínimo 5 dígitos)
            return String.format("EM%05d", nextNumber);
        } else {
            // Si no hay registros, comenzar con TM00001
            return "EM00001";
        }
    }
    public boolean actualizarEstado(Integer id, String nuevoEstado) {
        Optional<DetalleEncomienda> optionalDetalle = detalleEncomiendaRepository.findById(id);

        if (optionalDetalle.isPresent()) {
            DetalleEncomienda detalle = optionalDetalle.get();
            detalle.setEstado(nuevoEstado);
            detalleEncomiendaRepository.save(detalle);
            return true;
        } else {
            return false;
        }
    }
    public static BufferedImage generarQRCode(String contenido) throws Exception {
        int ancho = 300;
        int alto = 300;

        // Configurar los parámetros del QR (puedes ajustar esto según tus necesidades)
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.MARGIN, 1);  // Margen alrededor del código QR

        // Crear el QR a partir del contenido
        BitMatrix matrix = new MultiFormatWriter().encode(contenido, BarcodeFormat.QR_CODE, ancho, alto, hints);

        // Convertir el BitMatrix en una imagen de tipo BufferedImage
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                imagen.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);  // Negro o blanco
            }
        }

        return imagen;
    }
    private String guardarQRCode(BufferedImage qrCode, String numGuia) {
        try {
            // Generar nombre único para el archivo QR
            String nombreArchivo = numGuia + "_qr.png";

            // Definir la ruta donde se guardará el código QR
            Path rutaArchivo = Paths.get("qr_codes").resolve(nombreArchivo).toAbsolutePath();

            // Crear el directorio si no existe
            if (!Files.exists(rutaArchivo.getParent())) {
                Files.createDirectories(rutaArchivo.getParent());
            }

            // Guardar la imagen del QR en la ruta especificada
            ImageIO.write(qrCode, "PNG", rutaArchivo.toFile());

            return "/qr_codes/" + nombreArchivo;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el código QR", e);
        }
    }

    // Obtener todas las encomiendas (para admin y empleados)
    public List<DetalleEncomiendaDTO> obtenerTodasEncomiendas() {
        List<DetalleEncomienda> encomiendas = detalleEncomiendaRepository.findAll();
        return encomiendas.stream()
                .map(this::convertirADetalleEncomiendaDTO)
                .collect(Collectors.toList());
    }

    // Obtener las encomiendas asociadas al usuario
    public List<DetalleEncomiendaDTO> obtenerEncomiendasPorUsuario(Usuario usuario) {
        List<DetalleEncomienda> encomiendas = detalleEncomiendaRepository.findByUsuario(usuario);
        return encomiendas.stream()
                .map(this::convertirADetalleEncomiendaDTO)
                .collect(Collectors.toList());
    }

}