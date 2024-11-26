package org.tinkoff.apigateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tinkoff.apigateway.service.jwt.JwtAuthFilter;

@Configuration
public class GatewayConfig {

    @Value("${spring.cloud.gateway.routes[0].uri}")
    private String userServiceUri;

    @Value("${spring.cloud.gateway.routes[1].uri}")
    private String orderServiceUri;

    @Value("${spring.cloud.gateway.routes[2].uri}")
    private String restaurantServiceUri;

    @Value("${spring.cloud.gateway.routes[0].predicates[0]}")
    private String userServicePredicate;

    @Value("${spring.cloud.gateway.routes[1].predicates[0]}")
    private String orderServicePredicate;

    @Value("${spring.cloud.gateway.routes[2].predicates[0]}")
    private String restaurantServicePredicate;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, JwtAuthFilter jwtAuthFilter) {
        return builder.routes()
                .route("user-service",
                        r -> r.path(userServicePredicate)
                                .filters(f -> f.filter(jwtAuthFilter))
                                .uri(userServiceUri))
                .route("order-service",
                        r -> r.path(orderServicePredicate)
                                .filters(f -> f.filter(jwtAuthFilter))
                                .uri(orderServiceUri))
                .route("restaurant-service",
                        r -> r.path(restaurantServicePredicate)
                                .filters(f -> f.filter(jwtAuthFilter))
                                .uri(restaurantServiceUri))
                .build();
    }
}
