package org.elyashevich.consumer.service;

import org.elyashevich.consumer.domain.entity.Order;

public interface OrderService {

    Order create(String producerNAme, String topicName, Order order);

    Order update(Order order);

    Order findById(String id);

    void cancel(Order order);

    Order complete(Order order);
}
