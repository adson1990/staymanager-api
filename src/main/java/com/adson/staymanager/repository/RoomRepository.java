package com.adson.staymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.adson.staymanager.entity.Room;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    
    Optional<Room> findByNumber(String number);

    boolean existsByNumber(String number);
}
