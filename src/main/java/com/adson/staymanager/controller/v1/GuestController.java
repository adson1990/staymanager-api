package com.adson.staymanager.controller.v1;

import com.adson.staymanager.dto.request.GuestCreateRequestDTO;
import com.adson.staymanager.entity.GuestProfile;
import com.adson.staymanager.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guests")
public class GuestController {

    private final GuestService service;

    public GuestController(GuestService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GuestProfile create(@Valid @RequestBody GuestCreateRequestDTO dto) {
        return service.createGuest(dto);
    }
}