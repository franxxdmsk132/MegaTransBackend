package com.megatrans.megatransappbackend.Encomiendas.Service;

import com.megatrans.megatransappbackend.Encomiendas.DTO.ProductoDTO;
import com.megatrans.megatransappbackend.Encomiendas.Entity.Producto;
import com.megatrans.megatransappbackend.Encomiendas.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Crear un nuevo producto
    public Producto create(Producto producto) {
        return productoRepository.save(producto);
    }

    // Obtener todos los productos
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    // Obtener un producto por ID
    public Optional<Producto> findById(Integer id) {
        return productoRepository.findById(id);
    }

    // Eliminar un producto
    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }
}
