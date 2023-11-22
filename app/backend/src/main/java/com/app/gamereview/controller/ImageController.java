package com.app.gamereview.controller;

import com.app.gamereview.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class ImageController {

	private final FileStorageService fileService;
	@Value("${image.base-directory}")
	private String imageBaseDirectory;

	public ImageController(FileStorageService fileService) {
		this.fileService = fileService;
	}

	@GetMapping("/api/{folder}/{fileName:.+}")
	public ResponseEntity<Resource> serveImage(@PathVariable String folder, @PathVariable String fileName) {
		try {
			File imageFile = new File(imageBaseDirectory + folder + File.separator + fileName);

			if (imageFile.exists() && imageFile.isFile()) {
				Resource resource = new FileSystemResource(imageFile);
				return ResponseEntity.ok()
					.contentLength(imageFile.length())
					.contentType(MediaType.IMAGE_PNG) // Set appropriate content type
														// (e.g., IMAGE_PNG, IMAGE_JPEG)
					.body(resource);
			}
			else {
				// Handle resource not found error
				return ResponseEntity.notFound().build();
			}
		}
		catch (Exception e) {
			// Handle exceptions, e.g., file not found
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping("/api/image/upload")
	public ResponseEntity<String> uploadImage(@RequestParam String folder, @RequestPart MultipartFile image) throws IOException {
		return ResponseEntity.ok(folder + "/" + fileService.storeFile(image, folder));
	}

}
