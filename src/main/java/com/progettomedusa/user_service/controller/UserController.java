package com.progettomedusa.user_service.controller;

import com.progettomedusa.user_service.dto.UserDTO;
import com.progettomedusa.user_service.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.progettomedusa.user_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

   // private final UserService userService;

   /* public UserController(UserService userService) {
        this.userService = userService;
    }*/

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        logger.info("Sto creando un nuovo utente:", userDTO);
        {
            try {
                User user = userService.createUser(userDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body("Utente creato con successo! ID: " + user.getId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante la creazione dell'utente: " + e.getMessage());
            }
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Sto recuperando tutti gli utenti:");
        {
            try {
                List<UserDTO> users = userService.getAllUsers();
                logger.debug("Utenti recuperati con successo!");
                return ResponseEntity.ok(users);
            } catch (Exception e) {
                logger.debug("Errore durante il recupero degli utenti!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        logger.info("Sto modificando l'utente con id: " + id);
        {
            try {
                User user = userService.updateUser(id, userDTO);
                return ResponseEntity.ok("Utente aggiornato con successo! ID: " + user.getId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
            }
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        logger.info("Sto eliminando l'utente conn id: " + id);
        {
            try {
                userService.deleteUser(id);
                return ResponseEntity.ok("Utente eliminato con successo! ID: " + id);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore durante l'eliminazione dell'utente: " + e.getMessage());
            }
        }
    }
}