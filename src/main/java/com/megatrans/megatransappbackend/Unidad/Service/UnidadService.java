package com.megatrans.megatransappbackend.Unidad.Service;

import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import com.megatrans.megatransappbackend.Unidad.Repository.UnidadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UnidadService {

    @Autowired
    UnidadRepository unidadRepository;

    public List<Unidad> getAllUnidades() {return unidadRepository.findAll();}

    public Optional<Unidad> getUnidadById(int id) {return unidadRepository.findById(id);}

    public void addUnidad(Unidad unidad) {
        unidadRepository.save(unidad);}

    public void deleteUnidad(int id) {
        unidadRepository.deleteById(id);}

    public boolean existUnidad(int id) {return unidadRepository.existsById(id);}

    public boolean existsById(int id){
        return unidadRepository.existsById(id);
    }
}
