package com.adson.staymanager.mapper;

import org.springframework.stereotype.Component;

import com.adson.staymanager.dto.request.RoomRequestDTO;
import com.adson.staymanager.dto.response.RoomResponseDTO;
import com.adson.staymanager.entity.Room;

@Component
public class RoomMapper {

    public static Room toEntity(RoomRequestDTO dto) {
        return new Room(dto.number(), dto.floor(), dto.dailyRate());
    }

    public static RoomResponseDTO toResponse(Room room) {
        return new RoomResponseDTO(
                room.getId(),
                room.getNumber(),
                room.getFloor(),
                room.getStatus().name(),
                room.getDailyRate().toPlainString()
        );
    }
}