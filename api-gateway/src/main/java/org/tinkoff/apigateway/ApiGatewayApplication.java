package org.tinkoff.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.reactive.ReactiveManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.tinkoff.apigateway.configuration.ApplicationConfig;

@SpringBootApplication(exclude = {
        ReactiveManagementWebSecurityAutoConfiguration.class,
        ReactiveSecurityAutoConfiguration.class
})
@EnableConfigurationProperties(ApplicationConfig.class)
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
