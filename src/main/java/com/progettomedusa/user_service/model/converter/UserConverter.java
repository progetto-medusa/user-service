package com.progettomedusa.user_service.model.converter;

import com.progettomedusa.user_service.config.AppProperties;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.exception.ErrorMsg;
import com.progettomedusa.user_service.model.po.UserPO;
import com.progettomedusa.user_service.model.request.CreateUserRequest;
import com.progettomedusa.user_service.model.request.LoginRequest;
import com.progettomedusa.user_service.model.request.ResetPasswordRequest;
import com.progettomedusa.user_service.model.request.UpdateUserRequest;
import com.progettomedusa.user_service.model.response.*;
import com.progettomedusa.user_service.model.response.Error;
import com.progettomedusa.user_service.util.Tools;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.progettomedusa.user_service.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserConverter {

    private final Tools tools;
    private final AppProperties userApplicationProperties;

    public UserDTO createRequestToUserDTO(CreateUserRequest createUserRequest) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(createUserRequest.getUsername());
        userDTO.setPassword(createUserRequest.getPassword());
        userDTO.setEmail(createUserRequest.getEmail());
        userDTO.setRole(createUserRequest.getRole());
        userDTO.setApplicationId(createUserRequest.getApplicationId());
        log.info("UserConverter - createRequestToUserDTO END with DTO -> {}", userDTO);
        return userDTO;
    }

    public CreateRequestResponse createRequestResponse(UserPO userCreated) {
        CreateRequestResponse createRequestResponse = new CreateRequestResponse();
        createRequestResponse.setMessage("Creation done");
        createRequestResponse.setDomain(userApplicationProperties.getName());
        createRequestResponse.setTimestamp(tools.getInstant());
        createRequestResponse.setEmail(userCreated.getEmail());
        createRequestResponse.setUsername(userCreated.getUsername());
        log.info("UserConverter - createRequestResponse END with createRequestResponse -> {}", createRequestResponse);
        return createRequestResponse;
    }

    public CreateRequestResponse createRequestResponse(Exception e) {
        CreateRequestResponse createRequestResponse = new CreateRequestResponse();
        createRequestResponse.setMessage(e.getMessage());
        createRequestResponse.setDomain(userApplicationProperties.getName());
        createRequestResponse.setTimestamp(tools.getInstant());
        createRequestResponse.setDetailed(BASE_ERROR_DETAILS);
        log.info("UserConverter - createRequestResponse END with createRequestResponse -> {}", createRequestResponse);
        return createRequestResponse;
    }

    public UserPO dtoToPo(UserDTO userDTO) {
        UserPO userPO = new UserPO();
        if (userDTO.getId() != null) {
            userPO.setId(Long.valueOf(userDTO.getId()));
        }
        userPO.setUsername(userDTO.getUsername());
        userPO.setEmail(userDTO.getEmail());
        userPO.setPassword(userDTO.getPassword());
        userPO.setRole(userDTO.getRole());
        userPO.setApplicationId(userDTO.getApplicationId());
        log.info("UserConverter - createRequestToUserDTO END with PO -> {}", userPO);
        return userPO;
    }

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
        log.info("UserConverter - updateRequestToDto END with DTO -> {}", userDTO);
        return userDTO;
    }

    public UpdateUserResponse userToUpdateResponse(UserPO userPO) {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(userPO.getEmail());
        userResponse.setUsername(userPO.getUsername());

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

    public UserDTO loginRequestToUserDTO(LoginRequest loginRequest) {
        UserDTO userDTO = new UserDTO();
        String email = loginRequest.getEmail();
        userDTO.setEmail(email);
        if (email == null || !tools.isValidEmail(email)) {
            log.warn("UserConverter - Email non valida o assente in LoginRequest: {}", email);
        }
        userDTO.setPassword(loginRequest.getPassword());
        log.info("UserConverter - loginRequestToUserDTO END with DTO -> {}", userDTO);
        userDTO.setApplicationId(loginRequest.getApplicationId());
        return userDTO;
    }

    public LoginResponse userPoToLoginResponse(UserPO userPO) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setDomain(userApplicationProperties.getName());
        loginResponse.setTimestamp(tools.getInstant());
        log.info("UserConverter - userPoToLoginResponse END with LoginResponse -> {}", loginResponse);
        return loginResponse;
    }

    public LoginResponse userPoToLoginResponse(String message) {
        LoginResponse loginResponse = new LoginResponse();
        Error error = new Error();
        error.setCode("CODECODECODE");
        if (message.equals("WRONG_PASSWORD")) {
            error.setCode(ErrorMsg.USRSRV14.getCode());
            error.setMessage("Somethings gone wrong, check the documentation");
        } else if (message.equals("USER_NOT_FOUND")) {
            error.setCode(ErrorMsg.USRSRV15.getCode());
            error.setMessage("Somethings gone wrong, check the documentation");
        }
        error.setDomain("MicroServiceFunctional");
        error.setDetailed("Check on the docs with code, domain and status");
        loginResponse.setTimestamp(tools.getInstant());
        loginResponse.setDetailed(BASE_ERROR_DETAILS);
        loginResponse.setError(error);
        log.info("UserConverter - userPoToLoginResponse END with userPoToLoginResponse -> {}", loginResponse);
        return loginResponse;
    }

    public LoginResponse userPoToLoginResponse(Exception exception) {
        LoginResponse loginResponse = new LoginResponse();
        Error error = new Error();
        error.setCode("CODECODECODE");
        error.setMessage("Somethings gone wrong, check the documentation");
        error.setDomain("MicroServiceFunctional");
        error.setDetailed("Check on the docs with code, domain and status");
        loginResponse.setTimestamp(tools.getInstant());
        loginResponse.setDetailed(BASE_ERROR_DETAILS);
        loginResponse.setError(error);
        return loginResponse;
    }

    public UserDTO resetPasswordRequestToDto(ResetPasswordRequest resetPasswordRequest) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(resetPasswordRequest.getMail());
        log.info("UserConverter - updateRequestToDto END with DTO -> {}", userDTO);
        return userDTO;
    }


    public ResetPasswordResponse resetPasswordResponse(String message) {
        ResetPasswordResponse resetPasswordResponse = new ResetPasswordResponse();
        resetPasswordResponse.setMessage(message);
        resetPasswordResponse.setDomain(userApplicationProperties.getName());
        resetPasswordResponse.setTimestamp(tools.getInstant());
        if (message.equals("USER_NOT_FOUND")) {
            resetPasswordResponse.setDetailed(USER_NOT_FOUND_MESSAGE);
    }
        return resetPasswordResponse;
    }

}
