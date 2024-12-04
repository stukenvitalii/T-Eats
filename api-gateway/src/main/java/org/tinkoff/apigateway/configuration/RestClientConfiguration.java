package org.tinkoff.apigateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
    @Value("${app.user-service-url}")
    private String userServiceUrl;

    @Bean
    public RestClient restClient() {
//        var factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setConnectTimeout(5000);
//        factory.setReadTimeout(5000);
        return RestClient.builder().baseUrl(userServiceUrl).build();
    }
}
