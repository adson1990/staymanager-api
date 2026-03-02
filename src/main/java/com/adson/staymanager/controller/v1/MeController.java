package com.adson.staymanager.controller.v1;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    @GetMapping
    public Object me(Authentication authentication) {
        return authentication; // retorna principal + authorities
    }
}