package com.auhentication.userdemo.repository;

import com.auhentication.userdemo.payload.TokenClass;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface TokenRepository extends ReactiveMongoRepository<TokenClass, String> {
    Mono<TokenClass> findByUserName(String userName);
}
