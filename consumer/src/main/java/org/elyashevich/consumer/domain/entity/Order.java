package org.elyashevich.consumer.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Order {

    @Id
    private String id;

    @DBRef
    private Category category;

    @Field
    private String productName;

    @Field
    private BigDecimal price;

    @Field
    private Integer quantity;

    @Field
    private OrderStatus status;

    @CreatedDate
    private LocalDateTime createdAt;
}
