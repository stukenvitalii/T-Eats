package org.tinkoff.apigateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
    @Value("${app.user-service-url}")
    private String userServiceUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder().baseUrl(userServiceUrl).build();
    }
}
