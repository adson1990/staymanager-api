package com.adson.staymanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.adson.staymanager.entity.Booking;
import com.adson.staymanager.entity.BookingStatus;
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

  @AfterEach
    void tearDown() {
    clearInvocations( bookingRepository, roomRepository, guestProfileRepository);
  }

  @InjectMocks BookingService bookingService;

  @Test
  void shouldThrowWhenDatesOverlap() {
    // arrange
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

    // act & assert
    when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
    when(guestProfileRepository.findById(guestId)).thenReturn(Optional.of(guest));
    when(bookingRepository.existsByRoomIdAndStatusInAndCheckOutDateAfterAndCheckInDateBefore(
        eq(roomId), anyList(), eq(checkIn), eq(checkOut) 
    )).thenReturn(true); 

    assertThrows(BusinessRuleException.class,
        () -> bookingService.createReservation(roomId, guestId, checkIn, checkOut));
  }

  @Test
  void shouldReturnCheckInSuccessfully() {
      User user = new User();
      ReflectionTestUtils.setField(user, "id", 1L);
      user.setName("John Doe");
      user.setEmail("john@email.com");
      user.setPassword("123456");
      user.setRole(Role.RECEPCAO);
      user.setActive(true);

      GuestProfile guest = new GuestProfile();
      ReflectionTestUtils.setField(guest, "id", 1L);
      guest.setUser(user);
      guest.setCpf("12345678900");
      guest.setPhone("11999999999");
      guest.setBirthDate(LocalDate.of(1990, 1, 1));
      guest.setCep("12345678");
      guest.setStreet("Main Street");
      guest.setNumber(123);
      guest.setDistrict("Downtown");
      guest.setCity("City");
      guest.setState("State");
        
      Room room = new Room();
      ReflectionTestUtils.setField(room, "id", 1L);
      room.setNumber("101");
      room.setFloor(1);
      room.setDailyRate(new BigDecimal("100.00"));
      room.setStatus(RoomStatus.AVAILABLE);
        
      Booking booking = new Booking(
              guest,
              room,
              LocalDate.of(2026, 7, 1),
              LocalDate.of(2026, 7, 5),
              new BigDecimal("500.00")
      );
      ReflectionTestUtils.setField(booking, "id", 1L);
      booking.setStatus(BookingStatus.RESERVED); 
    
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
      when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
      when(roomRepository.save(any(Room.class))).thenReturn(room);
    
      Booking result = bookingService.checkIn(1L);
    
      assertNotNull(result);
      assertEquals(BookingStatus.CHECKED_IN, result.getStatus());
      assertEquals(RoomStatus.OCCUPIED, room.getStatus());
    
      verify(bookingRepository).findById(1L);
      verify(bookingRepository).save(booking);
      verify(roomRepository).save(room);
    }
}