package com.adson.staymanager.dto.response;

public record RoomResponseDTO (Long id, 
                               String number, 
                               Integer floor, 
                               String status, 
                               String dailyRate) 
{    
}
