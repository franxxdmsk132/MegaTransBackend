package com.megatrans.megatransappbackend.Lote.Service;

import com.megatrans.megatransappbackend.Encomiendas.DTO.DetalleEncomiendaDTO;
import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Encomiendas.Repository.DetalleEncomiendaRepository;
import com.megatrans.megatransappbackend.Lote.DTO.LoteDTO;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import com.megatrans.megatransappbackend.Lote.Repository.LoteRepository;
import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import com.megatrans.megatransappbackend.Unidad.Repository.UnidadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoteService {

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private UnidadRepository unidadRepository;

    @Autowired
    private DetalleEncomiendaRepository encomiendaRepository;
    @Autowired
    private DetalleEncomiendaRepository detalleEncomiendaRepository;

    @Transactional
    public Lote crearLote(LoteDTO loteDTO) {
        // Buscar la unidad por ID
        Unidad unidad = unidadRepository.findById(loteDTO.getId())
                .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));

        // Crear la instancia del lote
        Lote lote = new Lote();
        lote.setNumLote(loteDTO.getNumLote());
        lote.setFecha(loteDTO.getFecha());
        lote.setEstado(loteDTO.getEstado());
        lote.setUnidad(unidad);
        lote.setRuta(loteDTO.getRuta());

        // Guardar el lote en la base de datos antes de asignar encomiendas
        final Lote loteGuardado = loteRepository.save(lote); // ✅ Ahora es final

        // Buscar las encomiendas existentes por sus IDs
        List<DetalleEncomienda> encomiendas = detalleEncomiendaRepository.findAllById(loteDTO.getEncomiendaIds());

        // Asignar el lote a cada encomienda
        encomiendas.forEach(encomienda -> encomienda.setLote(loteGuardado));

        // Guardar los cambios en las encomiendas
        detalleEncomiendaRepository.saveAll(encomiendas);

        return loteGuardado;
    }



    // Método para listar todos los lotes
    public List<LoteDTO> listarLotes() {
        List<Lote> lotes = loteRepository.findAll(); // Obtener todos los lotes de la base de datos

        // Mapear cada lote a LoteDTO
        List<LoteDTO> loteDTOs = lotes.stream().map(lote -> {
            LoteDTO loteDTO = new LoteDTO();
            loteDTO.setId(lote.getId());
            loteDTO.setNumLote(lote.getNumLote());
            loteDTO.setFecha(lote.getFecha());
            loteDTO.setEstado(lote.getEstado());
            loteDTO.setRuta(lote.getRuta());

            // Obtener solo los IDs de las encomiendas asociadas al lote
            List<Integer> encomiendaIds = lote.getDetalleEncomiendas().stream()
                    .map(encomienda -> encomienda.getId()) // Obtener ID de cada encomienda
                    .collect(Collectors.toList());
            loteDTO.setEncomiendaIds(encomiendaIds);

            // Obtener los números de guía de las encomiendas
            List<String> numerosGuia = lote.getDetalleEncomiendas().stream()
                    .map(encomienda -> encomienda.getNumGuia()) // Obtener el número de guía de cada encomienda
                    .collect(Collectors.toList());
            loteDTO.setNumerosGuia(numerosGuia); // Asignar la lista de números de guía

            loteDTO.setUnidad(lote.getUnidad()); // Asignar la unidad asociada al lote

            return loteDTO; // Retornar el DTO
        }).collect(Collectors.toList());

        return loteDTOs; // Devolver la lista de LoteDTOs
    }
    @Transactional
    public Lote actualizarLote(Integer id, LoteDTO loteDTO) {
        // Buscar el lote por ID
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        // Buscar la unidad si se proporciona un nuevo ID de unidad
        if (loteDTO.getUnidad() != null && !lote.getUnidad().getId().equals(loteDTO.getUnidad().getId())) {
            Unidad unidad = unidadRepository.findById(loteDTO.getUnidad().getId())
                    .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));
            lote.setUnidad(unidad);
        }

        // Actualizar los datos del lote
        lote.setNumLote(loteDTO.getNumLote());
        lote.setFecha(loteDTO.getFecha());
        lote.setEstado(loteDTO.getEstado());
        lote.setRuta(loteDTO.getRuta());

        // Buscar las encomiendas existentes por sus IDs y asignarlas al lote
        List<DetalleEncomienda> encomiendas = detalleEncomiendaRepository.findAllById(loteDTO.getEncomiendaIds());

        // Desvincular encomiendas anteriores
        lote.getDetalleEncomiendas().forEach(encomienda -> encomienda.setLote(null));

        // Asignar las nuevas encomiendas al lote
        encomiendas.forEach(encomienda -> encomienda.setLote(lote));

        // Guardar cambios en las encomiendas
        detalleEncomiendaRepository.saveAll(encomiendas);

        // Guardar los cambios en el lote
        return loteRepository.save(lote);
    }

    public LoteDTO obtenerLotePorId(Integer id) {
        // Buscar el lote por su ID
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        // Convertir el lote a DTO
        LoteDTO loteDTO = new LoteDTO();
        loteDTO.setId(lote.getId());
        loteDTO.setNumLote(lote.getNumLote());
        loteDTO.setFecha(lote.getFecha());
        loteDTO.setEstado(lote.getEstado());
        loteDTO.setRuta(lote.getRuta());

        // Obtener solo los IDs de las encomiendas asociadas al lote
        List<Integer> encomiendaIds = lote.getDetalleEncomiendas().stream()
                .map(DetalleEncomienda::getId) // Obtener ID de cada encomienda
                .collect(Collectors.toList());
        loteDTO.setEncomiendaIds(encomiendaIds);

        // Obtener los números de guía de las encomiendas
        List<String> numerosGuia = lote.getDetalleEncomiendas().stream()
                .map(DetalleEncomienda::getNumGuia) // Obtener el número de guía de cada encomienda
                .collect(Collectors.toList());
        loteDTO.setNumerosGuia(numerosGuia);

        loteDTO.setUnidad(lote.getUnidad()); // Asignar la unidad asociada al lote

        return loteDTO; // Retornar el DTO con la información del lote
    }
    @Transactional
    public Lote actualizarEstadoLote(Integer id, String nuevoEstado) {
        // Buscar el lote por su ID
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        // Actualizar el estado del lote
        lote.setEstado(nuevoEstado);

        // Guardar y retornar el lote actualizado
        return loteRepository.save(lote);
    }




}
