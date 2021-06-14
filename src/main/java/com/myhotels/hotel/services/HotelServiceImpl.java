package com.myhotels.hotel.services;

import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.entities.Room;
import com.myhotels.hotel.exceptions.DataNotFoundException;
import com.myhotels.hotel.exceptions.InvalidRequestException;
import com.myhotels.hotel.repositories.AvailabilityRepo;
import com.myhotels.hotel.repositories.HotelRepo;
import com.myhotels.hotel.repositories.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Transactional
    @Override
    public Hotel addHotel(Hotel hotel) {
        Hotel createdHotel = repo.save(hotel);
        List<Availability> availabilities = new ArrayList<>();
        createdHotel.getRooms().forEach(room -> {
            for (LocalDate date = LocalDate.now(); date.isBefore(LocalDate.now().plusDays(90)); date = date.plusDays(1)) {
                Availability availability = new Availability();
                availability.setAvailable(room.getTotal());
                availability.setDate(date);
                availability.setRoom(room);
                availabilities.add(availability);
            }
        });
        availabilityRepo.saveAll(availabilities);
        return createdHotel;
    }

    @Override
    public Hotel updateHotel(String hotelName, Map<String, String> params) {
        Hotel updatedHotel = repo.findByName(hotelName);
        if (updatedHotel == null) {
            throw new DataNotFoundException("Hotel not found with specified name");
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            if (value != null) {
                switch (entry.getKey()) {
                    case "description":
                        updatedHotel.setDescription(value);
                        break;
                    case "location":
                        updatedHotel.setLocation(value);
                        break;
                    case "name":
                        updatedHotel.setName(value);
                        break;
                    case "status":
                        updatedHotel.setStatus(Boolean.parseBoolean(value));
                        break;
                    default:
                        throw new InvalidRequestException("Parameter " + entry.getKey() + " is invalid");
                }
            }
        }
        return repo.save(updatedHotel);
    }

    @Transactional
    @Override
    public void deactivateHotel(String hotelName) {
        Hotel hotel = repo.findByNameAndStatus(hotelName, true);
        if (hotel == null) {
            throw new DataNotFoundException("No active hotel exists for specified name.");
        }
        hotel.setStatus(false);
        repo.save(hotel);
    }

    @Transactional
    @Override
    public void createBooking(String hotelName, String roomType, LocalDate startDate, LocalDate endDate, boolean status) {
        List<Availability> availabilityList = getAvailabilityByNameAndRoomTypeAndDateAndStatus(hotelName, roomType, startDate, endDate, status);
        availabilityList.forEach(availability -> {
            if (availability.getAvailable() <= 0) {
                throw new InvalidRequestException("Rooms not available for selected dates.");
            }
        });
        List<Availability> availabilities = availabilityList.stream().map(a -> {
            a.setAvailable(a.getAvailable() - 1);
            return a;
        }).collect(Collectors.toList());
        availabilityRepo.saveAll(availabilities);
    }
}
