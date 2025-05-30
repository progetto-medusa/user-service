package com.progettomedusa.user_service.service;

import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.po.UserPO;
import com.progettomedusa.user_service.model.response.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.progettomedusa.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateRequestResponse createUser(UserDTO userDTO) {
        log.info("Service - createUser START with DTO -> {}",userDTO);


        CreateRequestResponse createRequestResponse;
        try {
            log.debug("Service - codifica della password START");
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(encodedPassword);
            log.debug("Service - codifica della password END");

            UserPO userToCreate = userConverter.dtoToPo(userDTO);

            UserPO userCreated = userRepository.save(userToCreate);
            createRequestResponse = userConverter.createRequestResponse(userCreated);
        }catch(Exception e){
            log.error("Service - createUser ERROR with message -> {}",e.getMessage());
            createRequestResponse = userConverter.createRequestResponse(e);
        }
        log.info("Service - createUser END with response -> {}",createRequestResponse);
        return createRequestResponse;
    }

    public GetUsersResponse getUsers() {
        log.info("Service - getUsers START");

        List<UserPO> userList = userRepository.findAll();
        GetUsersResponse getUsersResponse = userConverter.listOfUsersGetUsersResponse(userList);

        log.info("Service - getUsers END with response -> {}",getUsersResponse);
        return getUsersResponse;
    }

    public GetUserResponse getUser(Long id) {
        log.info("Service - getUser START with id -> {}", id);

        Optional<UserPO> userPo = userRepository.findById(id);

        GetUserResponse getUserResponse;
        if(userPo.isPresent()){
            getUserResponse = userConverter.userPoToGetUserResponse(userPo.get(), false);
        }else{
            getUserResponse = userConverter.getUserResponse();
        }

        log.info("Service - getAllUsers END with response -> {}",getUserResponse);
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
}
