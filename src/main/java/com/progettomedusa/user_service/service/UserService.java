package com.progettomedusa.user_service.service;

import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.po.UserPO;
import com.progettomedusa.user_service.model.response.*;

import org.springframework.stereotype.Service;
import com.progettomedusa.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserConverter userConverter;
    private final UserRepository userRepository;

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

  /*  public UpdateUserResponse updateUser(UserDTO userDTO) {
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
    }*/

    public DeleteUserResponse deleteUser(Long id) {
        log.info("Service - deleteUser START with id -> {}", id);
        userRepository.deleteById(id);
        DeleteUserResponse deleteUserResponse = userConverter.deleteUserResponse();
        log.info("Service - deleteUser END with response -> {}", deleteUserResponse);
        return deleteUserResponse;
    }

}
