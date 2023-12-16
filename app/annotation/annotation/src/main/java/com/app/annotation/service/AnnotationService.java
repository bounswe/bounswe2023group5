package com.app.annotation.service;

import com.app.annotation.model.Annotation;
import com.app.annotation.repository.AnnotationRepository;
import com.app.annotation.dto.request.CreateAnnotationRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnnotationService {

    private final AnnotationRepository annotationRepository;

    private final ModelMapper modelMapper;


    public AnnotationService(AnnotationRepository annotationRepository, ModelMapper modelMapper) {
        this.annotationRepository = annotationRepository;
        this.modelMapper = modelMapper;
    }

    public Map<String, Object> createAnnotation(CreateAnnotationRequestDto dto) {

        Annotation annotationToCreate = modelMapper.map(dto, Annotation.class);

        return annotationRepository.save(annotationToCreate).toJSON();
    }
}
