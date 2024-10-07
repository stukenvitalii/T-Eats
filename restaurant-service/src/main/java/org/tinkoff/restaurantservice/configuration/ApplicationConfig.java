package org.tinkoff.restaurantservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
        String orderServiceUrl,
        String userServiceUrl
) {
}