package com.elyashevich.nats_consumer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "nats")
public class NatsProperties {
    private String server = "nats://localhost:4222";
    private int maxReconnectAttempts = 5;
    private long reconnectWaitMillis = 1000;
}