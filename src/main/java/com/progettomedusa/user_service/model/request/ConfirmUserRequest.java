package com.progettomedusa.user_service.model.request;

import jakarta.validation.constraints.Email;
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
    private String confirmationToken;
    @Email
    private String email;
}
