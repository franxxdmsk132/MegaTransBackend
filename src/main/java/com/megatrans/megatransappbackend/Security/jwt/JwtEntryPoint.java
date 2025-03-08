package com.megatrans.megatransappbackend.Security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        String errorMessage = "No autorizado";

        // Verificar si el error es una excepción de JWT (token malformado o expirado)
        if (authException.getCause() instanceof ExpiredJwtException) {
            errorMessage = "Token expirado. Por favor, inicie sesión nuevamente.";
        } else if (authException.getCause() instanceof MalformedJwtException) {
            errorMessage = "Token malformado. Verifique el formato del token.";
        } else {
            errorMessage = "Autenticación fallida: " + authException.getMessage();
        }

        // Agregar un log con el error de autenticación
        logger.error("Autenticación fallida: " + errorMessage);

        // Enviar respuesta 401 con el mensaje de error específico
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
    }
}
