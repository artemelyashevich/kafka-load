package com.elyashevich.nats_consumer.api.subscriber;

import io.nats.client.Message;
import io.nats.client.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExampleMessageHandler implements MessageHandler {

    @Override
    public void onMessage(Message msg) {
        String message = new String(msg.getData());
        log.info("Received message on subject {}: {}", msg.getSubject(), message);
    }
}