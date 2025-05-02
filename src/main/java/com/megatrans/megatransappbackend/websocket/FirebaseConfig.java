package com.megatrans.megatransappbackend.websocket;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.config.path}")
    private String FIREBASE_CONFIG_PATH;

    /**
     * Inicializa Firebase con las credenciales del archivo JSON.
     * Este método se ejecuta al iniciar la aplicación.
     */
    @PostConstruct
    public void initFirebase() {
        try (InputStream serviceAccount =
                     getClass().getClassLoader().getResourceAsStream(FIREBASE_CONFIG_PATH)) {

            if (serviceAccount == null) {
                logger.error("No se encontró el archivo de configuración de Firebase en la ruta: {}", FIREBASE_CONFIG_PATH);
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("✅ Firebase inicializado correctamente.");
            } else {
                logger.info("ℹ️ Firebase ya estaba inicializado.");
            }

        } catch (IOException e) {
            logger.error("❌ Error al inicializar Firebase: {}", e.getMessage(), e);
        }
    }
}
