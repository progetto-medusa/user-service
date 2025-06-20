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
public class CreatePMUserRequest {
    @NotBlank
    private String username;
    @Email
    @NotBlank
    private String email;
    @Size(min = 8)
    @NotBlank
    private String password;
    @NotBlank
    private String role;
    @NotBlank
    @JsonProperty("application_id")
    private String applicationId;
    @NotNull
    @JsonProperty("accepted_terms")
    private boolean acceptedTerms;
}
