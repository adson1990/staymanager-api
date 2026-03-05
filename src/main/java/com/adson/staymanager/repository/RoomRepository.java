package com.adson.staymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adson.staymanager.entity.BookingStatus;
import com.adson.staymanager.entity.Room;
import com.adson.staymanager.entity.RoomStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository extends JpaRepository<Room, Long> {
    
    Optional<Room> findByNumber(String number);

    boolean existsByNumber(String number);

     @Query("""
        select distinct a.room.id
        from Booking a
          where a.status in :statuses
          and a.checkInDate < :checkOut
          and a.checkOutDate > :checkIn
        """)
        List<Long> findConflictingRoomIds(
            @Param("statuses") List<BookingStatus> statuses,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    Set<Room> findByStatusNot(RoomStatus status);
}
