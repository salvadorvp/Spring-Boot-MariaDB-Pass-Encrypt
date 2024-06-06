package com.westwebsystems.budgets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class BudgetsApplication {
	private static final Logger logger = LoggerFactory.getLogger(BudgetsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BudgetsApplication.class, args);
		logger.info("---------->   A P P L I C A T I O N   S T A R T   <----------");
	}
}
