package com.myhotels.hotel.repositories;

import com.myhotels.hotel.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long> {

    List<Hotel> findByStatus(boolean status);
    Hotel findByNameAndStatus(String name, boolean status);
    Hotel findByName(String hotelName);
}
