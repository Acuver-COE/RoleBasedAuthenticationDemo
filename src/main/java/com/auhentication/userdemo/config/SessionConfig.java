package com.auhentication.userdemo.config;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.session.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;

//@EnableSpringWebSession
public class SessionConfig {

    private HazelcastInstance hazelcastInstance;
    SessionConfig(HazelcastInstance hazelcastInstance){
        this.hazelcastInstance = hazelcastInstance;
    }


    @Bean
    public ReactiveSessionRepository reactiveSessionRepository() {
        final IMap<String, Session> map = hazelcastInstance.getMap(StringConstants.MAP_CONFIG_NAME);
        return new ReactiveMapSessionRepository (map);
    }
}
