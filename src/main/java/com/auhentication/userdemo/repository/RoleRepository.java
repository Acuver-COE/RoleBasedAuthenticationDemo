package com.auhentication.userdemo.repository;

import com.auhentication.userdemo.model.Role;
import com.auhentication.userdemo.model.Rolevalues;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveMongoRepository<Role, String> {
    Mono<Role> findByName(Rolevalues name);
}
