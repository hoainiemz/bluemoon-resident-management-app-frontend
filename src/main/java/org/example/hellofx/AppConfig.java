package org.example.hellofx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    public static String backendUrl = "http://localhost:8080";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
