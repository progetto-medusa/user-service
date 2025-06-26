package com.progettomedusa.user_service.model.converter;

import com.progettomedusa.user_service.config.AppProperties;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.po.UserPO;
import com.progettomedusa.user_service.model.request.*;
import com.progettomedusa.user_service.model.response.*;
import com.progettomedusa.user_service.model.response.Error;
import com.progettomedusa.user_service.util.Tools;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import java.util.ArrayList;
import java.util.List;

import static com.progettomedusa.user_service.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserConverter {

    private final Tools tools;
    private final AppProperties userApplicationProperties;


    public GetUsersResponse listOfUsersGetUsersResponse(List<UserPO> userList) {
        GetUsersResponse getUsersResponse = new GetUsersResponse();

        List<GetUserResponse> getUserResponseList = new ArrayList<>();

        for (UserPO user : userList) {
            GetUserResponse getUserResponse = userPoToGetUserResponse(user, true);
            getUserResponseList.add(getUserResponse);
        }

        getUsersResponse.setUsers(getUserResponseList);
        getUsersResponse.setDomain(userApplicationProperties.getName());
        getUsersResponse.setTimestamp(tools.getInstant());

        log.info("UserConverter - listOfUsersoGetUsersResponse END with response -> {}", getUsersResponse);
        return getUsersResponse;
    }

    public GetUserResponse userPoToGetUserResponse(UserPO userPO, boolean internal) {
        GetUserResponse getUserResponse = new GetUserResponse();

        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(userPO.getId()));
        userResponse.setRole(userPO.getRole());
        userResponse.setUsername(userPO.getUsername());
        userResponse.setEmail(userPO.getEmail());
        userResponse.setUpdateDate(userPO.getUpdateDate());
        userResponse.setInsertDate(userPO.getInsertDate());
        userResponse.setValid(userPO.isValid());
        userResponse.setAcceptedTerms(userPO.isAcceptedTerms());

        getUserResponse.setUser(userResponse);
        if (!internal) {
            getUserResponse.setDomain(userApplicationProperties.getName());
            getUserResponse.setTimestamp(tools.getInstant());
        }
        log.info("UserConverter - userPoToGetUserResponse END with response -> {}", getUserResponse);
        return getUserResponse;
    }

    public GetUserResponse getUserResponse() {
        GetUserResponse getUserResponse = new GetUserResponse();
        getUserResponse.setUser(new UserResponse());
        getUserResponse.setMessage(USER_NOT_FOUND_MESSAGE);
        getUserResponse.setDomain(userApplicationProperties.getName());
        getUserResponse.setTimestamp(tools.getInstant());
        log.info("UserConverter - getUserResponse END with getUserResponse -> {}", getUserResponse);
        return getUserResponse;
    }

    public UserDTO updateRequestToDto(UpdateUserRequest updateUserRequest) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(updateUserRequest.getId());
        userDTO.setUsername(updateUserRequest.getUsername());
        userDTO.setPassword(updateUserRequest.getPassword());
        userDTO.setEmail(updateUserRequest.getEmail());
        userDTO.setRole(updateUserRequest.getRole());
        userDTO.setApplicationId(updateUserRequest.getApplicationId());
        userDTO.setValid(updateUserRequest.isValid());
        log.info("UserConverter - updateRequestToDto END with DTO -> {}", userDTO);
        return userDTO;
    }

    public UpdateUserResponse userToUpdateResponse(UserPO userPO) {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(userPO.getEmail());
        userResponse.setUsername(userPO.getUsername());
        userResponse.setValid(userPO.isValid());

        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setMessage("Update done");
        updateUserResponse.setDomain(userApplicationProperties.getName());
        updateUserResponse.setTimestamp(tools.getInstant());
        updateUserResponse.setUser(userResponse);

        log.info("UserConverter - userToUpdateResponse END with updateUserResponse -> {}", updateUserResponse);
        return updateUserResponse;
    }

    public DeleteUserResponse deleteUserResponse() {
        DeleteUserResponse deleteUserResponse = new DeleteUserResponse();
        deleteUserResponse.setMessage(USER_DELETED_MESSAGE);
        deleteUserResponse.setDomain(userApplicationProperties.getName());
        deleteUserResponse.setTimestamp(tools.getInstant());
        log.info("UserConverter - deleteUserResponse END with deleteUserResponse -> {}", deleteUserResponse);
        return deleteUserResponse;
    }

    public LoginResponse userPoToLoginResponse(UserPO userPO) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setRole(userPO.getRole());
        loginResponse.setUuid(userPO.getUserUuid());
        loginResponse.setDomain(userApplicationProperties.getName());
        loginResponse.setTimestamp(tools.getInstant());
        log.info("UserConverter - userPoToLoginResponse END with LoginResponse -> {}", loginResponse);
        return loginResponse;
    }

    @Deprecated
    public LoginResponse userPoToLoginResponse(String message) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setTimestamp(tools.getInstant());
        loginResponse.setDetailed(BASE_ERROR_DETAILS);
        log.info("UserConverter - userPoToLoginResponse END with userPoToLoginResponse -> {}", loginResponse);
        return loginResponse;
    }

}
