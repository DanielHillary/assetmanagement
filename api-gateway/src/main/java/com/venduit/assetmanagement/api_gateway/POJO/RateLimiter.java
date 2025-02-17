package com.venduit.assetmanagement.api_gateway.POJO;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
    private final int maxRequests;
    private final long timeWindowMillis;
    private final Map<String, LinkedList<Long>> requestLogs;

    public RateLimiter(int maxRequests, long timeWindowMillis) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeWindowMillis;
        this.requestLogs = new ConcurrentHashMap<>();
    }

    public synchronized boolean isAllowed(String identifier) {
        long now = Instant.now().toEpochMilli();


        requestLogs.putIfAbsent(identifier, new LinkedList<>());
        LinkedList<Long> requestLog = requestLogs.get(identifier);

        // Remove timestamps outside the time window
        while (!requestLog.isEmpty() && requestLog.getFirst() < now - timeWindowMillis) {
            requestLog.pollFirst();
        }

        // Check if the requests exceed the rate limit
        if (requestLog.size() < maxRequests) {
            requestLog.addLast(now);
            return true;
        }

        return false;
    }
}
