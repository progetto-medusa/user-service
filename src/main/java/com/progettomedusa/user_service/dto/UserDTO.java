package com.progettomedusa.user_service.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="password")
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
}
