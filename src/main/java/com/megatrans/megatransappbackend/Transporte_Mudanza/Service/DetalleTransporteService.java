package com.megatrans.megatransappbackend.Transporte_Mudanza.Service;

import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.repository.UsuarioRepository;
import com.megatrans.megatransappbackend.Transporte_Mudanza.DTO.DetalleTransporteDTO;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.Direccion;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Repository.DetalleTransporteRepository;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Repository.DireccionRepository;
import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import com.megatrans.megatransappbackend.Unidad.Repository.UnidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleTransporteService {
    @Autowired
    private DetalleTransporteRepository detalleTransporteRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private UnidadRepository unidadRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<DetalleTransporte> listar() {
        return detalleTransporteRepository.findAll();
    }

    public DetalleTransporte guardar(DetalleTransporteDTO dto) {
        // Crear y guardar la dirección de origen
        Direccion origen = new Direccion();
        origen.setBarrio(dto.getDireccionOrigen().getBarrio());
        origen.setCallePrincipal(dto.getDireccionOrigen().getCallePrincipal());
        origen.setCalleSecundaria(dto.getDireccionOrigen().getCalleSecundaria());
        origen.setCiudad(dto.getDireccionOrigen().getCiudad());
        origen.setLatitud(dto.getDireccionOrigen().getLatitud());
        origen.setLongitud(dto.getDireccionOrigen().getLongitud());
        origen.setReferencia(dto.getDireccionOrigen().getReferencia());
        origen.setTelefono(dto.getDireccionOrigen().getTelefono());
        origen = direccionRepository.save(origen);

        // Crear y guardar la dirección de destino
        Direccion destino = new Direccion();
        destino.setBarrio(dto.getDireccionDestino().getBarrio());
        destino.setCallePrincipal(dto.getDireccionDestino().getCallePrincipal());
        destino.setCalleSecundaria(dto.getDireccionDestino().getCalleSecundaria());
        destino.setCiudad(dto.getDireccionDestino().getCiudad());
        destino.setLatitud(dto.getDireccionDestino().getLatitud());
        destino.setLongitud(dto.getDireccionDestino().getLongitud());
        destino.setReferencia(dto.getDireccionDestino().getReferencia());
        destino.setTelefono(dto.getDireccionDestino().getTelefono());
        destino = direccionRepository.save(destino);

        // Buscar la unidad y el usuario
        Unidad unidad = unidadRepository.findById(dto.getUnidadId())
                .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear y guardar el detalle de transporte
        DetalleTransporte detalle = new DetalleTransporte();
        detalle.setTipoServicio(dto.getTipoServicio().name());
        detalle.setCantidadEstibaje(dto.getCantidadEstibaje());
        detalle.setDescripcionProducto(dto.getDescripcionProducto());
        detalle.setEstado(dto.getEstado().name());
        detalle.setEstibaje(dto.getEstibaje());
        detalle.setFecha(dto.getFecha());
        detalle.setNumOrden(dto.getNumOrden());
        detalle.setPago(dto.getPago().name());
        detalle.setDirOrigen(origen);
        detalle.setDirDestino(destino);
        detalle.setUnidad(unidad);
        detalle.setUsuario(usuario);

        return detalleTransporteRepository.save(detalle);
    }




    public Optional<DetalleTransporte> obtenerPorId(Long id) {
        return detalleTransporteRepository.findById(id);
    }

    public void eliminar(Long id) {
        detalleTransporteRepository.deleteById(id);
    }
}
