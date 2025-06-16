package com.elyashevich.nats_consumer.api.subscriber;

import com.elyashevich.nats_consumer.metrics.GrafanaNatsConsumerMetrics;
import io.micrometer.core.instrument.Timer;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NatsMessageHandler implements MessageHandler {

    private final GrafanaNatsConsumerMetrics metrics;

    @Override
    public void onMessage(Message msg) {
        Timer.Sample timer = this.metrics.startTimer();

        String message = new String(msg.getData());

        this.metrics.recordSuccess(timer, msg.getSubject(), message.getBytes().length);
        log.info("Received message on subject {}: {}", msg.getSubject(), message);
    }
}