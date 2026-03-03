package com.adson.staymanager.controller.v1;

import com.adson.staymanager.dto.request.RoomRequestDTO;
import com.adson.staymanager.dto.response.RoomResponseDTO;
import com.adson.staymanager.entity.Room;
import com.adson.staymanager.entity.RoomStatus;
import com.adson.staymanager.mapper.RoomMapper;
import com.adson.staymanager.service.RoomService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA')")
    @PostMapping
    public RoomResponseDTO create(@Valid @RequestBody RoomRequestDTO dto) {
        Room room = service.create(RoomMapper.toEntity(dto));
        return RoomMapper.toResponse(room);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO','MANUTENCAO')")
    @GetMapping
    public List<RoomResponseDTO> findAll() {
        return service.findAll()
                .stream()
                .map(RoomMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA')")
    @PutMapping("/{id}")
    public RoomResponseDTO update(@PathVariable Long id,
                                   @Valid @RequestBody RoomRequestDTO dto) {

        Room room = service.update(id, RoomMapper.toEntity(dto));
        return RoomMapper.toResponse(room);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','MANUTENCAO')")
    @PatchMapping("/{id}/status")
    public RoomResponseDTO changeStatus(@PathVariable Long id,
                                        @RequestParam RoomStatus status) {

        Room room = service.changeStatus(id, status);
        return RoomMapper.toResponse(room);
    }
}