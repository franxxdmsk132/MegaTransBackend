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
import java.util.Optional;
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
        Unidad unidad = unidadRepository.findById(loteDTO.getIdUnidad())
                .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));

        // Crear la instancia del lote
        Lote lote = new Lote();
        lote.setNumLote(generarNuevoNumLote());
        lote.setFecha(loteDTO.getFecha());
        lote.setEstado("PENDIENTE");
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

            loteDTO.setIdUnidad(lote.getUnidad().getId()); // Asignar la unidad asociada al lote

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
        if (loteDTO.getIdUnidad() != null && !lote.getUnidad().getId().equals(loteDTO.getIdUnidad())) {
            Unidad unidad = unidadRepository.findById(loteDTO.getIdUnidad())
                    .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));
            lote.setUnidad(unidad);
        }

        // Actualizar los datos del lote
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

        loteDTO.setIdUnidad(lote.getUnidad().getId()); // Asignar la unidad asociada al lote

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
    @Transactional
    public Lote actualizarEstadoLote2(Integer id, String nuevoEstado) {
        // Buscar el lote por su ID
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        // Verificar estado del lote
        System.out.println("Estado actual del lote: " + lote.getEstado());

        // Obtener las encomiendas asociadas al lote
        List<DetalleEncomienda> encomiendas = detalleEncomiendaRepository.findByLote(lote);

        // Verificar que se obtuvieron encomiendas
        if (encomiendas.isEmpty()) {
            throw new RuntimeException("No hay encomiendas asociadas al lote");
        }

        // Verificar los estados de las encomiendas antes de actualizar
        for (DetalleEncomienda encomienda : encomiendas) {
            System.out.println("Estado de encomienda " + encomienda.getNumGuia() + ": " + encomienda.getEstado());
        }

        // Si el estado cambia de "PENDIENTE" a "TRASLADANDO", actualizar las encomiendas
        if ("PENDIENTE".equals(lote.getEstado()) && "TRASLADANDO".equals(nuevoEstado)) {
            System.out.println("Entrando en el primer if");
            for (DetalleEncomienda encomienda : encomiendas) {
                if ("RECOLECTADO".equals(encomienda.getEstado())) {
                    encomienda.setEstado("TRASLADO");
                    System.out.println("Actualizando estado de encomienda " + encomienda.getNumGuia() + " a TRASLADO");
                }
            }
            // Guardar los cambios en las encomiendas
            System.out.println("Guardando cambios en las encomiendas...");
            encomiendaRepository.saveAll(encomiendas);
            encomiendaRepository.flush(); // Asegura que los cambios se persistan

            // Verificar los estados de las encomiendas después de la actualización
            for (DetalleEncomienda encomienda : encomiendas) {
                System.out.println("Estado actualizado de encomienda " + encomienda.getNumGuia() + ": " + encomienda.getEstado());
            }
        }

        // Si el estado cambia de "TRASLANDANDO" a "BODEGA", actualizar las encomiendas
        if ("TRASLADANDO".equals(lote.getEstado()) && "BODEGA".equals(nuevoEstado)) {
            System.out.println("Entrando en el segundo if");
            for (DetalleEncomienda encomienda : encomiendas) {
                if ("TRASLADO".equals(encomienda.getEstado())) {
                    encomienda.setEstado("BODEGA");
                    System.out.println("Actualizando estado de encomienda " + encomienda.getNumGuia() + " a BODEGA");
                }
            }
            // Guardar los cambios en las encomiendas
            System.out.println("Guardando cambios en las encomiendas...");
            encomiendaRepository.saveAll(encomiendas);
            encomiendaRepository.flush(); // Asegura que los cambios se persistan

            // Verificar los estados de las encomiendas después de la actualización
            for (DetalleEncomienda encomienda : encomiendas) {
                System.out.println("Estado actualizado de encomienda " + encomienda.getNumGuia() + ": " + encomienda.getEstado());
            }
        }

        // Actualizar el estado del lote
        lote.setEstado(nuevoEstado);

        // Guardar y retornar el lote actualizado
        return loteRepository.save(lote);
    }



    public String generarNuevoNumLote() {
        Optional<String> lastNumLote = loteRepository.findLastNumLote();

        if (lastNumLote.isPresent()) {
            // Extraer el número actual y aumentar en 1
            String lastLote = lastNumLote.get();
            int lastNumber = Integer.parseInt(lastLote.substring(2)); // Extrae el número ignorando 'TM'
            int nextNumber = lastNumber + 1;

            // Formatear con ceros a la izquierda (mínimo 5 dígitos)
            return String.format("LT%05d", nextNumber);
        } else {
            // Si no hay registros, comenzar con TM00001
            return "LT00001";
        }
    }



}
