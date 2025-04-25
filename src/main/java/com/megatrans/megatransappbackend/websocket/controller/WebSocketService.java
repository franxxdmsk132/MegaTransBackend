package com.megatrans.megatransappbackend.websocket.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.megatrans.megatransappbackend.Encomiendas.DTO.EstadoEncomiendaDTO;
import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Encomiendas.Entity.EstadoEncomienda;
import com.megatrans.megatransappbackend.Encomiendas.Repository.EstadoEncomiendaRepository;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.enums.RolNombre;
import com.megatrans.megatransappbackend.Security.repository.UsuarioRepository;
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

@Controller
@RequiredArgsConstructor
public class WebSocketService {

    private final FmcService fmcService;

    private final UsuarioRepository usuarioRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final EstadoEncomiendaRepository estadoEncomiendaRepository;

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

        fmcService.sendNotificationToAllTokens(tokens, "Solicitud de transporte", estadoEncomiendaDTO.toJson());

        simpMessagingTemplate.convertAndSend("/specific/request", estadoEncomiendaDTO);
    }

    @MessageMapping("/admin/response")
    @Transactional
    public void adminAnswer(EstadoEncomiendaDTO estadoEncomiendaDTO) {
        updateEncomienda(estadoEncomiendaDTO);
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
        ;

        existingEstadoEncomienda.setEstado(estadoEncomiendaDTO.getEstado());

        return estadoEncomiendaRepository.save(existingEstadoEncomienda);
    }
}
