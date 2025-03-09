package com.megatrans.megatransappbackend.Transporte_Mudanza.Controller;

import com.megatrans.megatransappbackend.Reportes.ExcelService;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.repository.UsuarioRepository;
import com.megatrans.megatransappbackend.Transporte_Mudanza.DTO.DetalleTransporteDTO;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.Direccion;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Repository.DetalleTransporteRepository;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Repository.DireccionRepository;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Service.DetalleTransporteService;
import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import com.megatrans.megatransappbackend.Unidad.Repository.UnidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/detalle-transporte")
@CrossOrigin
public class DetalleTransporteController {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private DetalleTransporteRepository detalleTransporteRepository;

    @Autowired
    private UnidadRepository unidadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ExcelService excelService;


    private final DetalleTransporteService detalleTransporteService;

    public DetalleTransporteController(DetalleTransporteService detalleTransporteService) {
        this.detalleTransporteService = detalleTransporteService;
    }

    // POST para crear un nuevo detalle de transporte
    @PostMapping
    public ResponseEntity<DetalleTransporte> crearDetalleTransporte(@RequestBody DetalleTransporteDTO dto, Authentication authentication) {

        // Obtener el nombre de usuario desde la sesión (esto asume que usas Spring Security)
        String username = authentication.getName();

        // Buscar el usuario autenticado en la base de datos
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Verificar si las direcciones ya existen, de lo contrario, crearlas
        Direccion direccionOrigen = new Direccion();
        direccionOrigen.setBarrio(dto.getDireccionOrigen().getBarrio());
        direccionOrigen.setCallePrincipal(dto.getDireccionOrigen().getCallePrincipal());
        direccionOrigen.setCalleSecundaria(dto.getDireccionOrigen().getCalleSecundaria());
        direccionOrigen.setCiudad(dto.getDireccionOrigen().getCiudad());
        direccionOrigen.setLatitud(dto.getDireccionOrigen().getLatitud());
        direccionOrigen.setLongitud(dto.getDireccionOrigen().getLongitud());
        direccionOrigen.setReferencia(dto.getDireccionOrigen().getReferencia());
        direccionOrigen.setTelefono(dto.getDireccionOrigen().getTelefono());

        direccionOrigen = direccionRepository.save(direccionOrigen); // Guardamos la dirección de origen

        Direccion direccionDestino = new Direccion();
        direccionDestino.setBarrio(dto.getDireccionDestino().getBarrio());
        direccionDestino.setCallePrincipal(dto.getDireccionDestino().getCallePrincipal());
        direccionDestino.setCalleSecundaria(dto.getDireccionDestino().getCalleSecundaria());
        direccionDestino.setCiudad(dto.getDireccionDestino().getCiudad());
        direccionDestino.setLatitud(dto.getDireccionDestino().getLatitud());
        direccionDestino.setLongitud(dto.getDireccionDestino().getLongitud());
        direccionDestino.setReferencia(dto.getDireccionDestino().getReferencia());
        direccionDestino.setTelefono(dto.getDireccionDestino().getTelefono());

        direccionDestino = direccionRepository.save(direccionDestino); // Guardamos la dirección de destino

        // Buscar la unidad y el usuario por sus IDs
        Unidad unidad = unidadRepository.findById(dto.getUnidadId()).orElseThrow(() -> new RuntimeException("Unidad no encontrada"));
//        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar automáticamente el numOrden
        String numOrden = detalleTransporteService.generarNuevoNumOrden();

        // Crear el detalle de transporte
        DetalleTransporte detalle = new DetalleTransporte();
        detalle.setCantidadEstibaje(dto.getCantidadEstibaje());
        detalle.setDescripcionProducto(dto.getDescripcionProducto());
        detalle.setEstado("PENDIENTE");
        detalle.setTipoServicio(dto.getTipoServicio().name());
        detalle.setEstibaje(dto.getEstibaje());
        detalle.setFecha(dto.getFecha());
        detalle.setNumOrden(numOrden);
        detalle.setPago(dto.getPago().name());
        detalle.setDirOrigen(direccionOrigen);  // Asociar la dirección de origen
        detalle.setDirDestino(direccionDestino);  // Asociar la dirección de destino
        detalle.setUnidad(unidad);
        detalle.setUsuario(usuario);

        // Guardar el detalle de transporte
        DetalleTransporte detalleGuardado = detalleTransporteRepository.save(detalle);

        return ResponseEntity.status(HttpStatus.CREATED).body(detalleGuardado);
    }

    // Obtener todos los detalles de transporte
    @GetMapping
    public ResponseEntity<List<DetalleTransporte>> listarDetallesTransporte() {
        List<DetalleTransporte> detalles = detalleTransporteService.listar();
        return ResponseEntity.ok(detalles);
    }


    // Obtener un DetalleTransporte por ID
    @GetMapping("/{id}")
    public ResponseEntity<DetalleTransporte> obtenerPorId(@PathVariable Long id) {
        return detalleTransporteService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //    // Actualizar un DetalleTransporte existente
//    @PutMapping("/{id}")
//    public ResponseEntity<DetalleTransporte> actualizarDetalleTransporte(
//            @PathVariable Long id, @RequestBody DetalleTransporteDTO dto) {
//        DetalleTransporte detalleActualizado = detalleTransporteService.actualizar(id, dto);
//        return ResponseEntity.ok(detalleActualizado);
//    }
// Endpoint para buscar por numOrden
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarPorNumOrden(@RequestParam String numOrden) {
        Optional<DetalleTransporte> detalle = detalleTransporteRepository.findByNumOrden(numOrden);
        Map<String, Object> response = new HashMap<>();

        if (detalle.isPresent()) {
            response.put("mensaje", "Número de orden " + numOrden + " encontrado.");
            response.put("detalle", detalle.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Número de orden no encontrado: " + numOrden);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }


    // Eliminar un DetalleTransporte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleTransporte(@PathVariable Long id) {
        detalleTransporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarEstado(@PathVariable Long id, @RequestBody DetalleTransporte estado) {
        boolean actualizado = detalleTransporteService.actualizarEstado(id, estado.getEstado());
        if (actualizado) {
            return ResponseEntity.ok("Estado actualizado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el detalle de transporte.");
        }
    }


    // Guardar un nuevo detalle de transporte
    @PostMapping("/nuevo")
    public ResponseEntity<DetalleTransporte> guardarDetalle(@RequestBody DetalleTransporteDTO dto) {
        try {
            DetalleTransporte nuevoDetalle = detalleTransporteService.guardar(dto);
            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/filtrados")
    public ResponseEntity<List<DetalleTransporte>> listarDetallesTransporte(Authentication authentication) {
        // Obtener el nombre de usuario desde la sesión
        String username = authentication.getName();

        // Buscar el usuario autenticado en la base de datos
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener el rol del usuario
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isEmpleado = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPL"));

        List<DetalleTransporte> detalles;

        if (isAdmin || isEmpleado) {
            // Si es ADMIN o EMPLEADO, obtener todos los detalles
            detalles = detalleTransporteService.listar();
        } else {
            // Si es USUARIO, obtener solo los detalles asociados a ese usuario
            detalles = detalleTransporteRepository.findByUsuario(usuario);
        }

        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportarExcel() {
        try {
            List<DetalleTransporte> detalles = detalleTransporteRepository.findAll();
            byte[] excelBytes = excelService.generarExcelTransporte(detalles);

            // Obtener la fecha actual en formato yyyy-MM-dd
            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Transporte_" + fechaActual + ".xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}