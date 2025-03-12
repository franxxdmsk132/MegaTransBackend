package com.megatrans.megatransappbackend.Lote.Controller;

import com.lowagie.text.DocumentException;
import com.megatrans.megatransappbackend.Lote.DTO.LoteDTO;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import com.megatrans.megatransappbackend.Lote.Repository.LoteRepository;
import com.megatrans.megatransappbackend.Lote.Service.LoteService;
import com.megatrans.megatransappbackend.Reportes.ExcelServiceLot;
import com.megatrans.megatransappbackend.Reportes.PDFReportServiceLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lote")
@CrossOrigin
public class LoteController {
    @Autowired
    private LoteService loteService;
    @Autowired
    private ExcelServiceLot excelServiceLot;
    @Autowired
    private LoteRepository loteRepository;
    @Autowired
    private PDFReportServiceLot pdfReportServiceLot;

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
    @PutMapping("/{id}/estado2")
    public ResponseEntity<Map<String, String>> actualizarEstadoLote2(@PathVariable Integer id, @RequestParam String nuevoEstado) {
        loteService.actualizarEstadoLote2(id, nuevoEstado);

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
    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportarExcelLote() {
        try {
            List<Lote> lotes = loteRepository.findAll();
            byte[] excelBytes = excelServiceLot.generarReporteExcelLote(lotes);

            // Obtener la fecha actual en formato yyyy-MM-dd
            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=LoteReporte_" + fechaActual + ".xlsx")
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/pdf/{loteId}")
    public ResponseEntity<byte[]> generarReporte(@PathVariable Integer loteId) throws IOException, DocumentException {
        byte[] pdfBytes = pdfReportServiceLot.generarReportePdfLote(loteId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=Reporte_Lote_" + loteId + ".pdf")
                .body(pdfBytes);
    }

}