package com.myhotels.hotel.controllers;

import com.myhotels.hotel.dtos.AvailabilityDto;
import com.myhotels.hotel.dtos.HotelDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface HotelController {
    ResponseEntity<List<HotelDto>> getAllActiveHotels();
    ResponseEntity<List<HotelDto>> getAllHotelsByStatus(boolean active);
    ResponseEntity<HotelDto> getHotelByName(String name);
    List<List<AvailabilityDto>> getAvailabilityByNameAndDate(String name, LocalDate startDate, LocalDate endDate);
    List<AvailabilityDto> getAvailabilityByNameAndDateAndRoomType(String name, String roomType, LocalDate startDate, LocalDate endDate);
}
