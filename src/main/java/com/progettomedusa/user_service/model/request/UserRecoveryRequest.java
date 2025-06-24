package com.progettomedusa.user_service.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRecoveryRequest {
    @Email
    @NotNull
    @NotBlank
    private String email;
    @NotBlank
    @JsonProperty("application_id")
    private String applicationId;
}
