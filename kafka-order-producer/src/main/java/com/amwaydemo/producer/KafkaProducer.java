package com.amwaydemo.producer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gopalakrishnanv
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Order Service API",
        version = "1.0",
        description = "Microservices to publish messages to kafka topic and fetch orders from db"
))
public class KafkaProducer {

    public static void main(String[] args) {
        SpringApplication.run(KafkaProducer.class, args);
    }

}
