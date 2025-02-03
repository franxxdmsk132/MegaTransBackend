package com.megatrans.megatransappbackend.Pedido.Service;

import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import com.megatrans.megatransappbackend.Unidad.Repository.UnidadRepository;
import com.megatrans.megatransappbackend.Pedido.DTO.PedidosDto;
import com.megatrans.megatransappbackend.Pedido.Entity.Pedidos;
import com.megatrans.megatransappbackend.Pedido.Repository.PedidosRepository;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidosService {


    @Autowired
    private PedidosRepository pedidosRepository;
    @Autowired
    private UnidadRepository unidadRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<PedidosDto> findAll() {
        return pedidosRepository.findAll()
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }
    public PedidosDto obtenerPedidoPorId(int id) {
        Pedidos pedido = pedidosRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return convertirEntityADto(pedido);
    }
    public PedidosDto crearPedido(PedidosDto pedidoDto) {
        Pedidos pedido = convertirDtoAEntity(pedidoDto);
        pedido.setFecha_pededo(java.time.LocalDateTime.now());
        pedido.setEstado("Pendiente");
        pedido = pedidosRepository.save(pedido);
        return convertirEntityADto(pedido);
    }

    public PedidosDto actualizarPedido(int id, PedidosDto pedidoDto) {
        Pedidos pedido = pedidosRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Recuperar el camión por ID
        Unidad camion = unidadRepository.findById(pedidoDto.getCamionId())
                .orElseThrow(() -> new RuntimeException("Camión no encontrado"));

        pedido.setDireccion(pedidoDto.getDireccion());
        pedido.setLatitud(pedidoDto.getLatitud());
        pedido.setLongitud(pedidoDto.getLongitud());
        pedido.setEstado(pedidoDto.getEstado());
        pedido.setUnidad(camion); // Asignar el camión recuperado

        pedido = pedidosRepository.save(pedido);
        return convertirEntityADto(pedido);
    }

    public void eliminarPedido(int id) {
        if (!pedidosRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado");
        }
        pedidosRepository.deleteById(id);
    }

    private PedidosDto convertirEntityADto(Pedidos pedido) {
        PedidosDto dto = new PedidosDto();
        dto.setId(pedido.getId());
        dto.setUsuarioId(pedido.getUsuario().getId());
        dto.setCamionId(pedido.getUnidad().getId());
        dto.setDireccion(pedido.getDireccion());
        dto.setLatitud(pedido.getLatitud());
        dto.setLongitud(pedido.getLongitud());
        dto.setFechaPedido(pedido.getFecha_pededo());
        dto.setEstado(pedido.getEstado());
        return dto;
    }

    private Pedidos convertirDtoAEntity(PedidosDto dto) {
        Pedidos pedido = new Pedidos();

        // Buscar el usuario por ID
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Asociar usuario y camión
        pedido.setUsuario(usuario);
        // Buscar el camión por ID
        Unidad unidad = unidadRepository.findById((Integer) dto.getCamionId())
                .orElseThrow(() -> new RuntimeException("Camión no encontrado"));

        pedido.setUnidad(unidad);
        pedido.setDireccion(dto.getDireccion());
        pedido.setLatitud(dto.getLatitud());
        pedido.setLongitud(dto.getLongitud());
        pedido.setFecha_pededo(dto.getFechaPedido());
        pedido.setEstado(dto.getEstado());

        return pedido;
    }
}
