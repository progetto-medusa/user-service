package com.progettomedusa.user_service.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String username;
    @Email
    @NotBlank
    private String email;
    @Size(min = 8, max = 30)
    @NotBlank
    private String password;
    @NotBlank
    private String role;
    @NotNull
    @JsonProperty("application_id")
    private String applicationId;
}

