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
public class NewPasswordEmailRequest {
    @JsonProperty("application_id")
    private String applicationId;
    @NotBlank
    private String email;
    @NotBlank
    private String token;
}
