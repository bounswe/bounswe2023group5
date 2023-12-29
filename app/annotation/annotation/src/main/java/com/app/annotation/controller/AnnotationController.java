package com.app.annotation.controller;

import com.app.annotation.service.AnnotationService;
import com.app.annotation.dto.request.CreateAnnotationRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/annotation")
public class AnnotationController {

    private final AnnotationService annotationService;

    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAnnotation(@RequestBody CreateAnnotationRequestDto dto) {
        Map<String, Object> annotation = annotationService.createAnnotation(dto);
        return ResponseEntity.ok(annotation);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteAnnotation(@RequestParam String id) {
        boolean isDeleted = annotationService.deleteAnnotation(id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(true);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateAnnotation(@RequestBody CreateAnnotationRequestDto dto) {
        Map<String, Object> annotation = annotationService.updateAnnotation(dto);
        if(annotation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(annotation);
    }

    @GetMapping("/get-source-annotations")
    public ResponseEntity<List<Map<String, Object>>> getAnnotation(@RequestParam String source) {
        List<Map<String, Object>> annotations = annotationService.getAnnotations(source);
        if(annotations == null || annotations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(annotations);
    }

    @GetMapping("/get-image-annotations")
    public ResponseEntity<List<Map<String, Object>>> getImageAnnotations(@RequestParam String source) {
        List<Map<String, Object>> annotations = annotationService.getImageAnnotations(source);
        if(annotations == null || annotations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(annotations);
    }
}
