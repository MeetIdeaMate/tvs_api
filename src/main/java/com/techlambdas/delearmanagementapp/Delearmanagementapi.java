package com.techlambdas.delearmanagementapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class Delearmanagementapi {

	public static void main(String[] args) {
		SpringApplication.run(Delearmanagementapi.class, args);
	}

}
