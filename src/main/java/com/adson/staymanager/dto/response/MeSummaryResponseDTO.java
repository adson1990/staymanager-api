package com.adson.staymanager.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MeSummaryResponseDTO(
        Long totalBookings,
        BigDecimal totalSpent,
        boolean existsReserved,
        Long totalCheckedIn,
        Long totalCheckedOut,
        Long totalCancelled,
        LocalDate lastCheckInDate
) {
    
}
