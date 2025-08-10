package com.dynamic_form.service.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EnvironmentConfig {

    @Autowired
    private ConfigurableEnvironment environment;

    @PostConstruct
    public void loadEnvFile() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            Map<String, Object> envProps = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                // Only add if not already set by system environment or system properties
                if (System.getProperty(key) == null && System.getenv(key) == null) {
                    envProps.put(key, value);
                }
            });

            if (!envProps.isEmpty()) {
                environment.getPropertySources()
                        .addLast(new MapPropertySource("dotenv", envProps));
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load .env file: " + e.getMessage());
        }
    }
}