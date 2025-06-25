package com.progettomedusa.user_service.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NewPasswordRequest {
    @JsonProperty("application_id")
    private String applicationId;
    @NotBlank
    private String password;
    @NotBlank
    private String token;
}
