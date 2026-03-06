package com.adson.staymanager.controller;

import com.adson.staymanager.controller.v1.RoomController;
import com.adson.staymanager.entity.Room;
import com.adson.staymanager.entity.RoomStatus;
import com.adson.staymanager.mapper.RoomMapper;
import com.adson.staymanager.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
@AutoConfigureMockMvc(addFilters = false)
class RoomControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoomService roomService;

    @MockBean
    private RoomMapper roomMapper;

    @SuppressWarnings("null")
    @Test
    void shouldCreateRoomSuccessfully() throws Exception {
        // arranje
        Room room = new Room();
        ReflectionTestUtils.setField(room, "id", 1L);
        room.setNumber("101");
        room.setFloor(1);
        room.setDailyRate(new BigDecimal("100.00"));
        room.setStatus(RoomStatus.AVAILABLE);

        // act
        when(roomService.create(any(Room.class))).thenReturn(room);

        //assert
        mvc.perform(post("/api/v1/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"number":"101","floor":1,"dailyRate":100.00}
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.number").value("101"))
        .andExpect(jsonPath("$.floor").value(1))
        .andExpect(jsonPath("$.status").value("AVAILABLE"))
        .andExpect(jsonPath("$.dailyRate").value("100.00"));
    }
        
  @Test
  void shouldReturnAllRoomsSuccessfully() throws Exception {
      
        // arranje
          Room r1 = new Room();
          ReflectionTestUtils.setField(r1, "id", 1L);
          r1.setNumber("101");
          r1.setFloor(1);
          r1.setStatus(RoomStatus.AVAILABLE);
          r1.setDailyRate(new BigDecimal("100.00"));
      
          Room r2 = new Room();
          ReflectionTestUtils.setField(r2, "id", 2L);
          r2.setNumber("102");
          r2.setFloor(1);
          r2.setStatus(RoomStatus.MAINTENANCE); 
          r2.setDailyRate(new BigDecimal("150.50"));
      
          // act
          when(roomService.findAll()).thenReturn(List.of(r1, r2));
      
          // assert
          mvc.perform(get("/api/v1/rooms")
                  .accept(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      
              // lista com 2 itens
              .andExpect(jsonPath("$").isArray())
              .andExpect(jsonPath("$.length()").value(2))
      
              // item 0
              .andExpect(jsonPath("$[0].id").value(1))
              .andExpect(jsonPath("$[0].number").value("101"))
              .andExpect(jsonPath("$[0].floor").value(1))
              .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
              .andExpect(jsonPath("$[0].dailyRate").value("100.00"))
      
              // item 1
              .andExpect(jsonPath("$[1].id").value(2))
              .andExpect(jsonPath("$[1].number").value("102"))
              .andExpect(jsonPath("$[1].floor").value(1))
              .andExpect(jsonPath("$[1].status").value("MAINTENANCE"))
              .andExpect(jsonPath("$[1].dailyRate").value("150.50"));
      
          verify(roomService).findAll();
          verifyNoMoreInteractions(roomService);
    }

    @SuppressWarnings("null")
    @Test
    void shouldUpdateRoomSuccessfully() throws Exception {
      // arranje
      Room room = new Room();
      ReflectionTestUtils.setField(room, "id", 1L);
      room.setNumber("101");
      room.setFloor(1);
      room.setDailyRate(new BigDecimal("100.00"));
      room.setStatus(RoomStatus.AVAILABLE);

      // act
      when(roomService.update(any(Long.class), any(Room.class))).thenReturn(room);

      //assert
      mvc.perform(put("/api/v1/rooms/1")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {"number":"101","floor":1,"dailyRate":100.00}
          """))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.number").value("101"))
      .andExpect(jsonPath("$.floor").value(1))
      .andExpect(jsonPath("$.dailyRate").value("100.00"));

    }

    @Test
    void shouldChangeRoomStatusSuccessfully() throws Exception {
        // arranje
        Room room = new Room();
        ReflectionTestUtils.setField(room, "id", 1L);
        room.setNumber("101");
        room.setFloor(1);
        room.setDailyRate(new BigDecimal("100.00"));
        room.setStatus(RoomStatus.MAINTENANCE);

        // act
        when(roomService.changeStatus(any(Long.class), any(RoomStatus.class))).thenReturn(room);

        //assert
        mvc.perform(patch("/api/v1/rooms/1/status")
            .param("status", "MAINTENANCE")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.number").value("101"))
        .andExpect(jsonPath("$.floor").value(1))
        .andExpect(jsonPath("$.dailyRate").value("100.00"))
        .andExpect(jsonPath("$.status").value("MAINTENANCE"));

    }

    @Test
    void shouldReturnAvailableRoomsSuccessfully() throws Exception {
        // arranje
        Room r1 = new Room();
        ReflectionTestUtils.setField(r1, "id", 1L);
        r1.setNumber("101");
        r1.setFloor(1);
        r1.setStatus(RoomStatus.AVAILABLE);
        r1.setDailyRate(new BigDecimal("100.00"));

        Room r2 = new Room();
        ReflectionTestUtils.setField(r2, "id", 2L);
        r2.setNumber("102");
        r2.setFloor(1);
        r2.setStatus(RoomStatus.AVAILABLE); 
        r2.setDailyRate(new BigDecimal("150.50"));

        // act
        when(roomService.findAvailableRooms(any(LocalDate.class), any(LocalDate.class)))
                                            .thenReturn(List.of(r1, r2));

        // assert
        mvc.perform(get("/api/v1/rooms/available")
                .param("checkIn", "2024-07-01")
                .param("checkOut", "2024-07-10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

            // lista com 2 itens
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))

            // item 0
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].number").value("101"))
            .andExpect(jsonPath("$[0].floor").value(1))
            .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
            .andExpect(jsonPath("$[0].dailyRate").value("100.00"))

            // item 1
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].number").value("102"))
            .andExpect(jsonPath("$[1].floor").value(1))
            .andExpect(jsonPath("$[1].status").value("AVAILABLE"))
            .andExpect(jsonPath("$[1].dailyRate").value("150.50"));

        verify(roomService).findAvailableRooms(any(LocalDate.class), any(LocalDate.class));
        verifyNoMoreInteractions(roomService);
    }

}