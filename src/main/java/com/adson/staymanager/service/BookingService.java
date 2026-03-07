package com.adson.staymanager.service;

import com.adson.staymanager.entity.*;
import com.adson.staymanager.exception.BusinessRuleException;
import com.adson.staymanager.repository.BookingRepository;
import com.adson.staymanager.repository.GuestProfileRepository;
import com.adson.staymanager.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestProfileRepository guestProfileRepository;

    public BookingService(BookingRepository bookingRepository,
                          RoomRepository roomRepository,
                          GuestProfileRepository guestProfileRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.guestProfileRepository = guestProfileRepository;
    }

    @Transactional
    public Booking createReservation(Long roomId, Long guestProfileId,
                                     java.time.LocalDate checkIn,
                                     java.time.LocalDate checkOut) {                              

        if (!checkOut.isAfter(checkIn)) {
            throw new BusinessRuleException("checkOutDate deve ser depois de checkInDate");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessRuleException("Quarto não encontrado"));

        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            throw new BusinessRuleException("Quarto em manutenção não pode ser reservado");
        }

        GuestProfile guest = guestProfileRepository.findById(guestProfileId)
                .orElseThrow(() -> new BusinessRuleException("Hóspede não encontrado"));

        if (guest.getUser().getRole() != Role.CLIENTE) {
            throw new BusinessRuleException("Somente usuários CLIENTE podem ser hóspedes");
        }

        boolean conflict = bookingRepository.existsByRoomIdAndStatusInAndCheckOutDateAfterAndCheckInDateBefore(
                roomId,
                List.of(BookingStatus.RESERVED, BookingStatus.CHECKED_IN),
                checkIn,
                checkOut
        );

        if (conflict) {
            throw new BusinessRuleException("Já existe reserva ativa para este quarto no período informado");
        }

        // calcular total (noites * dailyRate)
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal total = room.getDailyRate().multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking(guest, room, checkIn, checkOut, total);
        booking.setStatus(BookingStatus.RESERVED);

        Booking saved = bookingRepository.save(booking);

        // Atualiza status do quarto: se estava AVAILABLE, vira RESERVED
        room.setStatus(RoomStatus.RESERVED);
        roomRepository.save(room);

        return saved;
    }

    @SuppressWarnings("null")
    @Transactional
    public Booking checkIn(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessRuleException("Reserva não encontrada"));

        if (booking.getStatus() != BookingStatus.RESERVED) {
            throw new BusinessRuleException("Somente reservas em RESERVED podem fazer check-in");
        }

        // Se quarto está em manutenção, impedir check-in
        if (booking.getRoom().getStatus() == RoomStatus.MAINTENANCE) {
            throw new BusinessRuleException("Quarto em manutenção não permite check-in");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        Booking saved = bookingRepository.save(booking);

        Room room = booking.getRoom();
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        return saved;
    }

    @SuppressWarnings("null")
    @Transactional
    public Booking checkOut(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessRuleException("Reserva não encontrada"));

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BusinessRuleException("Somente reservas em CHECKED_IN podem fazer check-out");
        }

        booking.setStatus(BookingStatus.CHECKED_OUT);
        Booking saved = bookingRepository.save(booking);

        Room room = booking.getRoom();
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);

        return saved;
    }

    @SuppressWarnings("null")
    @Transactional
    public Booking cancel(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessRuleException("Reserva não encontrada"));

        if (booking.getStatus() != BookingStatus.RESERVED) {
            throw new BusinessRuleException("Somente reservas em RESERVED podem ser canceladas");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);

        Room room = booking.getRoom();
        // volta a ficar disponível
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);

        return saved;
    }
}
