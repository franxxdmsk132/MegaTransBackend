package com.megatrans.megatransappbackend.websocket.controller;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.megatrans.megatransappbackend.Encomiendas.DTO.DetalleEncomiendaDTO;
import com.megatrans.megatransappbackend.Encomiendas.DTO.EstadoEncomiendaDTO;
import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Encomiendas.Entity.EstadoEncomienda;
import com.megatrans.megatransappbackend.Encomiendas.Repository.DetalleEncomiendaRepository;
import com.megatrans.megatransappbackend.Encomiendas.Repository.EstadoEncomiendaRepository;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.enums.RolNombre;
import com.megatrans.megatransappbackend.Security.repository.UsuarioRepository;
import com.megatrans.megatransappbackend.Transporte_Mudanza.DTO.EstadoDetalleTransporteDTO;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.EstadoDetalleTransporte;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Repository.DetalleTransporteRepository;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Repository.EstadoDetalleTransporteRepository;
import com.megatrans.megatransappbackend.websocket.service.FmcService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class WebSocketService {

    private final FmcService fmcService;

    private final UsuarioRepository usuarioRepository;

    private final DetalleEncomiendaRepository detalleEncomiendaRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final EstadoEncomiendaRepository estadoEncomiendaRepository;

    private final DetalleTransporteRepository detalleTransporteRepository;

    private final EstadoDetalleTransporteRepository estadoDetalleTransporteRepository;

    @MessageMapping("/user/request")
    @Transactional
    public void userNeedTransport(@Payload EstadoEncomiendaDTO estadoEncomiendaDTO) throws FirebaseMessagingException {
        EstadoEncomienda estadoEncomienda = updateEncomienda(estadoEncomiendaDTO);
        estadoEncomiendaDTO.setId(estadoEncomienda.getId());
        estadoEncomiendaDTO.setFechaCreacion(estadoEncomienda.getFechaCreacion().toString());
        List<Usuario> admins = usuarioRepository.findAllByRoles_RolNombre(RolNombre.ROLE_ADMIN);

        List<String> tokens = admins.stream()
                .map((Usuario::getTokenFMC))
                .filter(Objects::nonNull)
                .toList();

        System.out.println(estadoEncomiendaDTO.toJson());

        fmcService.sendNotificationToAllTokens(tokens, "Solicitud de Encomienda", estadoEncomiendaDTO.toJson());

        simpMessagingTemplate.convertAndSend("/specific/request", estadoEncomiendaDTO);
    }
    @MessageMapping("/transporte/request")
    @Transactional
    public void userNeedTransportMudanza(@Payload EstadoDetalleTransporteDTO inputDto) throws FirebaseMessagingException {
        EstadoDetalleTransporte estado = saveOrUpdateEstadoTransporte(inputDto);

        EstadoDetalleTransporteDTO dto = buildEstadoDTO(estado); // reconstruido con coordenadas
        if (dto == null) return;

        List<Usuario> admins = usuarioRepository.findAllByRoles_RolNombre(RolNombre.ROLE_ADMIN);
        List<String> tokens = admins.stream()
                .map(Usuario::getTokenFMC)
                .filter(Objects::nonNull)
                .toList();

        fmcService.sendNotificationToAllTokens(tokens, "Solicitud de Transporte", dto.toJson());
        simpMessagingTemplate.convertAndSend("/specific/transporte/request", dto);
    }


    @MessageMapping("/admin/response")
    @Transactional
    public void adminAnswer(EstadoEncomiendaDTO estadoEncomiendaDTO) {
        updateEncomienda(estadoEncomiendaDTO);

        updateDetalleEncomienda(estadoEncomiendaDTO.getEncomienda(), estadoEncomiendaDTO.getEstado());

        usuarioRepository.findByNombreUsuario(estadoEncomiendaDTO.getEmail()).ifPresent(usuario -> {
            if (usuario.getTokenFMC().isEmpty()) {
                return;
            }

            String message = "Tu solicitud fue %s por un administrador.";
            fmcService.sendNotification(usuario.getTokenFMC(), "Se revisó tu solicitud.",
                    String.format(message, estadoEncomiendaDTO.getEstado() ? "aceptada" : "rechazada")
            );
        });

        simpMessagingTemplate.convertAndSend("/specific/response", estadoEncomiendaDTO);
    }
    @MessageMapping("/admin/transporte/response")
    @Transactional
    public void adminAnswerTransporte(@Payload EstadoDetalleTransporteDTO dto) {
        saveOrUpdateEstadoTransporte(dto);
        updateDetalleTransporte(Long.valueOf(dto.getTransporte()), dto.getEstado());

        // reconstruir DTO con coordenadas
        EstadoDetalleTransporteDTO enrichedDto = buildEstadoDTO(
                estadoDetalleTransporteRepository.findByDetalleTransporte_Id(dto.getTransporte()).orElse(null)
        );

        if (enrichedDto == null) return;

        usuarioRepository.findByNombreUsuario(dto.getEmail()).ifPresent(usuario -> {
            if (usuario.getTokenFMC() != null && !usuario.getTokenFMC().isEmpty()) {
                String message = "Tu solicitud de transporte fue %s por un administrador.";
                fmcService.sendNotification(usuario.getTokenFMC(), "Estado de tu solicitud de transporte",
                        String.format(message, enrichedDto.getEstado() ? "aceptada" : "rechazada")
                );
            }
        });

        simpMessagingTemplate.convertAndSend("/specific/transporte/response", enrichedDto);
    }


    /**
     * This method is used to save the status of a package.
     *
     * @param estadoEncomiendaDTO
     */
    private EstadoEncomienda updateEncomienda(EstadoEncomiendaDTO estadoEncomiendaDTO) {

        if (estadoEncomiendaDTO.getId() == null) {
            estadoEncomiendaDTO.setId(-20);
        }

        EstadoEncomienda existingEstadoEncomienda = estadoEncomiendaRepository.findById(estadoEncomiendaDTO.getId()).orElse(null);

        if (Objects.isNull(existingEstadoEncomienda)) {
            EstadoEncomienda estadoEncomienda = new EstadoEncomienda();
            DetalleEncomienda detalleEncomienda = new DetalleEncomienda();
            detalleEncomienda.setId(estadoEncomiendaDTO.getEncomienda());

            estadoEncomienda.setEstado(estadoEncomiendaDTO.getEstado());
            estadoEncomienda.setFechaCreacion(LocalDateTime.now());
            estadoEncomienda.setDetalleEncomienda(detalleEncomienda);

            return estadoEncomiendaRepository.save(estadoEncomienda);
        }

        existingEstadoEncomienda.setEstado(estadoEncomiendaDTO.getEstado());

        return estadoEncomiendaRepository.save(existingEstadoEncomienda);
    }

    private DetalleEncomienda updateDetalleEncomienda(Integer idEncomienda, Boolean estadoEncomienda) {
        if (idEncomienda == null) {
            return null;
        }

        DetalleEncomienda detalleEncomienda = detalleEncomiendaRepository.findById(idEncomienda).orElse(null);

        if (Objects.isNull(detalleEncomienda)) {
            return null;
        }

        detalleEncomienda.setEstado(String.valueOf(estadoEncomienda ? DetalleEncomiendaDTO.EstadoEncomienda.RECOLECCION : DetalleEncomiendaDTO.EstadoEncomienda.CANCELADO));

        return detalleEncomiendaRepository.save(detalleEncomienda);
    }

    private DetalleTransporte updateDetalleTransporte(Long idTransporte, Boolean estadoTransporte) {
        if (idTransporte == null) {
            return null;
        }

        DetalleTransporte detalle = detalleTransporteRepository.findById(idTransporte).orElse(null);
        if (detalle == null) return null;

        detalle.setEstado(estadoTransporte ? "PROCESANDO" : "CANCELADO");

        return detalleTransporteRepository.save(detalle);
    }


    private EstadoDetalleTransporte saveOrUpdateEstadoTransporte(EstadoDetalleTransporteDTO dto) {
        Integer detalleId = Integer.valueOf(dto.getTransporte());

        // Buscar si ya existe un estado para ese transporte
        Optional<EstadoDetalleTransporte> optionalEstado = estadoDetalleTransporteRepository
                .findByDetalleTransporte_Id(detalleId);

        if (optionalEstado.isPresent()) {
            EstadoDetalleTransporte existente = optionalEstado.get();
            existente.setEstado(dto.getEstado()); // Solo actualiza el estado
            return estadoDetalleTransporteRepository.save(existente);
        }

        // Si no existe, crea uno nuevo
        DetalleTransporte detalle = new DetalleTransporte();
        detalle.setId(Long.valueOf(detalleId));

        EstadoDetalleTransporte nuevo = new EstadoDetalleTransporte();
        nuevo.setDetalleTransporte(detalle);
        nuevo.setEstado(dto.getEstado());
        nuevo.setFechaCreacion(LocalDateTime.now());

        return estadoDetalleTransporteRepository.save(nuevo);
    }

    private EstadoDetalleTransporteDTO buildEstadoDTO(EstadoDetalleTransporte estado) {
        DetalleTransporte detalle = detalleTransporteRepository.findById(estado.getDetalleTransporte().getId()).orElse(null);
        if (detalle == null) return null;

        EstadoDetalleTransporteDTO dto = new EstadoDetalleTransporteDTO();

        dto.setId(estado.getId());
        dto.setFechaCreacion(estado.getFechaCreacion().toString());
        dto.setCantidad_estibaje(detalle.getCantidadEstibaje());
        dto.setEstado(estado.getEstado());
        dto.setTransporte(detalle.getId().intValue());
        dto.setNumOrden(detalle.getNumOrden());
        dto.setPago(detalle.getPago());
        dto.setTipoServicio(detalle.getTipoServicio());
        dto.setTipo_unidad(detalle.getUnidad() != null ? detalle.getUnidad().getTipo() : "Sin unidad");

        dto.setUsername(detalle.getUsuario().getNombre() + " " + detalle.getUsuario().getApellido());
        dto.setEmail(detalle.getUsuario().getNombreUsuario());
        dto.setTelf(detalle.getUsuario().getTelefono());

        // Coordenadas
        dto.setLatitudO(detalle.getDirOrigen().getLatitud());
        dto.setLongitudO(detalle.getDirOrigen().getLongitud());
        dto.setLatitudD(detalle.getDirDestino().getLatitud());
        dto.setLongitudD(detalle.getDirDestino().getLongitud());

        return dto;
    }



}
