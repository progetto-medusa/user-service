package com.progettomedusa.user_service.controller;

import com.progettomedusa.user_service.config.AppProperties;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.exception.ErrorMsg;
import com.progettomedusa.user_service.model.po.UserPO;
import com.progettomedusa.user_service.model.request.CreateUserRequest;
import com.progettomedusa.user_service.model.request.UpdateUserRequest;
import com.progettomedusa.user_service.model.request.LoginRequest;
import com.progettomedusa.user_service.model.response.*;
import com.progettomedusa.user_service.util.Tools;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.progettomedusa.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.progettomedusa.user_service.util.Constants.USER_NOT_FOUND_MESSAGE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final Tools tools;
    private final AppProperties appProperties;


    @PostMapping("/user")
    public ResponseEntity<CreateRequestResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.info("Controller - createUser START with request -> {}", createUserRequest);
        UserDTO userDTO = userConverter.createRequestToUserDTO(createUserRequest);
        CreateRequestResponse createRequestResponse = userService.createUser(userDTO);
        log.info("Controller - createUser END with response -> {}", createRequestResponse);
        return new ResponseEntity<>(createRequestResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping("/users")
    public ResponseEntity<GetUsersResponse> getAllUsers() {
        log.info("Controller - getUsers START");
        GetUsersResponse getUsersResponse = userService.getUsers();
        log.info("Controller - getUsers END with response -> {}", getUsersResponse);

        if (getUsersResponse.getDetailed() == null) {
            return new ResponseEntity<>(getUsersResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(getUsersResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable Long id) {
        log.info("Controller - getUser START with id -> {}", id);
        GetUserResponse getUserResponse = userService.getUser(id);
        log.info("Controller - getUser END with response -> {}", getUserResponse);

        if (getUserResponse.getMessage() == null) {
            return new ResponseEntity<>(getUserResponse, HttpStatus.OK);
        } else if (getUserResponse.getMessage().equals(USER_NOT_FOUND_MESSAGE)) {
            return new ResponseEntity<>(getUserResponse, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(getUserResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user")
    public ResponseEntity<UpdateUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        log.info("Controller - updateUser START with id -> {}", updateUserRequest);
        UserDTO userDTO = userConverter.updateRequestToDto(updateUserRequest);
        UpdateUserResponse updateUserResponse = userService.updateUser(userDTO);
        log.info("Controller - updateUser END with response -> {}", updateUserResponse);

        return new ResponseEntity<>(updateUserResponse, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable Long id) {
        log.info("Controller - deleteUser START with id -> {}", id);
        DeleteUserResponse deleteUserResponse = userService.deleteUser(id);
        log.info("Controller - deleteUser END with response -> {}", deleteUserResponse);
        return new ResponseEntity<>(deleteUserResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/progetto-medusa/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        log.info("Controller - loginUser START with request -> {}", loginRequest);
        UserDTO userDTO = userConverter.loginRequestToUserDTO(loginRequest);
        LoginResponse response = userService.loginUser(userDTO);
        if (response.getError() == null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (ErrorMsg.USRSRV15.getCode().equals(response.getError().getCode())) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (ErrorMsg.USRSRV14.getCode().equals(response.getError().getCode())) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

