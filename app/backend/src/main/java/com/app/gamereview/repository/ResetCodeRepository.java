package com.app.gamereview.repository;

import com.app.gamereview.model.ResetCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResetCodeRepository extends MongoRepository<ResetCode, String> {
    ResetCode findByUserId(String userId);
    ResetCode findByCode(String code);
    void deleteByUserId(String userId);
}