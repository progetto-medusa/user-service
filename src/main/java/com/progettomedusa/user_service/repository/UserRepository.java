package com.progettomedusa.user_service.repository;

import com.progettomedusa.user_service.dto.UserDTO;
import com.progettomedusa.user_service.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllProjectedBy();
}
