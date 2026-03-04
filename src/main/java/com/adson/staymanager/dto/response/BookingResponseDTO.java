package com.adson.staymanager.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingResponseDTO(
        Long id,
        Long roomId,
        Long guestId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        String status,
        BigDecimal totalPrice
) {}
