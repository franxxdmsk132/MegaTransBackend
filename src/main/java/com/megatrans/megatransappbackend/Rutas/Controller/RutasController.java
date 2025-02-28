package com.megatrans.megatransappbackend.Rutas.Controller;

import com.megatrans.megatransappbackend.Rutas.DTO.RutasDTO;
import com.megatrans.megatransappbackend.Rutas.Service.RutasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rutas")
@CrossOrigin
public class RutasController {


    private final RutasService rutasService;

    public RutasController(RutasService rutasService) {
        this.rutasService = rutasService;
    }

    @GetMapping
    public ResponseEntity<?> getRutas() {
        return rutasService.getRutas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRuta(@PathVariable int id) {
        return rutasService.getRuta(id);
    }

    @PostMapping
    public ResponseEntity<?> addRuta(@RequestBody RutasDTO rutasDTO) {
        return rutasService.addRuta(rutasDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRuta(@PathVariable int id) {
        return rutasService.deleteRuta(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRuta(@PathVariable int id, @RequestBody RutasDTO rutasDTO) {
        rutasDTO.setId(id);
        return rutasService.updateRuta(rutasDTO);
    }

}