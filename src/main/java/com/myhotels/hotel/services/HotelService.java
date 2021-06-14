package com.myhotels.hotel.services;

import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Hotel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HotelService {
    List<Hotel> getAllHotelsByStatus(boolean status);
    Hotel getHotelByNameAndStatus(String name, boolean status);
    List<List<Availability>> getAvailabilityByNameAndDateAndStatus(String name, LocalDate startDate, LocalDate endDate, boolean status);
    List<Availability> getAvailabilityByNameAndRoomTypeAndDateAndStatus(String name, String roomType, LocalDate startDate, LocalDate endDate, boolean b);
    Hotel addHotel(Hotel hotel);
    Hotel updateHotel(String hotelName, Map<String, String> params);
    void deactivateHotel(String name);
}
