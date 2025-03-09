package com.megatrans.megatransappbackend.Encomiendas.Controller;

import com.megatrans.megatransappbackend.Encomiendas.DTO.DetalleEncomiendaDTO;
import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Encomiendas.Repository.DetalleEncomiendaRepository;
import com.megatrans.megatransappbackend.Encomiendas.Service.DetalleEncomiendaService;
import com.megatrans.megatransappbackend.Reportes.ExcelService;
import com.megatrans.megatransappbackend.Reportes.ExcelServiceEnc;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.repository.UsuarioRepository;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/detalle-encomienda")
@CrossOrigin
public class DetalleEncomiendaController {
    @Autowired
    private DetalleEncomiendaService detalleEncomiendaService;
    @Autowired
    private DetalleEncomiendaRepository detalleEncomiendaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ExcelServiceEnc excelServiceEnc;

//    @GetMapping
//    public List<DetalleEncomiendaDTO> getAllDetalleEncomiendas() {
//        return detalleEncomiendaService.getEncomiendasPorUsuario();
//    }
    @PostMapping("/crear")
    public DetalleEncomiendaDTO crearEncomienda(@RequestBody DetalleEncomiendaDTO detalleEncomiendaDTO) {
        return detalleEncomiendaService.crearEncomienda(detalleEncomiendaDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DetalleEncomiendaDTO> obtenerDetalleEncomienda(@PathVariable Integer id) {
        DetalleEncomiendaDTO detalleEncomiendaDTO = detalleEncomiendaService.getDetalleEncomiendaById(id);
        return ResponseEntity.ok(detalleEncomiendaDTO);
    }
    @GetMapping("/guia/{numGuia}")
    public ResponseEntity<DetalleEncomiendaDTO> obtenerDetalleEncomiendaPorNumGuia(@PathVariable String numGuia) {
        DetalleEncomiendaDTO detalleEncomiendaDTO = detalleEncomiendaService.getDetalleEncomiendaByNumGuia(numGuia);
        return ResponseEntity.ok(detalleEncomiendaDTO);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarEstado(@PathVariable Integer id, @RequestBody DetalleEncomienda estado) {
        boolean actualizado = detalleEncomiendaService.actualizarEstado(id, estado.getEstado());
        if (actualizado) {
            return ResponseEntity.ok("Estado actualizado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el detalle de Encomienda.");
        }
    }

    @GetMapping("/{numGuia}/qr")
    public ResponseEntity<Resource> obtenerCodigoQR(@PathVariable String numGuia) throws IOException {
        Path path = Paths.get("qr_codes/" + numGuia + ".png");
        Resource resource = new FileSystemResource(path);
        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/filtrados")
    public ResponseEntity<List<DetalleEncomiendaDTO>> listarDetallesEncomienda(Authentication authentication) {
        // Obtener el nombre de usuario desde la autenticación
        String username = authentication.getName();

        // Buscar el usuario autenticado en la base de datos
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener el rol del usuario
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isEmpleado = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPL"));

        List<DetalleEncomiendaDTO> encomiendasDTO;

        if (isAdmin || isEmpleado) {
            // Si es ADMIN o EMPLEADO, obtener todas las encomiendas
            encomiendasDTO = detalleEncomiendaService.obtenerTodasEncomiendas();
        } else {
            // Si es un usuario común, obtener solo las encomiendas asociadas a ese usuario
            encomiendasDTO = detalleEncomiendaService.obtenerEncomiendasPorUsuario(usuario);
        }

        return ResponseEntity.ok(encomiendasDTO);
    }

    @GetMapping("/Recoleccion")
    public List<DetalleEncomiendaDTO> listarEncomiendasPendientes() {
        return detalleEncomiendaService.obtenerEncomiendasPendientes();
    }
    @GetMapping("/recolectados")
    public List<DetalleEncomiendaDTO> listarEncomiendasRecolectadas() {
        return detalleEncomiendaService.obtenerEncomiendasRecolectadas();
    }
    @GetMapping("/recolectadonlt")
    public List<DetalleEncomiendaDTO> listarEncomiendasRecolectadasSinLote() {
        return detalleEncomiendaService.obtenerEncomiendasRecolectadasSinLote();
    }
    @GetMapping("/excel")
    public ResponseEntity<byte[]> descargarReporteExcel() {
        try {
            List<DetalleEncomienda> encomiendas = detalleEncomiendaRepository.findAll();
            byte[] excelBytes = excelServiceEnc.generarReporteExcel(encomiendas);

            // Obtener la fecha actual en formato yyyy-MM-dd
            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=Reporte_Encomiendas_" + fechaActual + ".xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}