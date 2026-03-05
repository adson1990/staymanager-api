package com.adson.staymanager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adson.staymanager.entity.Booking;
import com.adson.staymanager.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByGuestId(Long guestId);

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByGuestIdOrderByCheckInDateDesc(Long guestId);

    // detectar conflitos de reserva para um quarto específico
    boolean existsByRoomIdAndStatusInAndCheckOutDateAfterAndCheckInDateBefore(
        long roomId,
        Iterable<BookingStatus> statuses,
        LocalDate checkOut,
        LocalDate checkIn
    );

    // total de reservas do cliente
    long countByGuestId(Long guestId);

    // total gasto do cliente (soma totalPrice)
    @Query("select coalesce(sum(a.totalPrice), 0) from Booking a where a.guest.id = :guestId")
    BigDecimal sumTotalPriceByGuestId(@Param("guestId") Long guestId);

    // contagem por status
    long countByGuestIdAndStatus(Long guestId, BookingStatus status);

    boolean existsByGuestIdAndStatus(Long guestId, BookingStatus status);

    // última data de check-in (pela data de check-in)
    @Query("select max(b.checkInDate) from Booking b where b.guest.id = :guestId")
    Optional<LocalDate> findLastCheckInDateByGuestId(@Param("guestId") Long guestId);
}
