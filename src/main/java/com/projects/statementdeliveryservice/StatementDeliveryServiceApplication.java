package com.projects.statementdeliveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StatementDeliveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatementDeliveryServiceApplication.class, args);
	}

}
