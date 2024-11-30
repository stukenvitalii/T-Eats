package org.tinkoff.orderservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
    @Value("${app.restaurant-service-url}")
    private String restaurantBaseUrl;

    @Value("${app.user-service-url}")
    private String userBaseUrl;

    @Bean
    public RestClient restaurantClient() {
        return RestClient.builder().baseUrl(restaurantBaseUrl).build();
    }

    @Bean
    public RestClient userClient() {
        return RestClient.builder().baseUrl(userBaseUrl).build();
    }
}
