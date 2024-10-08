package org.tinkoff.orderservice.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
        String restaurantServiceUrl,
        String userServiceUrl
) {
}
