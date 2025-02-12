package com.megatrans.megatransappbackend.Transporte_Mudanza.Controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        detalle.setEstado(dto.getEstado().name());
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
            response.put("error", "Error: No se encontró el número de orden: " + numOrden);
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
    public ResponseEntity<String> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        boolean actualizado = detalleTransporteService.actualizarEstado(id, estado);
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
}