package com.adson.staymanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adson.staymanager.entity.GuestProfile;

public interface GuestProfileRepository extends JpaRepository<GuestProfile, Long> {
    
    Optional<GuestProfile> findByCpf(String cpf);   

    Optional<GuestProfile> findByUserId(Long userId);

    boolean existsByCpf(String cpf);
}
