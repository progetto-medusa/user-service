package com.progettomedusa.user_service.service;

import com.progettomedusa.user_service.model.converter.PMUserConverter;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.exception.DomainMsg;
import com.progettomedusa.user_service.model.exception.ErrorMsg;
import com.progettomedusa.user_service.model.exception.LoginException;
import com.progettomedusa.user_service.model.exception.NewPasswordException;
import com.progettomedusa.user_service.model.po.UserPO;
import com.progettomedusa.user_service.model.request.NewPasswordEmailRequest;
import com.progettomedusa.user_service.model.request.NewPasswordRequest;
import com.progettomedusa.user_service.model.request.ResetPasswordEmailRequest;
import com.progettomedusa.user_service.model.response.*;
import com.progettomedusa.user_service.repository.UserRepository;
import com.progettomedusa.user_service.util.Tools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.util.*;

import static com.progettomedusa.user_service.util.Constants.BASE_ERROR_DETAILS;

@Slf4j
@Service
@RequiredArgsConstructor
public class PMUserService {
    private final UserConverter userConverter;
    private final PMUserConverter pmUserConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExternalCallingService externalCallingService;
    private final Tools tools;
    private final TokenRedisService tokenRedisService;

    //se si riavvia l'applicazione in questo caso questo token viene disabilitato e la creazione utente dovrà ripartire da zero
    private Map<String, String> appToken = new HashMap<>();


    public CreatePMUserResponse createUser(UserDTO userDTO) {
        log.info("Service - createUser START with DTO -> {}", userDTO);

        CreatePMUserResponse createPMUserResponse;
        try {
            log.debug("Service - codifica della password START");
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(encodedPassword);
            userDTO.setValid(false);
            log.debug("Service - codifica della password END");

            log.debug("Service - creazione dello userUuid START");
            String userUuid = UUID.randomUUID().toString();
            userDTO.setUserUuid(userUuid);
            log.debug("Service - creazione dello userUuid END");

            UserPO userToCreate = pmUserConverter.dtoToPo(userDTO);

            UserPO userCreated = userRepository.save(userToCreate);

            String uuid = UUID.randomUUID().toString();
            String key = String.join("#",userCreated.getApplicationId(),uuid);
            //appToken.put(key, userDTO.getEmail());
            tokenRedisService.storeToken(key, userDTO.getEmail());
            userDTO.setConfirmationToken(uuid);

            createPMUserResponse = externalCallingService.createConfirmUser(userDTO);

        } catch (Exception e) {
            log.error("Service - createUser ERROR with message -> {}", e.getMessage());
            createPMUserResponse = pmUserConverter.createPMUserResponse(e);
        }
        log.info("Service - createUser END with response -> {}", createPMUserResponse);

        return createPMUserResponse;
    }

    public GetUsersResponse getUsers() {
        log.info("Service - getUsers START");

        List<UserPO> userList = userRepository.findAll();
        GetUsersResponse getUsersResponse = userConverter.listOfUsersGetUsersResponse(userList);

        log.info("Service - getUsers END with response -> {}", getUsersResponse);
        return getUsersResponse;
    }


    public UpdateUserResponse updateUser(UserDTO userDTO) {
        log.info("Service - updateUser START with dto -> {}", userDTO);
        log.debug("Service - codifica della password START");
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        log.debug("Service - codifica della password END");

        UserPO userToUpdate = pmUserConverter.dtoToPo(userDTO);
        userToUpdate.setUpdateDate(tools.getInstant());
        UserPO currentUser = userRepository.save(userToUpdate);
        UpdateUserResponse updateUserResponse = userConverter.userToUpdateResponse(currentUser);

        log.info("Service - updateUser START with updateUserResponse -> {}", updateUserResponse);
        return updateUserResponse;
    }

    public LoginResponse loginUser(UserDTO userDTO) throws LoginException {
        log.info("Service - loginUser START with DTO -> {}", userDTO);

        String applicationId = userDTO.getApplicationId();
        log.info("Application ID -> {}", applicationId);

        try {
            Optional<UserPO> optionalUser = userRepository.findByEmail(userDTO.getEmail());
            if (optionalUser.isEmpty()) {
                throw new LoginException(
                        ErrorMsg.USRSRV15.getCode(),
                        ErrorMsg.USRSRV15.getMessage(),
                        DomainMsg.MICROSERVICE_FUNCTIONAL.getName(),
                        BASE_ERROR_DETAILS
                );
            }

            UserPO userFound = optionalUser.get();
            userFound.setApplicationId(applicationId);
            userFound.setUpdateDate(tools.getInstant());
            userRepository.save(userFound);

            if (!passwordEncoder.matches(userDTO.getPassword(), userFound.getPassword())) {
                throw new LoginException(
                        ErrorMsg.USRSRV14.getCode(),
                        ErrorMsg.USRSRV14.getMessage(),
                        DomainMsg.MICROSERVICE_FUNCTIONAL.getName(),
                        BASE_ERROR_DETAILS
                );
            }

            if (!userFound.isValid()) {
                throw new LoginException(
                        ErrorMsg.USRSRV16.getCode(),
                        ErrorMsg.USRSRV16.getMessage(),
                        DomainMsg.MICROSERVICE_FUNCTIONAL.getName(),
                        BASE_ERROR_DETAILS
                );
            }

            LoginResponse loginResponse = userConverter.userPoToLoginResponse(userFound);

            log.info("Service - loginUser END with response -> {}", loginResponse);
            return loginResponse;

        } catch (LoginException e) {
            log.error("Login failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during loginUser: {}", e.getMessage(), e);
            return pmUserConverter.userPoToLoginResponse(e); // Consider a fallback strategy here if applicable
        }
    }


    public UserRequestFormResponse confirmUser(UserDTO userDTO){
        UserRequestFormResponse userRequestFormResponse = new UserRequestFormResponse();

        String key = String.join("#",userDTO.getApplicationId(),userDTO.getConfirmationToken());
        String storedKey = tokenRedisService.consumeToken(key);
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

    public UserRecoveryResponse recoveryUser(UserDTO userDTO, String appKeyHeader) {
        log.info("Service - recoveryUser START with DTO -> {}", userDTO);
        UserRecoveryResponse userRecoveryResponse;

        try {
            if (userDTO == null) {
                log.warn("Service - recoveryUser received null userDTO");
                return pmUserConverter.userRecoveryResponse("Invalid input data");
            }

            String applicationId = userDTO.getApplicationId();
            String email = userDTO.getEmail();

            if (applicationId == null || applicationId.isEmpty() || email == null || email.isEmpty()) {
                log.warn("Service - recoveryUser received invalid fields: applicationId={}, email={}", applicationId, email);
                return pmUserConverter.userRecoveryResponse("Application ID and Email are required");
            }

            String recoveryUuid = UUID.randomUUID().toString();
            String key = String.join("#", applicationId, recoveryUuid);

            tokenRedisService.storeToken(key, email);

            NewPasswordEmailRequest newPasswordRequest = new NewPasswordEmailRequest().builder()
                    .email(email)
                    .token(recoveryUuid)
                    .build();

            userRecoveryResponse  = externalCallingService.recoveryUserToEmail(newPasswordRequest, appKeyHeader);

        } catch (Exception e) {
            log.error("Service - recoveryUser ERROR with message -> {}", e.getMessage(), e);
            userRecoveryResponse = pmUserConverter.userRecoveryResponse("Errore imprevisto durante la generazione del Uuid di recupero");
        }

        log.info("Service - recoveryUser END with response -> {}", userRecoveryResponse);
        return userRecoveryResponse;
    }


    public NewPasswordResponse newPassword(UserDTO userDTO, String appKeyHeader) throws NewPasswordException {
        log.info("Service - newPassword START with DTO -> {}", userDTO);

        NewPasswordResponse newPasswordResponse;

        try {

            String key = String.join("#", userDTO.getApplicationId(), userDTO.getToken());

            String email = tokenRedisService.consumeToken(key);

           if (email == null) {
               throw new NewPasswordException(
                       ErrorMsg.USRSRV17.getCode(),
                       ErrorMsg.USRSRV17.getMessage(),
                       DomainMsg.MICROSERVICE_FUNCTIONAL.getName(),
                       BASE_ERROR_DETAILS
               );
            }

            Optional<UserPO> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isEmpty()) {
                throw new NewPasswordException(
                        ErrorMsg.USRSRV15.getCode(),
                        ErrorMsg.USRSRV15.getMessage(),
                        DomainMsg.MICROSERVICE_FUNCTIONAL.getName(),
                        BASE_ERROR_DETAILS
                );
            }

            UserPO user = optionalUser.get();

            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

            user.setPassword(hashedPassword);
            user.setUpdateDate(tools.getInstant());

            userRepository.save(user);

            //appToken.remove(key);

            ResetPasswordEmailRequest resetPasswordEmailRequest = new ResetPasswordEmailRequest().builder()
                    .email(email)
                    .build();

            newPasswordResponse  = externalCallingService.retrieveUserData(resetPasswordEmailRequest, appKeyHeader);

        } catch (Exception e) {
            log.error("Service - newPassword ERROR", e);
            throw new NewPasswordException(
                    ErrorMsg.USRSRV14.getCode(),
                    ErrorMsg.USRSRV14.getMessage(),
                    DomainMsg.MICROSERVICE_FUNCTIONAL.getName(),
                    BASE_ERROR_DETAILS
            );
        }

        log.info("Service - newPassword END with response -> {}", newPasswordResponse);
        return newPasswordResponse;
    }


}
