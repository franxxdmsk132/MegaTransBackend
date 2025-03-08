package com.megatrans.megatransappbackend.Log;

import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class AuditoriaInterceptor implements HandlerInterceptor {

    private final LogAuditoriaRepository logAuditoriaRepository;
    private final UsuarioRepository usuarioRepository; // Asegúrate de tener este repositorio para obtener nombre y apellido.

    public AuditoriaInterceptor(LogAuditoriaRepository logAuditoriaRepository, UsuarioRepository usuarioRepository) {
        this.logAuditoriaRepository = logAuditoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String usuarioEmail = obtenerUsuarioActual();

//        // Si el email está vacío, se asigna un valor por defecto
//        if (usuarioEmail.isEmpty() || usuarioEmail.equals("ANÓNIMO")) {
//            usuarioEmail = "Desconocido";
//        }

        // Buscar al usuario en la base de datos
//        Usuario usuario = usuarioRepository.findByNombreUsuario(usuarioEmail).orElse(null);
//        if (usuario == null) {
//            // Si no se encuentra el usuario, asignar un valor por defecto
//            usuario = new Usuario();
//            usuario.setNombre("Desconocido");
//            usuario.setApellido("Desconocido");
//        }

        // Guardar el log de auditoría
        LogAuditoria log = new LogAuditoria(
                request.getMethod(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        logAuditoriaRepository.save(log);
        return true;
    }


}
