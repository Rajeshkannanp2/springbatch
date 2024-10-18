package com.rajesh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestSchedulerApplication.class, args);
	}

}
