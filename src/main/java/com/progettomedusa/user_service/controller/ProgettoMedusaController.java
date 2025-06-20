package com.progettomedusa.user_service.controller;

import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.exception.ErrorMsg;
import com.progettomedusa.user_service.model.exception.LoginException;
import com.progettomedusa.user_service.model.request.ConfirmUserRequest;
import com.progettomedusa.user_service.model.request.LoginRequest;
import com.progettomedusa.user_service.model.request.ResetPasswordRequest;
import com.progettomedusa.user_service.model.response.LoginResponse;
import com.progettomedusa.user_service.model.response.ResetPasswordResponse;
import com.progettomedusa.user_service.model.response.UserRequestFormResponse;
import com.progettomedusa.user_service.service.UserService;
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

    private final UserService userService;
    private final UserConverter userConverter;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) throws LoginException {
        log.info("Controller - loginUser START with request -> {}", loginRequest);

        UserDTO userDTO = userConverter.loginRequestToUserDTO(loginRequest);
        LoginResponse response = userService.loginUser(userDTO);

        log.info("Controller - loginUser END with response -> {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestHeader("X-APP-KEY") String appKeyHeader, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        log.info("Controller - resetPassword START with request -> {}", resetPasswordRequest);
        UserDTO userDTO = userConverter.resetPasswordRequestToDto(resetPasswordRequest);
        ResetPasswordResponse resetPasswordResponse = userService.resetPassword(userDTO, appKeyHeader);
        log.info("Controller - resetPassword END with response -> {}", resetPasswordRequest);

        if (!resetPasswordResponse.getMessage().equals("User not found")) {
            return new ResponseEntity<>(resetPasswordResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resetPasswordResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/confirm-user")
    public ResponseEntity<UserRequestFormResponse> confirmUser(@Valid @RequestBody ConfirmUserRequest confirmUserRequest){
        log.info("Controller - confirmUser START with request -> {}", confirmUserRequest);

        UserDTO userDTO = userConverter.confirmUserRequestToDto(confirmUserRequest);
        UserRequestFormResponse userRequestFormResponse = userService.confirmUser(userDTO);

        log.info("Controller - confirmUser END with response -> {}", userRequestFormResponse);

        if(userRequestFormResponse == null){
            return new ResponseEntity<>(userRequestFormResponse, HttpStatus.UNAUTHORIZED);
        }else {
            return new ResponseEntity<>(userRequestFormResponse, HttpStatus.OK);
        }
    }

}
