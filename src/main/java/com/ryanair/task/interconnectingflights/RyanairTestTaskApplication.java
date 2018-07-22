package com.ryanair.task.interconnectingflights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RyanairTestTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(RyanairTestTaskApplication.class, args);
	}
}
