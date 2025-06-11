package com.elyashevich.nats_producer.publisher;

import io.nats.client.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NatsPublisher {

    private final Connection natsConnection;

    public void publish(String subject, String message) {
        natsConnection.publish(subject, message.getBytes());
    }
}