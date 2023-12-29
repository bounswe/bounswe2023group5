package com.app.annotation.repository;

import com.app.annotation.model.Annotation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnnotationRepository extends MongoRepository<Annotation, String> {
    List<Annotation> findAllByTargetSource(String source);
}
