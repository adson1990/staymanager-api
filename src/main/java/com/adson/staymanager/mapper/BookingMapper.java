package com.adson.staymanager.mapper;

import com.adson.staymanager.dto.response.BookingResponseDTO;
import com.adson.staymanager.entity.Booking;

public class BookingMapper {

    public static BookingResponseDTO toResponse(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getRoom().getId(),
                booking.getGuest().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getStatus().name(),
                booking.getTotalPrice()
        );
    }
}
