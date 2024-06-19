package com.example.ems_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
public class EmsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmsAppApplication.class, args);
	}

}
