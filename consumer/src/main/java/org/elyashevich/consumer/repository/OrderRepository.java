package org.elyashevich.consumer.repository;

import org.elyashevich.consumer.domain.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
