package com.elyashevich.nats_producer.api.controller;

import com.elyashevich.nats_producer.metrics.GrafanaHttpProducerMetrics;
import com.elyashevich.nats_producer.publisher.NatsPublisher;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final NatsPublisher natsPublisher;
    private final GrafanaHttpProducerMetrics metrics;

    @PostMapping
    public void sendMessage(@RequestBody String message) {
        Timer.Sample timer = this.metrics.startTimer();
        this.natsPublisher.publish("messages.subject", message);
        this.metrics.recordSuccess(timer, "messages.subject", message.getBytes().length);
    }
}
