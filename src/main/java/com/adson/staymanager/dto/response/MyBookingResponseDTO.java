package com.adson.staymanager.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MyBookingResponseDTO(
        Long bookingId,
        Long roomId,
        String roomNumber,
        String status,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        BigDecimal totalPrice
) {
    
}
