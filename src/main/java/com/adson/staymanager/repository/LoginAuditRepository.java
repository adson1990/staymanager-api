package com.adson.staymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adson.staymanager.entity.LoginAudit;

public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long> {

    
}
