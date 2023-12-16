package com.app.annotation.service;

import com.app.annotation.model.Annotation;
import com.app.annotation.repository.AnnotationRepository;
import com.app.annotation.dto.request.CreateAnnotationRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnnotationService {

    private final AnnotationRepository annotationRepository;

    private final ModelMapper modelMapper;


    public AnnotationService(AnnotationRepository annotationRepository, ModelMapper modelMapper) {
        this.annotationRepository = annotationRepository;
        this.modelMapper = modelMapper;
    }

    public Annotation createAnnotation(CreateAnnotationRequestDto dto) {
        Annotation annotationToCreate = modelMapper.map(dto, Annotation.class);

        return annotationRepository.save(annotationToCreate);
    }

    private Map<String, Object> convertAnnotationToJsonLd(Annotation annotation) {
        Map<String, Object> annotationJson = new HashMap<>();
        annotationJson.put("@context", "http://www.w3.org/ns/anno.jsonld");
        annotationJson.put("type", "Annotation");
        annotationJson.put("target", annotation.getTarget());
        annotationJson.put("id", annotation.getId());

        return annotationJson;
    }
}
