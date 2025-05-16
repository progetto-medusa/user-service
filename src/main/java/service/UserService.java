package service;

import dto.UserDTO;
import model.converter.MainConverter;
import model.user.User;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDTO){
        User user = MainConverter.toEntity(userDTO);
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(MainConverter::userDTO)
                .collect(Collectors.toList());
    }

    public User updateUser(Long id, UserDTO userDTO){
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato, id: " + id));

        currentUser.setName(userDTO.getName());
        currentUser.setEmail(userDTO.getEmail());
        currentUser.setPassword(userDTO.getPassword());
        currentUser.setRole(userDTO.getRole());

        return userRepository.save(currentUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utente non trovato, con id: " + id);
        }
        userRepository.deleteById(id);
    }

}
