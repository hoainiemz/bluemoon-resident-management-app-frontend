package org.example.hellofx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

//    public static String backendUrl = "https://bluemoon-resident-management-app-backend.onrender.com/";
    public static String backendUrl = "https://hoainiem-bluemoon-106512668344.asia-east1.run.app";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
