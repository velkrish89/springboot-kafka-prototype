package com.amwaydemo.consumer.service;

import com.amwaydemo.consumer.exception.NoOrderFoundException;
import com.amwaydemo.consumer.model.Order;
import com.amwaydemo.consumer.model.Shipment;
import com.amwaydemo.consumer.repository.ShipmentRepository;
import com.amwaydemo.consumer.utils.ShippingStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KafkaConsumerService {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private ShipmentRepository shipmentRepository;

    @KafkaListener(topics = {"${spring.kafka.topic.name}"},
            groupId = "${spring.kafka.consumer.group-id}" )
    public void consumeOrder(String orderMessage) {

        ExecutorService executorService = Executors.newFixedThreadPool(150);
        try {
            //Passing order messages to thread pool
            executorService.submit(() ->
                    createShipment(orderMessage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

    public void createShipment(String orderMessage) {
        logger.info("Order message from topic: " + orderMessage.toString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                true);
        Order order = null;
        try {
            //Deserialization
            order = mapper.readValue(orderMessage, Order.class);

            //Create new shipment
            Shipment shipment = new Shipment();
            shipment.setOrderId(order.getOrderId());
            shipment.setCustomerEmail(order.getCustomerEmail());
            shipment.setOrderDate(order.getOrderDate());
            shipment.setOrderStatus(order.getOrderStatus());
            shipment.setPrice(order.getPrice());
            shipment.setProducts(order.getProducts());
            shipment.setShippingStatus(ShippingStatus.NOT_STARTED);
            shipment.setLastUpdated(new Date().toString());

            //Add shipment to db
            Shipment result = shipmentRepository.save(shipment);
            logger.info("Shipment added to db: "+ mapper.writeValueAsString(result).toString());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
