package org.elyashevich.consumer.repository;

import org.elyashevich.consumer.domain.entity.ProducerStats;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProducerStatsRepository extends MongoRepository<ProducerStats, String> {

    Optional<ProducerStats> findByProducerIdAndTopicName(String producerId, String topicName);
}