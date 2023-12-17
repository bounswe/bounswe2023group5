package com.app.annotation.repository;

import com.app.annotation.model.Annotation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnnotationRepository extends MongoRepository<Annotation, String> {
}
