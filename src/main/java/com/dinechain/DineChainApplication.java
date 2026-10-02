package com.dinechain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class DineChainApplication {

	public static void main(String[] args) {
		SpringApplication.run(DineChainApplication.class, args);
	}
}
