package com.app.annotation.controller;

import com.app.annotation.service.AnnotationService;
import com.app.annotation.dto.request.CreateAnnotationRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
