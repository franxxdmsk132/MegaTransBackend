package com.megatrans.megatransappbackend.websocket;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    /**
     * Inicializa Firebase con las credenciales del archivo JSON.
     * Este método se ejecuta al iniciar la aplicación.
     */
    @Value("${firebase.config.path}")
    private String FIREBASE_CONFIG_PATH;

    @PostConstruct
    public void initFirebase() {
        try (InputStream serviceAccount =
                     getClass().getClassLoader().getResourceAsStream(FIREBASE_CONFIG_PATH)) {

            assert serviceAccount != null;
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (IOException e) {
            e.printStackTrace(); // <-- Here you can change to a logger
        }
    }
}
