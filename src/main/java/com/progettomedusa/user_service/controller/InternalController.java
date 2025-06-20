package com.progettomedusa.user_service.controller;

import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.request.*;
import com.progettomedusa.user_service.model.response.*;
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
public class InternalController {

    private final UserService userService;
    private final UserConverter userConverter;

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

   /* @PutMapping("/user")
    public ResponseEntity<UpdateUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        log.info("Controller - updateUser START with id -> {}", updateUserRequest);
        UserDTO userDTO = userConverter.updateRequestToDto(updateUserRequest);
        UpdateUserResponse updateUserResponse = userService.updateUser(userDTO);
        log.info("Controller - updateUser END with response -> {}", updateUserResponse);

        return new ResponseEntity<>(updateUserResponse, HttpStatus.OK);
    }
*/
    @DeleteMapping("/user/{id}")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable Long id) {
        log.info("Controller - deleteUser START with id -> {}", id);
        DeleteUserResponse deleteUserResponse = userService.deleteUser(id);
        log.info("Controller - deleteUser END with response -> {}", deleteUserResponse);
        return new ResponseEntity<>(deleteUserResponse, HttpStatus.ACCEPTED);
    }

}

