package com.progettomedusa.user_service.repository;

import com.progettomedusa.user_service.model.po.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserPO, Long> {
    Optional<UserPO> findByEmail(String email);
}
