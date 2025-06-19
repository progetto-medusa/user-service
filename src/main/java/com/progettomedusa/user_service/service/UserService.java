package com.progettomedusa.user_service.service;

import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.po.UserPO;
import com.progettomedusa.user_service.model.request.CreateUserRequest;
import com.progettomedusa.user_service.model.request.ResetPasswordRequest;
import com.progettomedusa.user_service.model.response.*;
import com.progettomedusa.user_service.util.Tools;
import org.apache.catalina.User;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.progettomedusa.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.progettomedusa.user_service.config.MailServiceProperties;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExternalCallingService externalCallingService;
    private final Tools tools;

    //se si riavvia l'applicazione in questo caso questo token viene disabilitato e la creazione utente dovrà ripartire da zero
    private Map<String, String> appToken = new HashMap<>();

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
//            createRequestResponse = userConverter.createRequestResponse(userCreated);

//            createRequestResponse = createConfirmUser(userDTO);

            String uuid = UUID.randomUUID().toString();
            String key = String.join("#",userCreated.getApplicationId(),uuid);
            appToken.put(key, userDTO.getEmail());
            userDTO.setConfirmationToken(uuid);

            createRequestResponse = externalCallingService.createConfirmUser(userDTO);

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
        userToUpdate.setUpdateDate(tools.getInstant());
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
                userFound.setUpdateDate(tools.getInstant());

                if (passwordEncoder.matches(userDTO.getPassword(), userFound.getPassword())) {
                    if(userFound.isValid()){
                        loginResponse = userConverter.userPoToLoginResponse(userFound);
                    }else {
                        loginResponse = userConverter.userPoToLoginResponse("USER_NOT_ENABLE");
                    }
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
                userFound.setUpdateDate(tools.getInstant());
                userRepository.save(userFound);

                ResetPasswordRequest request = new ResetPasswordRequest();
                request.setMail(userDTO.getEmail());
                request.setPassword(newPassword);

                resetPasswordResponse = externalCallingService.retrieveUserData(request, appKeyHeader);
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

    public UserRequestFormResponse confirmUser(UserDTO userDTO){
        UserRequestFormResponse userRequestFormResponse = new UserRequestFormResponse();

        String key = String.join("#",userDTO.getApplicationId(),userDTO.getConfirmationToken());
        String storedKey = appToken.get(key);
        if(storedKey.isEmpty()){
            return null;
        }else{
            Optional<UserPO> userToEnable = userRepository.findByEmail(storedKey);
            if(userToEnable.isPresent()){
                UserPO userUpdated = userToEnable.get();
                userUpdated.setValid(true);
                userRepository.save(userUpdated);
            }else{
                return null;
            }
        }

        return userRequestFormResponse;
    }
}
