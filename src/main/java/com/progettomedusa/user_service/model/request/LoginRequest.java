package com.progettomedusa.user_service.model.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Email
    @NotNull
    @NotBlank
    private String email;
    @Size(min = 8)
    @NotBlank
    private String password;
    @NotBlank
    private String applicationId;
}
