package com.progettomedusa.user_service.controller;

import com.progettomedusa.user_service.model.converter.PMUserConverter;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.exception.LoginException;
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

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestHeader("X-APP-KEY") String appKeyHeader, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        log.info("Controller - resetPassword START with request -> {}", resetPasswordRequest);
        UserDTO userDTO = pmUserConverter.resetPasswordRequestToDto(resetPasswordRequest);
        ResetPasswordResponse resetPasswordResponse = pmUserService.resetPassword(userDTO, appKeyHeader);
        log.info("Controller - resetPassword END with response -> {}", resetPasswordRequest);

        if (!resetPasswordResponse.getMessage().equals("User not found")) {
            return new ResponseEntity<>(resetPasswordResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resetPasswordResponse, HttpStatus.NOT_FOUND);
        }
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

}
