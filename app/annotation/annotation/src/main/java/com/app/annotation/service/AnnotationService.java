package com.app.annotation.service;

import com.app.annotation.dto.request.SelectorDto;
import com.app.annotation.dto.request.TargetDto;
import com.app.annotation.model.*;
import com.app.annotation.repository.AnnotationRepository;
import com.app.annotation.dto.request.CreateAnnotationRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnnotationService {

    private final AnnotationRepository annotationRepository;

    private final ModelMapper modelMapper;


    public AnnotationService(AnnotationRepository annotationRepository, ModelMapper modelMapper) {
        this.annotationRepository = annotationRepository;
        this.modelMapper = modelMapper;

        modelMapper.addMappings(new PropertyMap<TargetDto, Target>() {
            @Override
            protected void configure() {
                skip().setSelector(null);
            }
        });
    }

    public Map<String, Object> createAnnotation(CreateAnnotationRequestDto dto) {

        Annotation annotationToCreate = modelMapper.map(dto, Annotation.class);

        List<Selector> selectorList = new ArrayList<>();
        for (SelectorDto s : dto.getTarget().getSelector()) {
            if (s.getExact() != null) {
                System.out.println(s.getType());
                System.out.println(s.getExact());
            }
            if (s.getStart() != null) {
                System.out.println(s.getType());
                System.out.println(s.getStart());
            }
            if (s.getType().equals("TextQuoteSelector")) {
                selectorList.add(modelMapper.map(s, TextQuoteSelector.class));
            } else if (s.getType().equals("TextPositionSelector")) {
                selectorList.add(modelMapper.map(s, TextPositionSelector.class));
            }
            //TODO add more selectors if implemented in the future
        }

        annotationToCreate.getTarget().setSelector(selectorList);

        return annotationRepository.save(annotationToCreate).toJSON();
    }
}
