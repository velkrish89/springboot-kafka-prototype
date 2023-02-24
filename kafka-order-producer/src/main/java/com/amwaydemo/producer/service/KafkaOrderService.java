package com.amwaydemo.producer.service;

import com.amwaydemo.producer.model.Order;
import com.amwaydemo.producer.repository.OrderRepository;
import com.amwaydemo.producer.utils.OrderStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class KafkaOrderService {
    private static Logger logger = LoggerFactory.getLogger(KafkaOrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    public List<Order> publishOrders(List<Order> orders) {

         ExecutorService executorService = Executors.newFixedThreadPool(150);
        try {
            //Passing orders to thread pool and call parallel stream
            return executorService.submit(() ->
                    orders.stream().parallel()
                            .map((order) -> insertAndPublishOrder(order))
                            .collect(Collectors.toList())
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

    public Order insertAndPublishOrder(Order order) {
        order.setOrderStatus(OrderStatus.CREATED);
        Order result = orderRepository.save(order);
        try {
            //Serialization
            String orderAdded = new ObjectMapper().writeValueAsString(result).toString();
            logger.info("Order added to Db: " + orderAdded);

            //Pushing the order message to topic
            kafkaMessageProducer.publishMessage(orderAdded);
            logger.info("Order Message published to kafka topic: " + orderAdded);

        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Order createOrder(Order order) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(order.getOrderDate());
            order.setOrderDate(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Order result = orderRepository.save(order);
        try {
            String orderAdded = new ObjectMapper().writeValueAsString(result).toString();
            logger.info("Order added to Db: " + orderAdded);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Optional<Order> getOrderById(long orderId) {
        Optional<Order> result = orderRepository.findById(orderId);
        logger.info("find order by Id : " + "orderId and result: " + result);
        return result;
    }
}
