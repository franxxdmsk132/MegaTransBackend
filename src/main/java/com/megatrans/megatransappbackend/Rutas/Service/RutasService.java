package com.megatrans.megatransappbackend.Rutas.Service;

import com.megatrans.megatransappbackend.Rutas.DTO.RutasDTO;
import com.megatrans.megatransappbackend.Rutas.Entity.Rutas;
import com.megatrans.megatransappbackend.Rutas.Repository.RutasRepository;
import com.megatrans.megatransappbackend.dto.Mensaje;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RutasService {

    private final RutasRepository rutasRepository;

    public RutasService(RutasRepository rutasRepository) {
        this.rutasRepository = rutasRepository;
    }

    // Convertir de entidad a DTO
    private RutasDTO convertToDTO(Rutas ruta) {
        RutasDTO rutasDTO = new RutasDTO();
        rutasDTO.setId(ruta.getId());
        rutasDTO.setOrigen(ruta.getOrigen());
        rutasDTO.setDestino(ruta.getDestino());
        return rutasDTO;
    }

    // Convertir de DTO a entidad
    private Rutas convertToEntity(RutasDTO rutasDTO) {
        Rutas ruta = new Rutas();
        ruta.setId(rutasDTO.getId());
        ruta.setOrigen(rutasDTO.getOrigen());
        ruta.setDestino(rutasDTO.getDestino());
        return ruta;
    }

    // Obtener todas las rutas
    public ResponseEntity<?> getRutas() {
        List<Rutas> rutas = rutasRepository.findAll();
        if (!rutas.isEmpty()) {
            List<RutasDTO> rutasDTOs = rutas.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(rutasDTOs, HttpStatus.OK);
        } else {
            Mensaje mensaje = new Mensaje("No se encontraron rutas");
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }
    }

    // Obtener una ruta por ID
    public ResponseEntity<?> getRuta(int id) {
        Optional<Rutas> ruta = rutasRepository.findById(id);
        if (ruta.isPresent()) {
            return new ResponseEntity<>(convertToDTO(ruta.get()), HttpStatus.OK);
        } else {
            Mensaje mensaje = new Mensaje("No se encontró la ruta con ese ID");
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }
    }

    // Agregar una nueva ruta
    public ResponseEntity<?> addRuta(RutasDTO rutasDTO) {
        Rutas ruta = convertToEntity(rutasDTO);
        rutasRepository.save(ruta);
        Mensaje mensaje = new Mensaje("Ruta agregada con éxito");
        return new ResponseEntity<>(mensaje, HttpStatus.CREATED);
    }

    // Eliminar una ruta por ID
    public ResponseEntity<?> deleteRuta(int id) {
        if (rutasRepository.existsById(id)) {
            rutasRepository.deleteById(id);
            Mensaje mensaje = new Mensaje("Ruta eliminada con éxito");
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } else {
            Mensaje mensaje = new Mensaje("Ruta no encontrada");
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar una ruta existente
    public ResponseEntity<?> updateRuta(RutasDTO rutasDTO) {
        // Convierte el DTO en la entidad
        Rutas ruta = convertToEntity(rutasDTO);
        // Verifica si la ruta existe
        if (rutasRepository.existsById(ruta.getId())) {
            // Si existe, la actualiza
            rutasRepository.save(ruta);
            Mensaje mensaje = new Mensaje("Ruta actualizada con éxito");
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } else {
            Mensaje mensaje = new Mensaje("Ruta no encontrada con ID: " + ruta.getId());
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }
    }
}
