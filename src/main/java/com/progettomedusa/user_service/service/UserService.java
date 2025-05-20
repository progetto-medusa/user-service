package com.progettomedusa.user_service.service;

import com.progettomedusa.user_service.dto.UserDTO;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.user.User;
import org.springframework.stereotype.Service;
import com.progettomedusa.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDTO) {
        User user = UserConverter.toEntity(userDTO);
        logger.debug("DTO convertito in entità User: " + user);
        return userRepository.save(user);
    }

    //stream usato nel primo approccio
  /*  public List<UserDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(MainConverter::userDTO)
                .collect(Collectors.toList());
    }*/
    //metodo nuovo creato nel repository per sostituire lo stream
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllProjectedBy();
    }

    public User updateUser(Long id, UserDTO userDTO) {
        logger.debug("Sono nel service per fare le modifiche");
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato, id: " + id));

        currentUser.setName(userDTO.getName());
        currentUser.setEmail(userDTO.getEmail());
        currentUser.setPassword(userDTO.getPassword());
        currentUser.setRole(userDTO.getRole());

        return userRepository.save(currentUser);
    }

    public void deleteUser(Long id) {
        logger.debug("Sono nel service per l'eliminazione");
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utente non trovato, con id: " + id);
        }
        userRepository.deleteById(id);
    }

}
