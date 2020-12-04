package com.auhentication.userdemo.repository;

import com.auhentication.userdemo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByEmailId(String email);
    Mono<Boolean> existsByEmailId(String email);
}
