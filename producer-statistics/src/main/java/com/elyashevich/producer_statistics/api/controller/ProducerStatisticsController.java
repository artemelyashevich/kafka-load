package com.elyashevich.producer_statistics.api.controller;

import com.elyashevich.producer_statistics.model.TopOrdersByCategoryQuantityAggregation;
import com.elyashevich.producer_statistics.model.TopOrdersByQuantityAggregation;
import com.elyashevich.producer_statistics.service.ProducerStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class ProducerStatisticsController {

    private final ProducerStatisticsService producerStatisticsService;

    @GetMapping("/categories/{collectionName}")
    public ResponseEntity<List<TopOrdersByCategoryQuantityAggregation>> findAllCategoriesRevenue(
            @PathVariable("collectionName") String collectionName
    ) {
        var response = this.producerStatisticsService.findTopOrdersByCategoryRevenue(collectionName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/quantity/{collectionName}")
    public ResponseEntity<List<TopOrdersByQuantityAggregation>> findAllQuantityRevenue(
            @PathVariable("collectionName") String collectionName
    ){
        var response = this.producerStatisticsService.findTopOrdersByQuantityRevenue(collectionName);
        return ResponseEntity.ok(response);
    }
}
