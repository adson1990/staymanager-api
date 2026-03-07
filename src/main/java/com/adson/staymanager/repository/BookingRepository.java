package com.adson.staymanager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.adson.staymanager.entity.Booking;
import com.adson.staymanager.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByRoomIdOrderByCheckInDateDesc(Long roomId, Pageable pageable);

    Page<Booking> findByStatusOrderByCheckInDateDesc(BookingStatus status, Pageable pageable);

    Page<Booking> findByGuestIdOrderByCheckInDateDesc(Long guestId, Pageable pageable);

    Page<Booking> findAllByOrderByCheckInDateDesc(Pageable pageable);

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

    Optional<Booking> findById(Long id);
}
