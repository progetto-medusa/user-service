package com.progettomedusa.user_service.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestForm {

    @NotBlank
    private String mail;
    @NotBlank
    private String password;
}
