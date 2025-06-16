package com.elyashevich.nats_producer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GrafanaHttpProducerMetrics {

    private final MeterRegistry meterRegistry;
    private final Map<String, Timer> successTimers = new ConcurrentHashMap<>();

    private final Timer globalProcessingTimer;
    private final Counter totalCounter;
    private final DistributionSummary sizeSummary;

    public GrafanaHttpProducerMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.globalProcessingTimer = Timer.builder("http.producer.processing.time.global")
                .description("Total requests processing time")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(this.meterRegistry);

        this.totalCounter = Counter.builder("http.producer.requests.total")
                .description("Total produced requests count")
                .register(this.meterRegistry);

        this.sizeSummary = DistributionSummary.builder("http.producer.request.size")
                .description("Requests size distribution")
                .baseUnit("bytes")
                .register(this.meterRegistry);
    }

    public Timer.Sample startTimer() {
        return Timer.start(this.meterRegistry);
    }

    public void recordSuccess(Timer.Sample sample, String subject, int messageSize) {
        if (sample == null) {
            return;
        }

        sample.stop(getSuccessTimer(subject));
        sample.stop(globalProcessingTimer);

        this.totalCounter.increment();
        this.sizeSummary.record(messageSize);
    }

    private Timer getSuccessTimer(String subject) {
        return successTimers.computeIfAbsent(subject, t ->
                Timer.builder("nats.consumer.processing.time.success")
                        .tags("subject", t, "status", "success")
                        .publishPercentiles(0.5, 0.95, 0.99)
                        .register(this.meterRegistry));
    }
}
