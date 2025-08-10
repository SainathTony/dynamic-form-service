package com.dynamic_form.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		// Load .env file if it exists
		try {
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();
			
			// Set system properties from .env file
			dotenv.entries().forEach(entry -> {
				String key = entry.getKey();
				String value = entry.getValue();
				if (System.getProperty(key) == null && System.getenv(key) == null) {
					System.setProperty(key, value);
				}
			});
		} catch (Exception e) {
			System.err.println("Warning: Could not load .env file: " + e.getMessage());
		}
		
		SpringApplication.run(ServiceApplication.class, args);
	}

}
