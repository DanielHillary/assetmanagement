package com.venduit.assetmanagement.user_service.util;


import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                return Health.up().withDetail("database", "Database is reachable").build();
            }
        } catch (Exception ex) {
            return Health.down()
                    .withDetail("database", "Database is unreachable")
                    .withException(ex)
                    .build();
        }

        return Health.unknown().build();
    }
}
