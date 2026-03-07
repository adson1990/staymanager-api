package com.adson.staymanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.adson.staymanager.entity.Room;
import com.adson.staymanager.entity.RoomStatus;
import com.adson.staymanager.exception.BusinessRuleException;
import com.adson.staymanager.exception.RoomNotFoundException;
import com.adson.staymanager.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock RoomRepository roomRepository;

    @InjectMocks RoomService roomService;

    @Test
    void shouldCreateRoomSuccessfully() {
        // arrange
        Room room = new Room();
        ReflectionTestUtils.setField(room, "id", 1L);
        room.setNumber("101");
        room.setFloor(1);
        room.setDailyRate(BigDecimal.valueOf(100));
        room.setStatus(RoomStatus.AVAILABLE);

        // act
        when(roomRepository.existsByNumber(anyString())).thenReturn(false);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        
        // assert
        Room createdRoom = roomService.create(room);
        assertNotNull(createdRoom);
        assertEquals(1L, createdRoom.getId());

        verify(roomRepository).save(room);
        verify(roomRepository).existsByNumber("101");
    }

    @Test
    void shouldThrowWhenCreatingRoomWithDuplicateNumber() {
        // arrange
        Room room = new Room();
        room.setNumber("101");
        when(roomRepository.existsByNumber(anyString())).thenReturn(true);
        // act and assert
        assertThrows(BusinessRuleException.class, () -> roomService.create(room));
    }

    @Test
    void shouldReturnAllRoomsSuccessfully() {
        // arrange
        Room room1 = new Room();
        ReflectionTestUtils.setField(room1, "id", 1L);
        room1.setNumber("101");
        room1.setFloor(1);
        room1.setDailyRate(BigDecimal.valueOf(100));
        room1.setStatus(RoomStatus.AVAILABLE);

        Room room2 = new Room();
        ReflectionTestUtils.setField(room2, "id", 2L);
        room2.setNumber("102");
        room2.setFloor(1);
        room2.setDailyRate(BigDecimal.valueOf(150));
        room2.setStatus(RoomStatus.OCCUPIED);

        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));

        // act
        var rooms = roomService.findAll();

        // assert
        assertNotNull(rooms);
        assertEquals(2, rooms.size());

        verify(roomRepository).findAll();
    }

    @Test
    void shouldReturnRoomByIdSuccessfully() {
        // arrange
        Room room = new Room();
        ReflectionTestUtils.setField(room, "id", 1L);
        room.setNumber("101");
        room.setFloor(1);
        room.setDailyRate(BigDecimal.valueOf(100));
        room.setStatus(RoomStatus.AVAILABLE);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        // act
        Room foundRoom = roomService.findById(1L);

        // assert
        assertNotNull(foundRoom);
        assertEquals(1L, foundRoom.getId());

        verify(roomRepository).findById(room.getId());
    }

    @Test
    void shouldThrowWhenRoomNotFoundById() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RoomNotFoundException.class, () -> roomService.findById(1L));

        verify(roomRepository).findById(1L);
    }

    @Test
    void shouldUpdateRoomSuccessfully() {
        // arrange
        Room existingRoom = new Room();
        ReflectionTestUtils.setField(existingRoom, "id", 1L);
        existingRoom.setNumber("101");
        existingRoom.setFloor(1);
        existingRoom.setDailyRate(BigDecimal.valueOf(100));
        existingRoom.setStatus(RoomStatus.AVAILABLE);

        Room updatedInfo = new Room();
        updatedInfo.setNumber("102");
        updatedInfo.setFloor(2);
        updatedInfo.setDailyRate(BigDecimal.valueOf(150));

        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(existingRoom);

        // act
        Room updatedRoom = roomService.update(1L, updatedInfo);

        // assert
        assertNotNull(updatedRoom);
        assertEquals("102", updatedRoom.getNumber());
        assertEquals(2, updatedRoom.getFloor());
        assertEquals(RoomStatus.AVAILABLE, updatedRoom.getStatus());
        assertEquals(BigDecimal.valueOf(150), updatedRoom.getDailyRate());

        verify(roomRepository).save(existingRoom);

    }
}