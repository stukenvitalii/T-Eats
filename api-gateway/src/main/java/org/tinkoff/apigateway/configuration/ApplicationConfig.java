package org.tinkoff.apigateway.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
        String secretKey,
        String userServiceUrl
) {
}
