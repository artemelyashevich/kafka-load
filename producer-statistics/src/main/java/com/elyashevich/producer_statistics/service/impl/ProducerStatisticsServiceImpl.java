package com.elyashevich.producer_statistics.service.impl;

import com.elyashevich.producer_statistics.model.TopOrdersByCategoryQuantityAggregation;
import com.elyashevich.producer_statistics.model.TopOrdersByQuantityAggregation;
import com.elyashevich.producer_statistics.service.ProducerStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerStatisticsServiceImpl implements ProducerStatisticsService {

    public static final String TOTAL_QUANTITY = "totalQuantity";
    public static final String TOTAL_SOLD = "totalSold";
    private final MongoTemplate mongoTemplate;

    @Override
    public List<TopOrdersByCategoryQuantityAggregation> findTopOrdersByCategoryRevenue(String collectionName) {
        var aggregation = Aggregation.newAggregation(
                Aggregation.group("category.$id")
                        .sum("quantity").as(TOTAL_QUANTITY),
                Aggregation.lookup("categories", "_id", "_id", "category"),
                Aggregation.unwind("category"),
                Aggregation.project()
                        .and("category.name").as("categoryName")
                        .andInclude(TOTAL_QUANTITY),
                Aggregation.sort(Sort.Direction.DESC, TOTAL_QUANTITY)
        );

        return mongoTemplate.aggregate(aggregation, collectionName, TopOrdersByCategoryQuantityAggregation.class)
                .getMappedResults();
    }

    @Override
    public List<TopOrdersByQuantityAggregation> findTopOrdersByQuantityRevenue(String collectionName) {
        var aggregation = Aggregation.newAggregation(
                Aggregation.group("$productName")
                        .sum("quantity").as(TOTAL_SOLD),
                Aggregation.sort(Sort.Direction.DESC, TOTAL_SOLD),
                Aggregation.limit(10)
        );
        return mongoTemplate.aggregate(aggregation, collectionName, TopOrdersByQuantityAggregation.class)
                .getMappedResults();
    }
}
