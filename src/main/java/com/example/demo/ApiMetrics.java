package com.example.demo;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

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
}
