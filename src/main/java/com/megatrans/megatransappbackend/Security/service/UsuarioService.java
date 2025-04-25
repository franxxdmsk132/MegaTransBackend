package com.megatrans.megatransappbackend.Security.service;


import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.enums.RolNombre;
import com.megatrans.megatransappbackend.Security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }
    public Optional<Usuario> getById(int id){
        return usuarioRepository.findById(id);
    }

    public boolean existsById(int id){
        return usuarioRepository.existsById(id);
    }


    public boolean existsByNombreUsuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }
//    public boolean existsByNombreComercial(String nombreComercial){
//        return usuarioRepository.existsByNombreComercial(nombreComercial);
//    }
    public List<Usuario> getUsuariosByRol(RolNombre rolNombre) {
        return usuarioRepository.findByRoles_RolNombre(rolNombre);
    }

//    public boolean existsByEmail(String email){
//        return usuarioRepository.existsByEmail(email);
//    }

    public void save(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    public void saveTokenFMC(Usuario usuario, String tokenFMC){
        usuario.setTokenFMC(tokenFMC);
        usuarioRepository.save(usuario);
    }

    public void deleteById(Integer id){
        usuarioRepository.deleteById(id);
    }
    // Método para contar el número de usuarios
    public long countUsuarios() {
        return usuarioRepository.count();
    }
}
