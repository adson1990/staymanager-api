package com.adson.staymanager.controller.v1;

import com.adson.staymanager.dto.request.BookingRequestDTO;
import com.adson.staymanager.dto.response.BookingResponseDTO;
import com.adson.staymanager.entity.BookingStatus;
import com.adson.staymanager.mapper.BookingMapper;
import com.adson.staymanager.service.BookingService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    // Recepção/Gerência/Admin fazem reservas
    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDTO create(@Valid @RequestBody BookingRequestDTO dto) {
        var booking = service.createReservation(dto.roomId(), dto.guestId(), dto.checkInDate(), dto.checkOutDate());
        return BookingMapper.toResponse(booking);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    @PatchMapping("/{id}/check-in")
    public BookingResponseDTO checkIn(@PathVariable Long id) {
        return BookingMapper.toResponse(service.checkIn(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    @PatchMapping("/{id}/check-out")
    public BookingResponseDTO checkOut(@PathVariable Long id) {
        return BookingMapper.toResponse(service.checkOut(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    @PatchMapping("/{id}/cancel")
    public BookingResponseDTO cancel(@PathVariable Long id) {
        return BookingMapper.toResponse(service.cancel(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    public BookingResponseDTO findById(@PathVariable Long id) {
        return BookingMapper.toResponse(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    public List<BookingResponseDTO> findAll() {
        return service.findAll()
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    public List<BookingResponseDTO> findByRoom(@PathVariable Long roomId) {
        return service.findByRoomId(roomId)
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    public List<BookingResponseDTO> findByGuest(@PathVariable Long guestId) {
        return service.findByGuestId(guestId)
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENCIA','RECEPCAO')")
    public List<BookingResponseDTO> findByStatus(@PathVariable BookingStatus status) {
        return service.findByStatus(status)
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }
    
}