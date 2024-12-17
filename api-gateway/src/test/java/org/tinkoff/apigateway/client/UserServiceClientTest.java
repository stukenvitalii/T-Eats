package org.tinkoff.apigateway.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tinkoff.apigateway.dto.UserDto;
import org.tinkoff.apigateway.dto.auth.request.RegisterRequest;
import org.tinkoff.apigateway.dto.auth.request.ResetPasswordRequest;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceClientTest {

    @Container
    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.6.0");

    @Autowired
    private UserServiceClient userServiceClient;

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("app.user-service-url", wiremockServer::getBaseUrl);
    }

    @Test
    void testIsUsernameTaken() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        stubFor(get(urlPathEqualTo("/rest/users/check-username"))
                .withQueryParam("username", equalTo("takenUsername"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));

        // When
        boolean result = userServiceClient.isUsernameTaken("takenUsername");

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void testIsUsernameNotTaken() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        stubFor(get(urlPathEqualTo("/rest/users/check-username"))
                .withQueryParam("username", equalTo("availableUsername"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")));

        // When
        boolean result = userServiceClient.isUsernameTaken("availableUsername");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void testRegisterUser() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        RegisterRequest request = new RegisterRequest("username", "password", "email", "firstName", "lastName");
        stubFor(post(urlPathEqualTo("/rest/users/add"))
                .willReturn(aResponse()
                        .withStatus(200)));

        // When
        userServiceClient.registerUser(request);

        // Then
        verify(postRequestedFor(urlPathEqualTo("/rest/users/add"))
                .withRequestBody(equalToJson("{\"username\":\"username\",\"password\":\"password\",\"email\":\"email\",\"firstName\":\"firstName\",\"lastName\":\"lastName\"}")));
    }

    @Test
    void testGetUserByUsername() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        String userJson = "{\"username\":\"existingUser\",\"email\":\"email\",\"firstName\":\"firstName\",\"lastName\":\"lastName\"}";
        stubFor(get(urlPathEqualTo("/rest/users/by-username/existingUser"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(userJson)));

        // When
        UserDto result = userServiceClient.getUserByUsername("existingUser");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("existingUser");
    }

    @Test
    void testGetUserByUsernameNotFound() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        stubFor(get(urlPathEqualTo("/rest/users/by-username/nonExistingUser"))
                .willReturn(aResponse()
                        .withStatus(404)));

        // When
        UserDto result = userServiceClient.getUserByUsername("nonExistingUser");

        // Then
        assertThat(result).isNull();
    }

    @Test
    void testResetPassword() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        ResetPasswordRequest request = new ResetPasswordRequest("username", "newPassword", "1234");
        stubFor(post(urlPathEqualTo("/users/reset-password"))
                .willReturn(aResponse()
                        .withStatus(200)));

        // When
        userServiceClient.resetPassword(request);

        // Then
        verify(postRequestedFor(urlPathEqualTo("/users/reset-password"))
                .withRequestBody(equalToJson("{\"username\":\"username\",\"newPassword\":\"newPassword\", \"confirmationCode\":\"1234\"}")));
    }
}