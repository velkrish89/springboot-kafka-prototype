package com.amwaydemo.producer.controller;

import com.amwaydemo.producer.exception.NoOrderFoundException;
import com.amwaydemo.producer.model.Order;
import com.amwaydemo.producer.service.KafkaMessageProducer;
import com.amwaydemo.producer.service.KafkaOrderService;
import com.amwaydemo.producer.utils.ExtractOrdersFromFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/order-service")
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;
    @Autowired
    private ExtractOrdersFromFile extractOrdersFromFile;
    @Autowired
    private KafkaOrderService kafkaOrderService;

    @Operation(summary = "To add bulk of orders in DB and push the same to kafka topic")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",
                    description = "Orders added to DB and pushed to google kafka topic"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error")
    })
    @PostMapping(value = "/bulk",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> publishBulkOrders(@RequestParam("orders") MultipartFile file) {
        try {
            long startTime = System.currentTimeMillis();
            List<Order> orderInput = extractOrdersFromFile.getOrderDetailsFromJSON(file.getBytes());
            List<Order> ordersAdded = kafkaOrderService.publishOrders(orderInput);
            long endTime = System.currentTimeMillis();

            JSONObject bodyObject = new JSONObject();
            bodyObject.put("Status", "SUCCESS");
            bodyObject.put("Records Processed", ordersAdded.size());
            bodyObject.put("Process Time", (endTime-startTime)+" ms");

            return new ResponseEntity<>("Orders added to DB - "+ bodyObject, HttpStatus.CREATED);
        } catch (Exception e) {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("Status", "FAILED");
            bodyObject.put("ERROR", e.getMessage());
            return new ResponseEntity<>("Error inserting orders - " + bodyObject , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "To add an order in DB and push the order to kafka topic")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "201",
                    description = "Added order to DB and pushed the order to google kafka topic"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error")
    })
    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {

        Order result = kafkaOrderService.createOrder(order);

        ObjectMapper mapper = new ObjectMapper();
        String orderMessage = null;

        try {
            orderMessage = mapper.writeValueAsString(result).toString();

            kafkaMessageProducer.publishMessage(orderMessage);
            logger.info("Order Message published to kafka topic: " + orderMessage);


            return new ResponseEntity<Order>(result, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error inserting order: " + e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "To fetch an order by order id from DB")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",
                    description = "Fetched order by order id from DB"),
            @ApiResponse(responseCode = "404",
                    description = "No records found with given order id"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error")
    })
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable long orderId) {

            Order order = kafkaOrderService
                    .getOrderById(orderId)
                    .orElseThrow(() -> new NoOrderFoundException());

            return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
}
