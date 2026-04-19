package com.bellator.bellator_barbearia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SystemBarbeariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemBarbeariaApplication.class, args);
	}

}
