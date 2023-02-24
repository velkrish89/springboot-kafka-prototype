package com.amwaydemo.consumer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Shipment Service API",
        version = "1.0",
        description = "Microservices to consume messages from kafka topic, create shipment and update the shipment status"
))
public class KafkaOrderConsumer {

    public static void main(String[] args) {
        SpringApplication.run(KafkaOrderConsumer.class, args);
    }
}
