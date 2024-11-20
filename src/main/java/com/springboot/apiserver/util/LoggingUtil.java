package com.springboot.apiserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);

    //Singleton
    private LoggingUtil() {}

    public static void logGeneratedNegativePrompt(String prompt) {
        logger.info("Generated Negative Prompt: {}", prompt);
    }

    public static void logResponseDetails(int statusCode, String responseBody) {
        logger.info("Response Code: {}", statusCode);
        logger.info("Response Body: {}", responseBody);
    }

    public static void logError(String message, Exception e) {
        logger.error(message, e);
    }
    public static void logGPTResponse(String message){
        logger.info("gpt message : {} ", message);
    }
}
