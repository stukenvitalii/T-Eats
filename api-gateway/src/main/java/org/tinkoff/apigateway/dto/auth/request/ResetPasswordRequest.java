package org.tinkoff.apigateway.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResetPasswordRequest {
    private String username;
    private String newPassword;
    private String confirmationCode;
}