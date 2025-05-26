package com.progettomedusa.user_service.model.converter;

import com.progettomedusa.user_service.config.AppProperties;
import com.progettomedusa.user_service.dto.UserDTO;
import com.progettomedusa.user_service.model.response.RestResponse;
import com.progettomedusa.user_service.model.user.User;
import com.progettomedusa.user_service.util.Tools;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
    public class UserConverter {

        private final Tools tools;
        private final AppProperties appProperties;

        public RestResponse retrieveRestResponseForRootPath(UserDTO userDTO) {
            log.debug("Converter - Retrieve rest response for root path START with user DTO -> {}", userDTO);

            RestResponse restResponse = new RestResponse();

            restResponse.setMessage("Service is ONLINE");
            restResponse.setDomain(appProperties.getName());
            restResponse.setDetailed("Called for user -> " + userDTO.getName());
            restResponse.setTimestamp(tools.getInstant());

            log.debug("Converter - Retrieve rest response for root path END with DTO -> {}", restResponse);
            return restResponse;
        }

        public UserDTO userDTO(User user) {
            log.debug("Converter - Converting User entity to UserDTO START for user -> {}", user);

            if (user == null) {
                log.warn("Converter - Input User entity is null");
                return null;
            }

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setRole(user.getRole());

            log.debug("Converter - Converting User entity to UserDTO END with DTO -> {}", userDTO);
            return userDTO;
        }

        public User toEntity(UserDTO userDTO) {
            log.debug("Converter - Converting UserDTO to User entity START for DTO -> {}", userDTO);

            if (userDTO == null) {
                log.warn("Converter - Input UserDTO is null");
                return null;
            }

            User user = new User();
            user.setId(userDTO.getId());
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());

            log.debug("Converter - Converting UserDTO to User entity END with entity -> {}", user);
            return user;
        }

        public RestResponse buildGetUserResponse(List<User> userList, UserDTO userDTO) {
            log.debug("Converter - Build Get User Response START with User List -> {}, DTO -> {}", userList, userDTO);

            RestResponse restResponse = new RestResponse();

            restResponse.setMessage("Users list retrieved successfully");
            restResponse.setDomain(appProperties.getName());

            try {
                restResponse.setDetailed("Requested by: " + userDTO.getName());
            } catch (NullPointerException e) {
                restResponse.setDetailed("Requested by: Anonymous");
            }

            restResponse.setTimestamp(tools.getInstant());

            log.debug("Converter - Build Get User Response END with Response -> {}", restResponse);
            return restResponse;
        }



}
