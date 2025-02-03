package com.megatrans.megatransappbackend.Pedido.Controller;

import com.megatrans.megatransappbackend.Pedido.DTO.PedidosDto;
import com.megatrans.megatransappbackend.Pedido.Service.PedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "http://localhost:4200")
public class PedidosController {

    @Autowired
    private PedidosService pedidosService;

    // Obtener todos los pedidos
    @GetMapping("/lista")
    public ResponseEntity<List<PedidosDto>> obtenerTodosLosPedidos() {
        List<PedidosDto> pedidos = pedidosService.findAll();
        return ResponseEntity.ok(pedidos);
    }

    // Obtener un pedido por ID
    @GetMapping("/detalle/{id}")
    public ResponseEntity<PedidosDto> obtenerPedidoPorId(@PathVariable int id) {
        PedidosDto pedido = pedidosService.obtenerPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    // Crear un nuevo pedido
    @PostMapping("/crear")
    public ResponseEntity<PedidosDto> crearPedido(@RequestBody PedidosDto pedidoDto) {
        PedidosDto nuevoPedido = pedidosService.crearPedido(pedidoDto);
        return ResponseEntity.ok(nuevoPedido);
    }

    // Actualizar un pedido existente
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PedidosDto> actualizarPedido(@PathVariable int id, @RequestBody PedidosDto pedidoDto) {
        PedidosDto pedidoActualizado = pedidosService.actualizarPedido(id, pedidoDto);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // Eliminar un pedido por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable int id) {
        pedidosService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
