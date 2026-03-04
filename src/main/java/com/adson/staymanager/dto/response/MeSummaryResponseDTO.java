package com.adson.staymanager.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MeSummaryResponseDTO(
        Long totalBookings,
        BigDecimal totalSpent,
        Long totalReserved,
        Long totalCheckedIn,
        Long totalCheckedOut,
        Long totalCancelled,
        LocalDate lastCheckInDate
) {
    
}
