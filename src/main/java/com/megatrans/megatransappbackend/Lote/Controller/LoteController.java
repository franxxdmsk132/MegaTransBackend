package com.megatrans.megatransappbackend.Lote.Controller;

import com.megatrans.megatransappbackend.Lote.DTO.LoteDTO;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import com.megatrans.megatransappbackend.Lote.Service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> actualizarEstadoLote(@PathVariable Integer id, @RequestParam String nuevoEstado) {
        loteService.actualizarEstadoLote(id, nuevoEstado);

        // Crear una respuesta personalizada con solo un mensaje
        Map<String, String> response = new HashMap<>();
        response.put("message", "El estado del lote ha sido actualizado correctamente.");

        return ResponseEntity.ok(response);
    }

    // ✅ Actualizar todo el lote por ID
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Map<String, String>> actualizarLote(@PathVariable Integer id, @RequestBody LoteDTO loteDTO) {
        loteService.actualizarLote(id, loteDTO);

        // Crear una respuesta personalizada con solo un mensaje
        Map<String, String> response = new HashMap<>();
        response.put("message", "El lote ha sido actualizado correctamente.");

        return ResponseEntity.ok(response);
    }

}