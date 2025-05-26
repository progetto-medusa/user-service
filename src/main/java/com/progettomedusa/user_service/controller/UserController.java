package com.progettomedusa.user_service.controller;

import com.progettomedusa.user_service.config.AppProperties;
import com.progettomedusa.user_service.dto.UserDTO;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.response.RestResponse;
import com.progettomedusa.user_service.model.user.User;
import com.progettomedusa.user_service.util.Tools;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.progettomedusa.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final Tools tools;
    private final AppProperties appProperties;


    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers(HttpServletRequest httpServletRequest) {
        log.info("Controller - Recupero lista utenti: START");

        List<UserDTO> userDTOs = userService.getAllUsers();

        List<User> users = new ArrayList<>();

        for (UserDTO userDTO : userDTOs) {
            users.add(userConverter.toEntity(userDTO)); // Usa il metodo esistente toEntity
        }
        RestResponse getUsersResponse = userConverter.buildGetUserResponse(users, null);

        log.info("Controller - Recupero lista utenti: DONE");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }



    @PostMapping("/user")
    public ResponseEntity<Object> createUser(HttpServletRequest httpServletRequest, @RequestBody UserDTO userDTO) {
        log.info("Controller - Creazione utente: START");

        userService.createUser(userDTO);

        RestResponse restResponse = new RestResponse();
        restResponse.setMessage("Utente creato con successo!");
        restResponse.setDetailed("Nome utente creato: " + userDTO.getName());
        restResponse.setDomain(appProperties.getName());
        restResponse.setTimestamp(tools.getInstant());


        log.info("Controller - Creazione utente: DONE");
        return new ResponseEntity<>(restResponse, HttpStatus.CREATED);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Object> updateUser(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody UserDTO userDTO) {
        log.info("Controller - Aggiornamento utente con ID {}: START", id);

        userService.updateUser(id, userDTO);

        RestResponse restResponse = new RestResponse();
        restResponse.setMessage("Utente aggiornato con successo!");
        restResponse.setDetailed("ID utente aggiornato: " + id);
        restResponse.setDomain(appProperties.getName());
        restResponse.setTimestamp(tools.getInstant());

        log.info("Controller - Aggiornamento utente con ID {}: DONE", id);
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> deleteUser(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        log.info("Controller - Eliminazione utente con ID {}: START", id);
        userService.deleteUser(id);

        RestResponse restResponse = new RestResponse();
        restResponse.setMessage("Utente eliminato con successo!");
        restResponse.setDetailed("ID utente eliminato: " + id);
        restResponse.setDomain(appProperties.getName());
        restResponse.setTimestamp(tools.getInstant());

        log.info("Controller - Eliminazione utente con ID {}: DONE", id);
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }
}