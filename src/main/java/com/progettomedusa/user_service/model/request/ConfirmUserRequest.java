package com.progettomedusa.user_service.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ConfirmUserRequest {
    @NotNull
    @NotEmpty
    @JsonProperty("confirmation_token")
    private String confirmationToken;
    @NotEmpty
    @NotNull
    @JsonProperty("application_id")
    private String applicationId;
}
