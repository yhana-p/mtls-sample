package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController("/")
public class Controller {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    public String get() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://localhost:8444/", String.class);
        return forEntity.getBody();
    }
}


