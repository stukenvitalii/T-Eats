package org.tinkoff.orderservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
    @Value("${app.api-gateway-url}")
    private String apiGatewayUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder().baseUrl(apiGatewayUrl).build();
    }
}
