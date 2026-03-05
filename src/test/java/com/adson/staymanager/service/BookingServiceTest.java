package com.adson.staymanager.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adson.staymanager.entity.GuestProfile;
import com.adson.staymanager.entity.Role;
import com.adson.staymanager.entity.Room;
import com.adson.staymanager.entity.RoomStatus;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.exception.BusinessRuleException;
import com.adson.staymanager.repository.BookingRepository;
import com.adson.staymanager.repository.GuestProfileRepository;
import com.adson.staymanager.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @Mock BookingRepository bookingRepository;
  @Mock RoomRepository roomRepository;
  @Mock GuestProfileRepository guestProfileRepository;

  @InjectMocks BookingService bookingService;

  @Test
  void shouldThrowWhenDatesOverlap() {
    Long roomId = 1L;
    Long guestId = 1L;
    LocalDate checkIn = LocalDate.of(2026, 3, 10);
    LocalDate checkOut = LocalDate.of(2026, 3, 12);

    Room room = new Room();

    room.setStatus(RoomStatus.AVAILABLE);
    room.setDailyRate(new BigDecimal("100.00"));

    GuestProfile guest = new GuestProfile();
    guest.setId(guestId);
    User u = new User();
    u.setRole(Role.CLIENTE);
    guest.setUser(u);

    when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
    when(guestProfileRepository.findById(guestId)).thenReturn(Optional.of(guest));
    when(bookingRepository.existsByRoomIdAndStatusInAndCheckOutDateAfterAndCheckInDateBefore(
        eq(roomId), anyList(), eq(checkIn), eq(checkOut) 
    )).thenReturn(true); 

    assertThrows(BusinessRuleException.class,
        () -> bookingService.createReservation(roomId, guestId, checkIn, checkOut));
  }
}