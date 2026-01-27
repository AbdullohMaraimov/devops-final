package com.example.demo;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ApiMetrics {

    private final MeterRegistry registry;

    public ApiMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void incRequest(String apiName) {
        Counter.builder("api_requests_total")
                .tag("api", apiName)
                .register(registry)
                .increment();
    }

    public void incSuccess(String apiName) {
        Counter.builder("api_success_total")
                .tag("api", apiName)
                .register(registry)
                .increment();
    }

    public void incFailed(String apiName, String reason) {
        Counter.builder("api_failed_total")
                .tag("api", apiName)
                .tag("reason", reason == null ? "unknown" : reason)
                .register(registry)
                .increment();
    }

    public void incFoodAdded() {
        Counter.builder("food_added_total").register(registry).increment();
    }

    public void incFoodUpdated() {
        Counter.builder("food_updated_total").register(registry).increment();
    }

    public void recordLatencyMs(String apiName, long ms) {
        Timer.builder("api_latency_ms")
                .tag("api", apiName)
                .publishPercentileHistogram(true)
                .register(registry)
                .record(ms, TimeUnit.MILLISECONDS);
    }
}
