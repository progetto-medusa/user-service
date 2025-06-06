package com.progettomedusa.user_service.service;

import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.po.UserPO;
import com.progettomedusa.user_service.model.request.ResetPasswordRequest;
import com.progettomedusa.user_service.model.response.*;
import com.progettomedusa.user_service.util.Tools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.progettomedusa.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.progettomedusa.user_service.config.MailServiceProperties;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExternalCallingService externalCallingService;
    private final MailServiceProperties mailServiceProperties;
    private final Tools tools;

    public CreateRequestResponse createUser(UserDTO userDTO) {
        log.info("Service - createUser START with DTO -> {}", userDTO);


        CreateRequestResponse createRequestResponse;
        try {
            log.debug("Service - codifica della password START");
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(encodedPassword);
            log.debug("Service - codifica della password END");

            UserPO userToCreate = userConverter.dtoToPo(userDTO);

            UserPO userCreated = userRepository.save(userToCreate);
            createRequestResponse = userConverter.createRequestResponse(userCreated);
        } catch (Exception e) {
            log.error("Service - createUser ERROR with message -> {}", e.getMessage());
            createRequestResponse = userConverter.createRequestResponse(e);
        }
        log.info("Service - createUser END with response -> {}", createRequestResponse);
        return createRequestResponse;
    }

    public GetUsersResponse getUsers() {
        log.info("Service - getUsers START");

        List<UserPO> userList = userRepository.findAll();
        GetUsersResponse getUsersResponse = userConverter.listOfUsersGetUsersResponse(userList);

        log.info("Service - getUsers END with response -> {}", getUsersResponse);
        return getUsersResponse;
    }

    public GetUserResponse getUser(Long id) {
        log.info("Service - getUser START with id -> {}", id);

        Optional<UserPO> userPo = userRepository.findById(id);

        GetUserResponse getUserResponse;
        if (userPo.isPresent()) {
            getUserResponse = userConverter.userPoToGetUserResponse(userPo.get(), false);
        } else {
            getUserResponse = userConverter.getUserResponse();
        }

        log.info("Service - getAllUsers END with response -> {}", getUserResponse);
        return getUserResponse;
    }

    public UpdateUserResponse updateUser(UserDTO userDTO) {
        log.info("Service - updateUser START with dto -> {}", userDTO);
        log.debug("Service - codifica della password START");
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        log.debug("Service - codifica della password END");

        UserPO userToUpdate = userConverter.dtoToPo(userDTO);
        UserPO currentUser = userRepository.save(userToUpdate);
        UpdateUserResponse updateUserResponse = userConverter.userToUpdateResponse(currentUser);

        log.info("Service - updateUser START with updateUserResponse -> {}", updateUserResponse);
        return updateUserResponse;
    }

    public DeleteUserResponse deleteUser(Long id) {
        log.info("Service - deleteUser START with id -> {}", id);
        userRepository.deleteById(id);
        DeleteUserResponse deleteUserResponse = userConverter.deleteUserResponse();
        log.info("Service - deleteUser END with response -> {}", deleteUserResponse);
        return deleteUserResponse;
    }

    public LoginResponse loginUser(UserDTO userDTO) {
        log.info("Service - loginUser START with DTO -> {}", userDTO);


        String applicationId = userDTO.getApplicationId();
        log.info("Application ID -> {}", applicationId);

        LoginResponse loginResponse;

        try {
            Optional<UserPO> optionalUser = userRepository.findByEmail(userDTO.getEmail());
            if (optionalUser.isPresent()) {
                UserPO userFound = optionalUser.get();

                userFound.setApplicationId(userDTO.getApplicationId());

                if (passwordEncoder.matches(userDTO.getPassword(), userFound.getPassword())) {
                    loginResponse = userConverter.userPoToLoginResponse(userFound);

                } else {
                    loginResponse = userConverter.userPoToLoginResponse("WRONG_PASSWORD");
                }
            } else {
                loginResponse = userConverter.userPoToLoginResponse("USER_NOT_FOUND");
            }
        } catch (Exception e) {
            log.error("Service - loginUser ERROR with message -> {}", e.getMessage());
            loginResponse = userConverter.userPoToLoginResponse(e);
        }
        log.info("Service - loginUser END with response -> {}", loginResponse);
        return loginResponse;
    }


    public ResetPasswordResponse resetPassword(UserDTO userDTO, String appKeyHeader) {
        log.info("Service - resetPassword START with DTO -> {}", userDTO);
        ResetPasswordResponse resetPasswordResponse;

        try {
            Optional<UserPO> optionalUser = userRepository.findByEmail(userDTO.getEmail());
            if (optionalUser.isPresent()) {
                UserPO userFound = optionalUser.get();

                String newPassword = tools.generateRandomPassword(10);
                String hashedPassword = passwordEncoder.encode(newPassword);


                userFound.setPassword(hashedPassword);
                userFound.setApplicationId(userDTO.getApplicationId());
                userRepository.save(userFound);

                ResetPasswordRequest request = new ResetPasswordRequest();
                request.setMail(userDTO.getEmail());
                request.setPassword(newPassword);



                resetPasswordResponse = retrieveUserPassword(request, appKeyHeader );
            } else {
                resetPasswordResponse = userConverter.resetPasswordResponse("User not found");
            }

        } catch (Exception e) {
            log.error("Service - resetPassword ERROR with message -> {}", e.getMessage());
            resetPasswordResponse = userConverter.resetPasswordResponse("Unexpected error occurred");
        }

        log.info("Service - resetPassword END with response -> {}", resetPasswordResponse);
        return resetPasswordResponse;
    }

    public ResetPasswordResponse retrieveUserPassword(ResetPasswordRequest resetPasswordRequest, String appKeyHeader) throws IOException {
        String url = String.join("", mailServiceProperties.getUrl(), "/mail-service/reset-password");
        return externalCallingService.retrieveUserData(url, resetPasswordRequest, appKeyHeader);
    }


}
