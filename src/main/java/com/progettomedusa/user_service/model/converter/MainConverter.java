package com.progettomedusa.user_service.model.converter;

import com.progettomedusa.user_service.dto.UserDTO;
import com.progettomedusa.user_service.model.user.User;

public class MainConverter {

    public static UserDTO userDTO(User user){
        if(user==null)
            return null;

        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }
    public static User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return new User(
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRole()
        );
    }
}
