package com.adson.staymanager.controller.v1;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleTestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "public ok";
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA')")
    @GetMapping("/management")
    public String management() {
        return "management ok";
    }

    @PreAuthorize("hasAnyRole('ADMIN','RECEPCAO','GERENCIA')")
    @GetMapping("/reception")
    public String reception() {
        return "reception ok";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANUTENCAO')")
    @GetMapping("/maintenance")
    public String maintenance() {
        return "maintenance ok";
    }
}