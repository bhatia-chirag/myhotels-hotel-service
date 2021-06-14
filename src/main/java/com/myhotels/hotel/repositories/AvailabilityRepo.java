package com.myhotels.hotel.repositories;

import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepo extends JpaRepository<Availability, Long> {
    List<Availability> findByRoomAndDateBetween(Room room, LocalDate startDate, LocalDate endDate);
}
