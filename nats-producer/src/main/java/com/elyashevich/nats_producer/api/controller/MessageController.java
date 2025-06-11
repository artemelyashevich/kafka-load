package com.elyashevich.nats_producer.api.controller;

import com.elyashevich.nats_producer.publisher.NatsPublisher;
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

    @PostMapping
    public void sendMessage(@RequestBody String message) {
        this.natsPublisher.publish("messages.subject", message);
    }
}
