package com.adson.staymanager.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingRequestDTO(
        @NotNull(message = "roomId é obrigatório")
        Long roomId,

        @NotNull(message = "guestId é obrigatório")
        Long guestId,

        @NotNull(message = "checkInDate é obrigatório")
        @FutureOrPresent(message = "checkInDate deve ser hoje ou no futuro")
        LocalDate checkInDate,

        @NotNull(message = "checkOutDate é obrigatório")
        LocalDate checkOutDate
) {}
