package com.minho.ownit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class OwnItApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwnItApplication.class, args);
	}

}
