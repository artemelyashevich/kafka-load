package com.elyashevich.producer_statistics.service;

import com.elyashevich.producer_statistics.model.TopOrdersByCategoryQuantityAggregation;
import com.elyashevich.producer_statistics.model.TopOrdersByQuantityAggregation;

import java.util.List;

public interface ProducerStatisticsService {

    List<TopOrdersByCategoryQuantityAggregation> findTopOrdersByCategoryRevenue(String collectionName);

    List<TopOrdersByQuantityAggregation> findTopOrdersByQuantityRevenue(String collectionName);
}
