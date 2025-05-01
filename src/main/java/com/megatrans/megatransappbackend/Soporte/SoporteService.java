package com.megatrans.megatransappbackend.Soporte;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SoporteService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarSoporte(SoporteDTO soporte) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("tapiafranklin666@gmail.com"); // o tu correo
        message.setSubject("Soporte: " + soporte.getTema());
        message.setText(
                "Nombre: " + soporte.getNombreUsuario() + "\n" +
                        "Email: " + soporte.getEmailUsuario() + "\n\n" +
                        "Descripción:\n" + soporte.getDescripcion()
        );
        javaMailSender.send(message);
    }
}
