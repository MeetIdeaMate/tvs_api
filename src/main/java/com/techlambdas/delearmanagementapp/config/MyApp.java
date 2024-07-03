package com.techlambdas.delearmanagementapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyApp {
    private static final Logger logger = LoggerFactory.getLogger(MyApp.class);
    public static void main(String[] args) {
        logger.info("This is an info message.");
        logger.error("This is an error message.");
    }
}
