package org.elyashevich.consumer.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("producerStatistics")
public class ProducerStats {

    @Id
    private String id;
    
    @Field
    private String producerId;

    @Field
    private String topicName;

    @Field
    private int callCount;

    @Field
    private LocalDateTime lastCallTime;
}