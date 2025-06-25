package com.progettomedusa.user_service.controller;

import com.progettomedusa.user_service.model.converter.PMUserConverter;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.exception.LoginException;
import com.progettomedusa.user_service.model.exception.NewPasswordException;
import com.progettomedusa.user_service.model.request.*;
import com.progettomedusa.user_service.model.response.*;
import com.progettomedusa.user_service.service.PMUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/progetto-medusa")
@RestController
@RequiredArgsConstructor
public class ProgettoMedusaController {

    private final PMUserService pmUserService;
    private final PMUserConverter pmUserConverter;

    //altro endpoint creazione dell'utente sulla base dell'interfaccia basata sui termini
    @PostMapping("/user")
    public ResponseEntity<CreatePMUserResponse> createUser(@RequestHeader("X-APP-KEY") String appKeyHeader, @Valid @RequestBody CreatePMUserRequest createPMUserRequest) {
        log.info("Controller - createUser START with request -> {}", createPMUserRequest);
        UserDTO userDTO = pmUserConverter.createPMUserRequestToUserDTO(createPMUserRequest);
        CreatePMUserResponse createPMUserResponse = pmUserService.createUser(userDTO);
        log.info("Controller - createUser END with response -> {}", createPMUserResponse);
        return new ResponseEntity<>(createPMUserResponse, HttpStatus.ACCEPTED);
    }


    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) throws LoginException {
        log.info("Controller - loginUser START with request -> {}", loginRequest);

        UserDTO userDTO = pmUserConverter.loginRequestToUserDTO(loginRequest);
        LoginResponse response = pmUserService.loginUser(userDTO);

        log.info("Controller - loginUser END with response -> {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/user/activate")
    public ResponseEntity<UserRequestFormResponse> confirmUser(@Valid @RequestBody ConfirmUserRequest confirmUserRequest){
        log.info("Controller - confirmUser START with request -> {}", confirmUserRequest);

        UserDTO userDTO = pmUserConverter.confirmUserRequestToDto(confirmUserRequest);
        UserRequestFormResponse userRequestFormResponse = pmUserService.confirmUser(userDTO);

        log.info("Controller - confirmUser END with response -> {}", userRequestFormResponse);

        if(userRequestFormResponse == null){
            return new ResponseEntity<>(userRequestFormResponse, HttpStatus.UNAUTHORIZED);
        }else {
            return new ResponseEntity<>(userRequestFormResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/user/recovery")
    public ResponseEntity<UserRecoveryResponse> recoveryUser(@RequestHeader("X-APP-KEY") String appKeyHeader, @Valid @RequestBody UserRecoveryRequest userRecoveryRequest){
        log.info("Controller - recoveryUser START with request -> {}", userRecoveryRequest);

        UserDTO userDTO = pmUserConverter.recoveryUserToDto(userRecoveryRequest);
        UserRecoveryResponse userRecoveryResponse = pmUserService.recoveryUser(userDTO, appKeyHeader);

        log.info("Controller - recoveryUser END with response -> {}", userRecoveryResponse);

        if(userRecoveryResponse == null){
            return new ResponseEntity<>(userRecoveryResponse, HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(userRecoveryResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/user/reset")
    public ResponseEntity<NewPasswordResponse> newPassword(@RequestHeader("X-APP-KEY") String appKeyHeader, @Valid @RequestBody NewPasswordRequest newPasswordRequest) throws NewPasswordException {
        log.info("Controller - newPassword START with request -> {}", newPasswordRequest);

        UserDTO userDTO = pmUserConverter.newPasswordRequestToDto(newPasswordRequest);
        NewPasswordResponse newPasswordResponse = pmUserService.newPassword(userDTO, appKeyHeader);
        log.info("Controller - resetPassword END with response -> {}", newPasswordResponse);
            return new ResponseEntity<>(newPasswordResponse, HttpStatus.OK);
    }
}
