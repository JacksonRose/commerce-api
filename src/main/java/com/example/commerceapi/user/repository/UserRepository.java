package com.example.commerceapi.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.commerceapi.user.document.UserDocument;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String>{
    Optional<UserDocument> findByEmail(String email);
    
}
