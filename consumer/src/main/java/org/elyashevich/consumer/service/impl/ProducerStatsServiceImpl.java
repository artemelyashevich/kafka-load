package org.elyashevich.consumer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elyashevich.consumer.domain.entity.ProducerStats;
import org.elyashevich.consumer.repository.ProducerStatsRepository;
import org.elyashevich.consumer.service.ProducerStatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerStatsServiceImpl implements ProducerStatsService {

    private final ProducerStatsRepository statsRepository;

    @Override
    @Transactional
    public void recordProducerCall(String producerId, String topicName) {
        statsRepository.findByProducerIdAndTopicName(producerId, topicName).ifPresentOrElse(
                stat -> {
                    stat.setCallCount(stat.getCallCount() + 1);
                    statsRepository.save(stat);
                }, () -> {
                    var stat = ProducerStats.builder()
                            .producerId(producerId)
                            .topicName(topicName)
                            .lastCallTime(LocalDateTime.now())
                            .callCount(1)
                            .build();
                    statsRepository.save(stat);
                }
        );
    }

    @Override
    public List<ProducerStats> findAll() {
        return statsRepository.findAll();
    }
}