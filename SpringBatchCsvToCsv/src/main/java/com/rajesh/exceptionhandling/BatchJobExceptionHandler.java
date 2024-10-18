package com.rajesh.exceptionhandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BatchJobExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(BatchJobExceptionHandler.class);

    public void handle(Exception e, String stage, Object item) {
        logger.error("Error occurred during {} stage for item: {}. Exception: {}", stage, item, e.getMessage());
    }
}
