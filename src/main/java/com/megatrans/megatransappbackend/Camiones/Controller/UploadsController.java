package com.megatrans.megatransappbackend.Camiones.Controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/imagen")
public class UploadsController {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @GetMapping("/{imageName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Path filePath = fileStorageLocation.resolve(imageName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            System.out.println("File Path: " + filePath.toString()); // Debugging line
            System.out.println("Resource Exists: " + resource.exists()); // Debugging line

            if (resource.exists() || resource.isReadable()) {
                String contentType = "application/octet-stream"; // Default content type
                try {
                    contentType = resource.getFile().toURI().toURL().openConnection().getContentType();
                } catch (IOException e) {
                    // Log the exception or handle it
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



}