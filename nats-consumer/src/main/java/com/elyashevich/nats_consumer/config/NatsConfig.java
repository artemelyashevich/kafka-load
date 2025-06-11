package com.elyashevich.nats_consumer.config;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class NatsConfig {

    private final NatsProperties natsProperties;

    @Bean
    public Connection natsConnection() throws IOException, InterruptedException {
        var options = new Options.Builder()
                .server(natsProperties.getServer())
                .maxReconnects(natsProperties.getMaxReconnectAttempts())
                .reconnectWait(Duration.ofMillis(natsProperties.getReconnectWaitMillis()))
                .build();
        return Nats.connect(options);
    }
}