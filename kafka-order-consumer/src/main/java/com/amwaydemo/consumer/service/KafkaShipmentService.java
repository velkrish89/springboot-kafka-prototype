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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class KafkaShipmentService {

    private static Logger logger = LoggerFactory.getLogger(KafkaShipmentService.class);

    @Autowired
    private ShipmentRepository shipmentRepository;

    public Optional<Shipment> getShipmentByOrderId(long orderId) {

        Optional<Shipment> shipment = shipmentRepository.findByOrderId(orderId);

        return shipment;
    }

    public List<Shipment> getOrdersByShipmentStatus(String status) {

        List<Shipment> orders = null;

        switch(status) {

            case "NOT_STARTED":
                orders = shipmentRepository.findByShippingStatus(ShippingStatus.NOT_STARTED);
                break;

            case "INPROGRESS":
                orders = shipmentRepository.findByShippingStatus(ShippingStatus.INPROGRESS);
                break;

            case "SHIPPED":
                orders = shipmentRepository.findByShippingStatus(ShippingStatus.SHIPPED);
                break;

            case "DELIVERED":
                orders = shipmentRepository.findByShippingStatus(ShippingStatus.DELIVERED);
                break;

            default:
                orders = Collections.EMPTY_LIST;
                break;
        }
        return orders;
    }


    public Shipment updateShippingStatus(long orderId, String status) {

        Optional<Shipment> shipment = shipmentRepository.findByOrderId(orderId);

        ShippingStatus shipStatus = ShippingStatus.valueOf(status);

        Shipment result = null;
        Shipment current = shipment.orElseThrow(NoOrderFoundException::new);
        current.setShippingStatus(shipStatus);
        current.setLastUpdated(new Date().toString());
        result = shipmentRepository.save(shipment.get());

        return result;
    }
}
