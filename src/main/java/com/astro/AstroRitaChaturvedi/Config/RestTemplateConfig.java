package com.astro.AstroRitaChaturvedi.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // Set timeouts to prevent hanging requests
        factory.setConnectTimeout(5000); // 5 seconds
        factory.setReadTimeout(10000);   // 10 seconds
        
        RestTemplate restTemplate = new RestTemplate(factory);
        
        // Add error handler if needed
        // restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        
        return restTemplate;
    }
}