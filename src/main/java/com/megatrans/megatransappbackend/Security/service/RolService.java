package com.megatrans.megatransappbackend.Security.service;


import com.megatrans.megatransappbackend.Security.entity.Rol;
import com.megatrans.megatransappbackend.Security.enums.RolNombre;
import com.megatrans.megatransappbackend.Security.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepository.findByRolNombre(rolNombre);
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
}
