package com.megatrans.megatransappbackend.Soporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/soporte")
@CrossOrigin
public class SoporteController {

    @Autowired
    private SoporteService soporteService;

    @PostMapping
    public ResponseEntity<String> enviarSoporte(@RequestBody SoporteDTO soporte) {
        soporteService.enviarSoporte(soporte);
        return ResponseEntity.ok("Soporte enviado correctamente.");
    }
}
