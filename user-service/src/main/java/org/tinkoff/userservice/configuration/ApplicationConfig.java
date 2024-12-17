package org.tinkoff.userservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
        String orderServiceUrl,
        String restaurantServiceUrl
) {
}
