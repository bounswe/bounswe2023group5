package com.app.gamereview.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.core.io.Resource;


import java.io.File;

@Controller
public class ImageController {

    private static final String IMAGE_DIRECTORY = "images/";

    @GetMapping("/images/{folder}/{fileName:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String folder, @PathVariable String fileName) {
        try {
            File imageFile = new File(IMAGE_DIRECTORY + folder + File.separator + fileName);

            if (imageFile.exists() && imageFile.isFile()) {
                Resource resource = new FileSystemResource(imageFile);
                return ResponseEntity.ok()
                        .contentLength(imageFile.length())
                        .contentType(MediaType.IMAGE_PNG) // Set appropriate content type (e.g., IMAGE_PNG, IMAGE_JPEG)
                        .body(resource);
            } else {
                // Handle resource not found error
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle exceptions, e.g., file not found
            return ResponseEntity.status(500).build();
        }
    }
}