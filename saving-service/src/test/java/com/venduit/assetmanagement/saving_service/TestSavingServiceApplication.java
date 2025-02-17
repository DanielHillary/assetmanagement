package com.venduit.assetmanagement.saving_service;

import org.springframework.boot.SpringApplication;

public class TestSavingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(SavingServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
