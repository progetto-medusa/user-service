package com.progettomedusa.user_service.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @Email
    @NotBlank
    private String mail;
    @JsonProperty("password")
    private String password;
    @JsonProperty("application_id")
    private String applicationId;
}
