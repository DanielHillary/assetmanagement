package com.venduit.assetmanagement.transaction_service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    public void logAction(String actionType, String userId, String details) {
        logger.info("Action: {}, User: {}, Details: {}", actionType, userId, details);
    }

    public void logError(String actionType, String userId, String errorMessage) {
        logger.error("Action: {}, User: {}, Error: {}", actionType, userId, errorMessage);
    }
}
