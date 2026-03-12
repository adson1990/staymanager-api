package com.adson.staymanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.adson.staymanager.controller.v1.BookingController;
import com.adson.staymanager.entity.Booking;
import com.adson.staymanager.entity.BookingStatus;
import com.adson.staymanager.entity.GuestProfile;
import com.adson.staymanager.entity.Role;
import com.adson.staymanager.entity.Room;
import com.adson.staymanager.entity.RoomStatus;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.service.BookingService;

@WebMvcTest(BookingController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void shouldCreateBookingSuccessfully() throws Exception {

        // arranje
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
        guest.setBirthDate(java.time.LocalDate.of(1990, 1, 1));
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
        room.setDailyRate(new java.math.BigDecimal("100.00"));
        room.setStatus(RoomStatus.AVAILABLE);

        Booking booking = new Booking(guest, room, LocalDate.of(2026, 7, 1), 
                                      LocalDate.of(2026, 7, 5), new BigDecimal("500.00"));
        ReflectionTestUtils.setField(booking, "id", 1L);
        booking.setStatus(BookingStatus.RESERVED);

        // act
         when(bookingService.createReservation(any(Long.class), any(Long.class), 
                                                  any(LocalDate.class), any(LocalDate.class))).thenReturn(booking);

        // assert
        mvc.perform(post("/api/v1/bookings")
           .contentType(MediaType.APPLICATION_JSON)
           .content("""
                    {"roomId":1,"guestId":1,"checkInDate":"2026-07-01","checkOutDate":"2026-07-05"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.roomId").value(1L))
            .andExpect(jsonPath("$.guestId").value(1L))
            .andExpect(jsonPath("$.checkInDate").value("2026-07-01"))
            .andExpect(jsonPath("$.checkOutDate").value("2026-07-05"))
            .andExpect(jsonPath("$.totalPrice").value(500.0));
    }

     @Test
     void shouldReturnBookingStatusCheckIn() throws Exception {
        // arranje
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
        guest.setBirthDate(java.time.LocalDate.of(1990, 1, 1));
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
        room.setDailyRate(new java.math.BigDecimal("100.00"));
        room.setStatus(RoomStatus.AVAILABLE);

        Booking booking = new Booking(guest, room, LocalDate.of(2026, 7, 1), 
                                      LocalDate.of(2026, 7, 5), new BigDecimal("500.00"));
        ReflectionTestUtils.setField(booking, "id", 1L);
        booking.setStatus(BookingStatus.CHECKED_IN); 

        // act
         when(bookingService.checkIn(any(Long.class))).thenReturn(booking);

        // assert
        mvc.perform(patch("/api/v1/bookings/1/check-in")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                    {"Id":1}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.status").value("CHECKED_IN"));
    }

    @Test
     void shouldReturnBookingStatusCheckOut() throws Exception {
        // arranje
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
        guest.setBirthDate(java.time.LocalDate.of(1990, 1, 1));
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
        room.setDailyRate(new java.math.BigDecimal("100.00"));
        room.setStatus(RoomStatus.AVAILABLE);

        Booking booking = new Booking(guest, room, LocalDate.of(2026, 7, 1), 
                                      LocalDate.of(2026, 7, 5), new BigDecimal("500.00"));
        ReflectionTestUtils.setField(booking, "id", 1L);
        booking.setStatus(BookingStatus.CHECKED_OUT); 

        // act
         when(bookingService.checkIn(any(Long.class))).thenReturn(booking);

        // assert
        mvc.perform(patch("/api/v1/bookings/1/check-in")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                    {"Id":1}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.status").value("CHECKED_OUT"));
    }

    @Test
     void shouldReturnBookingStatusCancelled() throws Exception {
        // arranje
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
        guest.setBirthDate(java.time.LocalDate.of(1990, 1, 1));
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
        room.setDailyRate(new java.math.BigDecimal("100.00"));
        room.setStatus(RoomStatus.AVAILABLE);

        Booking booking = new Booking(guest, room, LocalDate.of(2026, 7, 1), 
                                      LocalDate.of(2026, 7, 5), new BigDecimal("500.00"));
        ReflectionTestUtils.setField(booking, "id", 1L);
        booking.setStatus(BookingStatus.CANCELLED); 

        // act
         when(bookingService.checkIn(any(Long.class))).thenReturn(booking);

        // assert
        mvc.perform(patch("/api/v1/bookings/1/check-in")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                    {"Id":1}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}