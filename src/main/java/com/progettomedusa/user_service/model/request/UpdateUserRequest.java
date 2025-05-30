package com.progettomedusa.user_service.model.request;

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
    private String name;
    @Email
    @NotBlank
    private String email;
    @Size(min = 8)
    @NotBlank
    private String password;
    @NotBlank
    private String role;
}

