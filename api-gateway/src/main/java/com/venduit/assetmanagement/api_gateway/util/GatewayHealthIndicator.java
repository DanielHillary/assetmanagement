package com.venduit.assetmanagement.api_gateway.util;


import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class GatewayHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Simulate a health check; replace this with real logic
        boolean loanServiceAvailable = checkLoanServiceStatus();

        if (loanServiceAvailable) {
            return Health.up().withDetail("service", "API Gateway service is running smoothly").build();
        } else {
            return Health.down().withDetail("error", "API Gateway service is unavailable").build();
        }
    }

    private boolean checkLoanServiceStatus() {
        // Replace with actual logic, such as checking DB status or external API connectivity
        return true;
    }
}
