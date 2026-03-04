package com.adson.staymanager.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adson.staymanager.entity.Booking;
import com.adson.staymanager.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByGuestId(Long guestId);

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByStatus(BookingStatus status);

    // detectar conflitos de reserva para um quarto específico
    boolean existsByRoomIdAndStatusInAndCheckOutDateAfterAndCheckInDateBefore(
        Long roomId,
        Iterable<BookingStatus> statuses,
        LocalDate checkOut,
        LocalDate checkIn
    );
    
}
