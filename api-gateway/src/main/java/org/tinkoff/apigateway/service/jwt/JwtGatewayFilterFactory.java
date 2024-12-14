package org.tinkoff.apigateway.service.jwt;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final JwtGatewayFilter jwtGatewayFilter;

    public JwtGatewayFilterFactory(JwtGatewayFilter jwtGatewayFilter) {
        this.jwtGatewayFilter = jwtGatewayFilter;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return jwtGatewayFilter;
    }
}
