package org.example.hellofx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

//    public static String backendUrl = "https://bluemoon-resident-management-app-backend.onrender.com/";
//public static String backendUrl = "https://hoainiem-bluemoon-106512668344.asia-east1.run.app";
    public static String backendUrl = "https://92cd-2402-800-61c7-6300-7cc-5b2-12d3-9bdf.ngrok-free.app";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
