package com.progettomedusa.user_service.model.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="password")
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String applicationId;
    private String updateDate;
    private String insertDate;
    private boolean isValid;
    private String confirmationToken;
}
