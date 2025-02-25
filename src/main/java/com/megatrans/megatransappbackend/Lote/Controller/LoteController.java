package com.megatrans.megatransappbackend.Lote.Controller;

import com.megatrans.megatransappbackend.Lote.DTO.LoteDTO;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import com.megatrans.megatransappbackend.Lote.Service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lote")
@CrossOrigin
public class LoteController {
    @Autowired
    private LoteService loteService;

    @PostMapping
    public ResponseEntity<Lote> crearLote(@RequestBody LoteDTO loteDTO) {
        // Llamamos al servicio para crear el lote
        Lote loteCreado = loteService.crearLote(loteDTO);

        // Respondemos con el lote creado
        return ResponseEntity.status(HttpStatus.CREATED).body(loteCreado);
    }

    // Método para listar todos los lotes
    @GetMapping
    public List<LoteDTO> listarLotes() {
        return loteService.listarLotes(); // Devuelve la lista de LoteDTOs
    }
    // 📌 Endpoint para obtener un lote por ID
    @GetMapping("/{id}")
    public ResponseEntity<LoteDTO> obtenerLotePorId(@PathVariable Integer id) {
        LoteDTO loteDTO = loteService.obtenerLotePorId(id);
        return ResponseEntity.ok(loteDTO);
    }

    // 📌 Endpoint para actualizar solo el estado de un lote
    @PutMapping("/{id}/estado")
    public ResponseEntity<Lote> actualizarEstadoLote(@PathVariable Integer id, @RequestParam String nuevoEstado) {
        Lote loteActualizado = loteService.actualizarEstadoLote(id, nuevoEstado);
        return ResponseEntity.ok(loteActualizado);
    }
    // ✅ Actualizar todo el lote por ID
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Lote> actualizarLote(@PathVariable Integer id, @RequestBody LoteDTO loteDTO) {
        Lote loteActualizado = loteService.actualizarLote(id, loteDTO);
        return ResponseEntity.ok(loteActualizado);
    }
}