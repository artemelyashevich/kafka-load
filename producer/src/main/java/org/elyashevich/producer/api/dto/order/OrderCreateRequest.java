package org.elyashevich.producer.api.dto.order;

import java.math.BigDecimal;

public record OrderCreateRequest(
        String orderId,
        String productName,
        String categoryName,
        BigDecimal price,
        Integer quantity,
        String status,
        String customerId,
        String eventType
) {}