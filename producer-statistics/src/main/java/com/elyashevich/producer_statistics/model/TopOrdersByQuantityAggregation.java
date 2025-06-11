package com.elyashevich.producer_statistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopOrdersByQuantityAggregation {

    String id;
    Long totalSold;
}
