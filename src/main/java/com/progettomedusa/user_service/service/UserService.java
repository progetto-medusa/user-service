package com.progettomedusa.user_service.service;

import com.progettomedusa.user_service.dto.UserDTO;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.user.User;
import org.springframework.stereotype.Service;
import com.progettomedusa.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;


    public User createUser(UserDTO userDTO) {
        User user = userConverter.toEntity(userDTO);
        log.debug("DTO convertito in entità User: " + user);
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> allProjectedBy = userRepository.findAllProjectedBy();

        List<UserDTO> responseDto = new ArrayList<>();

        for (User user : allProjectedBy) {
            responseDto.add(userConverter.userDTO(user));
        }
        return responseDto;
    }

    public User updateUser(Long id, UserDTO userDTO) {
        log.debug("Sono nel service per fare le modifiche");
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato, id: " + id));

        currentUser.setName(userDTO.getName());
        currentUser.setEmail(userDTO.getEmail());
        currentUser.setPassword(userDTO.getPassword());
        currentUser.setRole(userDTO.getRole());

        return userRepository.save(currentUser);
    }

    public void deleteUser(Long id) {
        log.debug("Sono nel service per l'eliminazione");
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utente non trovato, con id: " + id);
        }
        userRepository.deleteById(id);
    }

}
