package com.app.annotation.service;

import com.app.annotation.dto.request.SelectorDto;
import com.app.annotation.dto.request.TargetDto;
import com.app.annotation.exception.BadRequestException;
import com.app.annotation.model.*;
import com.app.annotation.repository.AnnotationRepository;
import com.app.annotation.dto.request.CreateAnnotationRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        Optional<Annotation> prevAnnotation = annotationRepository.findById(dto.getId());

        if (prevAnnotation.isPresent()) {
            throw new BadRequestException("There is an annotation with same id.");
        }

        Annotation annotationToCreate = modelMapper.map(dto, Annotation.class);

        List<Selector> selectorList = new ArrayList<>();
        for (SelectorDto s : dto.getTarget().getSelector()) {
            switch (s.getType()) {
                case "TextQuoteSelector" -> selectorList.add(modelMapper.map(s, TextQuoteSelector.class));
                case "TextPositionSelector" -> selectorList.add(modelMapper.map(s, TextPositionSelector.class));
                case "FragmentSelector" -> selectorList.add(modelMapper.map(s, FragmentSelector.class));
            }
            //TODO add more selectors if implemented in the future
        }

        annotationToCreate.getTarget().setSelector(selectorList);

        return annotationRepository.save(annotationToCreate).toJSON();
    }

    public boolean deleteAnnotation(String id) {
        if(!annotationRepository.existsById(id)) {
            return false;
        }
        annotationRepository.deleteById(id);
        return true;
    }

    public Map<String, Object> updateAnnotation(CreateAnnotationRequestDto dto) {
        Optional<Annotation> annotation = annotationRepository.findById(dto.getId());
        if (annotation.isEmpty()) {
            return null;
        }
        Annotation annotationToUpdate = annotation.get();

        Annotation updatedAnnotation = modelMapper.map(dto, Annotation.class);
        List<Selector> selectorList = new ArrayList<>();
        for (SelectorDto s : dto.getTarget().getSelector()) {
            switch (s.getType()) {
                case "TextQuoteSelector" -> selectorList.add(modelMapper.map(s, TextQuoteSelector.class));
                case "TextPositionSelector" -> selectorList.add(modelMapper.map(s, TextPositionSelector.class));
                case "FragmentSelector" -> selectorList.add(modelMapper.map(s, FragmentSelector.class));
            }
            //TODO add more selectors if implemented in the future
        }
        updatedAnnotation.getTarget().setSelector(selectorList);

        annotationToUpdate.setTarget(updatedAnnotation.getTarget());
        annotationToUpdate.setBody(updatedAnnotation.getBody());
        annotationToUpdate.setCreated(updatedAnnotation.getCreated());
        annotationToUpdate.setCreator(updatedAnnotation.getCreator());
        annotationToUpdate.setMotivation(updatedAnnotation.getMotivation());

        return annotationRepository.save(annotationToUpdate).toJSON();
    }

    public List<Map<String, Object>> getAnnotations(String source) {
        List<Annotation> annotations = annotationRepository.findAllByTargetSource(source);
        List<Map<String, Object>> jsonAnnotations = new ArrayList<>();
        for (Annotation a : annotations) {

            List<Selector> selectors = a.getTarget().getSelector();

            boolean haveTextSelectors = true;

            for (Selector s : selectors) {
                if (!s.getType().equals("TextQuoteSelector") && !s.getType().equals("TextPositionSelector")) {
                    haveTextSelectors = false;
                    break;
                }

                // TODO change logic as more selectors are implemented in the future
            }

            if (!haveTextSelectors) {
                continue;
            }

            jsonAnnotations.add(a.toJSON());
        }
        return jsonAnnotations;
    }

    public List<Map<String, Object>> getImageAnnotations(String source) {
        List<Annotation> annotations = annotationRepository.findAllByTargetSource(source);
        List<Map<String, Object>> jsonAnnotations = new ArrayList<>();
        for (Annotation a : annotations) {

            if (a.getTarget().getSelector().size() == 1 && a.getTarget().getSelector().get(0).getType().equals("FragmentSelector")) {
                jsonAnnotations.add(a.toJSON());
            }
            // TODO change logic if more image selectors are implemented in the future
        }
        return jsonAnnotations;
    }
}
