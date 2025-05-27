package com.progettomedusa.user_service.repository;

import com.progettomedusa.user_service.model.po.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserPO, Long> {
}
