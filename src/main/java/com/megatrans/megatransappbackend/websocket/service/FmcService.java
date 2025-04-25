package com.megatrans.megatransappbackend.websocket.service;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FmcService {

    public void sendNotificationToAllTokens(List<String> tokens, String body, String data) throws FirebaseMessagingException {
        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle("MegaTrans")
                        .setBody(body)
                        .build())
                .putData("data", data)
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
    }

    public void sendNotification(String token, String title, String msg) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(msg)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("📬 Notificación enviada: " + response);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar notificación: " + e.getMessage());
        }
    }
}
