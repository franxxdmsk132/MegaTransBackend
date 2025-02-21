package com.megatrans.megatransappbackend.Encomiendas.Controller;

import com.megatrans.megatransappbackend.Encomiendas.DTO.DetalleEncomiendaDTO;
import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Encomiendas.Repository.DetalleEncomiendaRepository;
import com.megatrans.megatransappbackend.Encomiendas.Service.DetalleEncomiendaService;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping
    public List<DetalleEncomiendaDTO> getAllDetalleEncomiendas() {
        return detalleEncomiendaService.getAllDetalleEncomiendas();
    }
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




}