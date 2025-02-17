package com.venduit.assetmanagement.investment_service;

import org.springframework.boot.SpringApplication;

public class TestInvestmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(InvestmentServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
