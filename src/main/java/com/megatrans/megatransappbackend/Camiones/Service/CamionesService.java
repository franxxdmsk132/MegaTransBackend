package com.megatrans.megatransappbackend.Camiones.Service;

import com.megatrans.megatransappbackend.Camiones.Entity.Camiones;
import com.megatrans.megatransappbackend.Camiones.Repository.CamionesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CamionesService {

    @Autowired
    CamionesRepository camionesRepository;

    public List<Camiones> getAllCamiones() {return camionesRepository.findAll();}

    public Optional<Camiones> getCamionById(int id) {return camionesRepository.findById(id);}

    public void addCamion(Camiones camion) {camionesRepository.save(camion);}

    public void deleteCamion(int id) {camionesRepository.deleteById(id);}

    public boolean existCamion(String placa) {return camionesRepository.existsByPlaca(placa);}

    public boolean existsById(int id){
        return camionesRepository.existsById(id);
    }
}
