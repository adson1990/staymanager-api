package com.adson.staymanager.repository;

import com.adson.staymanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // uso de optional para evitar NullPointerException

}