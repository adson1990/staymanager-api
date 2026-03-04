package com.adson.staymanager.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "guest_profiles")
public class GuestProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    String cpf;
    
    String phone;

    @Column(nullable = false)
    LocalDate birthDate;
    
    @Column(nullable = false)
    String cep;
    
    @Column(nullable = false)
    String street;
    
    @Column(nullable = false)
    int number;

    @Column(nullable = false)
    String district;

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    String state;
    
}
