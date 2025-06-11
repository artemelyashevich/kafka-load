package com.elyashevich.nats_consumer.api.subscriber;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NatsSubscriber {

    private final Connection natsConnection;

    public void subscribe(String subject, MessageHandler handler) {
        Dispatcher dispatcher = natsConnection.createDispatcher();
        dispatcher.subscribe(subject, handler);
    }
}