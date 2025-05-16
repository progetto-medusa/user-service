package model.converter;

import dto.UserDTO;
import model.user.User;

public class MainConverter {

    public static UserDTO userDTO(User user){
        if(user==null)
            return null;

        return new UserDTO(
                user.getId(),
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
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRole()
        );
    }
}
