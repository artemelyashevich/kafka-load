package com.elyashevich.nats_consumer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GrafanaNatsConsumerMetrics {
    private final MeterRegistry registry;
    private final Map<String, Timer> successTimers = new ConcurrentHashMap<>();

    private final Timer globalProcessingTimer;
    private final Counter totalMessagesCounter;
    private final DistributionSummary messageSizeSummary;

    public GrafanaNatsConsumerMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.globalProcessingTimer = Timer.builder("nats.consumer.processing.time.global")
                .description("Total message processing time")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        this.totalMessagesCounter = Counter.builder("nats.consumer.messages.total")
                .description("Total consumed messages count")
                .register(registry);

        this.messageSizeSummary = DistributionSummary.builder("nats.consumer.message.size")
                .description("Message size distribution")
                .baseUnit("bytes")
                .register(registry);
    }

    public Timer.Sample startTimer() {
        return Timer.start(registry);
    }

    public void recordSuccess(Timer.Sample sample, String subject, int messageSize) {
        if (sample == null) {
            return;
        }

        sample.stop(getSuccessTimer(subject));
        sample.stop(globalProcessingTimer);

        totalMessagesCounter.increment();
        messageSizeSummary.record(messageSize);
    }

    private Timer getSuccessTimer(String subject) {
        return successTimers.computeIfAbsent(subject, t ->
                Timer.builder("nats.consumer.processing.time.success")
                        .tags("subject", t, "status", "success")
                        .publishPercentiles(0.5, 0.95, 0.99)
                        .register(registry));
    }
}