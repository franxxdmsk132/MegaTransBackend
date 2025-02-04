package com.megatrans.megatransappbackend.Transporte_Mudanza.Service;

import com.megatrans.megatransappbackend.Transporte_Mudanza.DTO.DireccionDTO;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.Direccion;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Repository.DireccionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    private DireccionDTO convertirADTO(Direccion direccion) {
        DireccionDTO dto = new DireccionDTO();
        dto.setId(direccion.getId());
        dto.setCallePrincipal(direccion.getCallePrincipal());
        dto.setCalleSecundaria(direccion.getCalleSecundaria());
        dto.setBarrio(direccion.getBarrio());
        dto.setCiudad(direccion.getCiudad());
        dto.setReferencia(direccion.getReferencia());
        dto.setTelefono(direccion.getTelefono());
        dto.setLatitud(direccion.getLatitud());
        dto.setLongitud(direccion.getLongitud());
        return dto;
    }
    // Convertir DTO a Entidad
    private Direccion convertirAEntidad(DireccionDTO dto) {
        Direccion direccion = new Direccion();
        direccion.setId(dto.getId());
        direccion.setCallePrincipal(dto.getCallePrincipal());
        direccion.setCalleSecundaria(dto.getCalleSecundaria());
        direccion.setBarrio(dto.getBarrio());
        direccion.setCiudad(dto.getCiudad());
        direccion.setReferencia(dto.getReferencia());
        direccion.setTelefono(dto.getTelefono());
        direccion.setLatitud(dto.getLatitud());
        direccion.setLongitud(dto.getLongitud());
        return direccion;
    }
    // Obtener todas las direcciones
    public List<DireccionDTO> obtenerTodas() {
        return direccionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener una dirección por ID
    public DireccionDTO obtenerPorId(Long id) {
        return direccionRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
    }

    // Guardar una dirección
    public DireccionDTO guardar(DireccionDTO dto) {
        Direccion direccion = convertirAEntidad(dto);
        direccion = direccionRepository.save(direccion);
        return convertirADTO(direccion);
    }

    // Eliminar una dirección por ID
    public void eliminar(Long id) {
        direccionRepository.deleteById(id);
    }
}

