package com.progettomedusa.user_service.model.converter;

import com.progettomedusa.user_service.config.AppProperties;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.po.UserPO;
import com.progettomedusa.user_service.model.request.*;
import com.progettomedusa.user_service.model.response.*;
import com.progettomedusa.user_service.model.response.Error;
import com.progettomedusa.user_service.util.Tools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.progettomedusa.user_service.util.Constants.BASE_ERROR_DETAILS;
import static com.progettomedusa.user_service.util.Constants.USER_NOT_FOUND_MESSAGE;

@Slf4j
@RequiredArgsConstructor
@Component
public class PMUserConverter {

    private final Tools tools;
    private final AppProperties userApplicationProperties;

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
        userPO.setUpdateDate(userDTO.getUpdateDate());
        userPO.setInsertDate(userDTO.getInsertDate());
        userPO.setValid(userDTO.isValid());
        userPO.setAcceptedTerms(userDTO.isAcceptedTerms());
        userPO.setUpdateDate(tools.getInstant());
        userPO.setInsertDate(tools.getInstant());
        log.info("UserConverter - createRequestToUserDTO END with PO -> {}", userPO);
        return userPO;
    }


    public UserDTO createPMUserRequestToUserDTO(CreatePMUserRequest createPMUserRequest) {
        UserDTO userDTO = new UserDTO();
        String confirmationToken = UUID.randomUUID().toString();
        userDTO.setUsername(createPMUserRequest.getUsername());
        userDTO.setPassword(createPMUserRequest.getPassword());
        userDTO.setEmail(createPMUserRequest.getEmail());
        userDTO.setRole(createPMUserRequest.getRole());
        userDTO.setApplicationId(createPMUserRequest.getApplicationId());
        userDTO.setUpdateDate(tools.getInstant());
        userDTO.setInsertDate(tools.getInstant());
        userDTO.setValid(false);
        userDTO.setConfirmationToken(confirmationToken);
        userDTO.setAcceptedTerms(createPMUserRequest.isAcceptedTerms());
        log.info("UserConverter - createRequestToUserDTO END with DTO -> {}", userDTO);
        return userDTO;
    }

    public CreatePMUserResponse createPMUserResponse(Exception e) {
        CreatePMUserResponse createPMUserResponse = new CreatePMUserResponse();
        createPMUserResponse.setMessage(e.getMessage());
        createPMUserResponse.setDomain(userApplicationProperties.getName());
        createPMUserResponse.setTimestamp(tools.getInstant());
        createPMUserResponse.setDetailed(BASE_ERROR_DETAILS);
        log.info("UserConverter - createRequestResponse END with createRequestResponse -> {}", createPMUserResponse);
        return createPMUserResponse;
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

    public UserDTO resetPasswordRequestToDto(ResetPasswordRequest resetPasswordRequest) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(resetPasswordRequest.getMail());
        userDTO.setApplicationId("mail-service");
        log.info("UserConverter - updateRequestToDto END with DTO -> {}", userDTO);
        return userDTO;
    }

    public UserDTO confirmUserRequestToDto(ConfirmUserRequest confirmUserRequest){
        UserDTO userDTO = new UserDTO();
        userDTO.setApplicationId(confirmUserRequest.getApplicationId());
        userDTO.setConfirmationToken(confirmUserRequest.getConfirmationToken());
        return userDTO;
    }

    public UserRequestForm userDtoToUserRequestForm(UserDTO userDTO){
        UserRequestForm userRequestForm = new UserRequestForm();
        userRequestForm.setMail(userDTO.getEmail());
        userRequestForm.setUsername(userDTO.getUsername());
        userRequestForm.setPassword(userDTO.getPassword());
        userRequestForm.setConfirmationToken(userDTO.getConfirmationToken());
        log.info("UserConverter - userDtoToUserRequestForm END with userDtoToUserRequestForm -> {}", userRequestForm);
        return userRequestForm;
    }

    public LoginResponse userPoToLoginResponse(Exception exception) {
        LoginResponse loginResponse = new LoginResponse();
        com.progettomedusa.user_service.model.response.Error error = new Error();
        error.setCode("CODECODECODE");
        error.setMessage("Somethings gone wrong, check the documentation");
        error.setDomain("MicroServiceFunctional");
        error.setDetailed("Check on the docs with code, domain and status");
        loginResponse.setTimestamp(tools.getInstant());
        loginResponse.setDetailed(BASE_ERROR_DETAILS);
        loginResponse.setError(error);
        return loginResponse;
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


    public UserDTO recoveryUserToDto(UserRecoveryRequest userRecoveryRequest){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userRecoveryRequest.getEmail());
        userDTO.setApplicationId(userRecoveryRequest.getApplicationId());
        return userDTO;
    }

    public UserRecoveryResponse userRecoveryResponse(String message){
        UserRecoveryResponse userRecoveryResponse = new UserRecoveryResponse();
        userRecoveryResponse.setMessage(message);
        userRecoveryResponse.setDomain(userApplicationProperties.getName());
        userRecoveryResponse.setTimestamp(tools.getInstant());
        log.info("UserConverter - createRequestResponse END with createRequestResponse -> {}", userRecoveryResponse);
        return userRecoveryResponse;
    }

    public UserDTO newPasswordRequestToDto(NewPasswordRequest newPasswordRequest){
        UserDTO userDTO = new UserDTO();
        userDTO.setApplicationId(newPasswordRequest.getApplicationId());
        userDTO.setPassword(newPasswordRequest.getEmail());
        userDTO.setToken(newPasswordRequest.getToken());
        log.info("UserConverter - newPasswordRequestToDto END with DTO -> {}", userDTO);
        return userDTO;
    }

    public NewPasswordResponse newPasswordResponse(String message) {
        NewPasswordResponse newPasswordResponse = new NewPasswordResponse();
        newPasswordResponse.setMessage(message);
        newPasswordResponse.setDomain(userApplicationProperties.getName());
        newPasswordResponse.setTimestamp(tools.getInstant());
        if (message.equals("USER_NOT_FOUND")) {
            newPasswordResponse.setDetailed(USER_NOT_FOUND_MESSAGE);
        }
        return newPasswordResponse;
    }


}
