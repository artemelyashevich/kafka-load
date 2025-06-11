package com.elyashevich.nats_consumer.config;

import com.elyashevich.nats_consumer.api.subscriber.ExampleMessageHandler;
import com.elyashevich.nats_consumer.api.subscriber.NatsSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NatsInitializer implements CommandLineRunner {

    private final NatsSubscriber natsSubscriber;
    private final ExampleMessageHandler messageHandler;

    @Override
    public void run(String... args) throws Exception {
        natsSubscriber.subscribe("messages.subject", messageHandler);
    }
}