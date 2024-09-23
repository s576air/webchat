package com.s576air.webchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WebchatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebchatApplication.class, args);
	}

}