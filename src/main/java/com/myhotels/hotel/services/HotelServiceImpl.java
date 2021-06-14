package com.myhotels.hotel.services;

import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.entities.Room;
import com.myhotels.hotel.exceptions.DataNotFoundException;
import com.myhotels.hotel.repositories.AvailabilityRepo;
import com.myhotels.hotel.repositories.HotelRepo;
import com.myhotels.hotel.repositories.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepo repo;
    @Autowired
    private AvailabilityRepo availabilityRepo;
    @Autowired
    private RoomRepo roomRepo;

    @Override
    public List<Hotel> getAllHotelsByStatus(boolean status) {
        return repo.findByStatus(status);
    }

    @Override
    public Hotel getHotelByNameAndStatus(String name, boolean status) {
        return repo.findByNameAndStatus(name, status);
    }

    @Override
    public List<List<Availability>> getAvailabilityByNameAndDateAndStatus(String name, LocalDate startDate, LocalDate endDate, boolean status) {
        Hotel hotel = repo.findByNameAndStatus(name, status);
        if (hotel == null) {
            throw new DataNotFoundException("No active hotel found with name " + name);
        }
        return hotel.getRooms().stream()
                .map(room -> availabilityRepo.findByRoomAndDateBetween(room, startDate, endDate))
                .filter(availabilities -> availabilities != null && !availabilities.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<Availability> getAvailabilityByNameAndRoomTypeAndDateAndStatus(String name, String roomType, LocalDate startDate, LocalDate endDate, boolean status) {
        Room room = roomRepo.findByHotelNameAndRoomTypeAndHotelStatus(name, roomType, status);
        if (room == null) {
            throw new DataNotFoundException("No active hotel and room type combination found for hotel with name " + name + " and room type " + roomType);
        }
        return availabilityRepo.findByRoomAndDateBetween(room, startDate, endDate);
    }
}
