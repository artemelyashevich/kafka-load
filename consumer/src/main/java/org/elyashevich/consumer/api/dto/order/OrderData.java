package org.elyashevich.consumer.api.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderData {
    private String orderId;
    private String productName;
    private String categoryId;
    private String categoryName;
    private BigDecimal price;
    private Integer quantity;
    private String status;
    private String customerId;
}