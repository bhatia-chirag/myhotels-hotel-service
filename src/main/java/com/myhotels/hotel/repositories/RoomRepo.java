package com.myhotels.hotel.repositories;

import com.myhotels.hotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
    @Query(value = "select ri.* from room_info ri\n" +
            "join hotel_info hi\n" +
            "on ri.hotel_Id = hi.id\n" +
            "where hi.name = :name\n" +
            "and hi.status = :status\n" +
            "and ri.room_type = :roomType", nativeQuery = true)
    Room findByHotelNameAndRoomTypeAndHotelStatus(@Param("name") String name, @Param("roomType") String roomType, @Param("status") boolean status);
}
