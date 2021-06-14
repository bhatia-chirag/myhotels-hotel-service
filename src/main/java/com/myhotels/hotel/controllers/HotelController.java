package com.myhotels.hotel.controllers;

import com.myhotels.hotel.dtos.AvailabilityDto;
import com.myhotels.hotel.dtos.BookingDto;
import com.myhotels.hotel.dtos.HotelDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HotelController {
    ResponseEntity<List<HotelDto>> getAllActiveHotels();

    ResponseEntity<List<HotelDto>> getAllHotelsByStatus(boolean active);

    ResponseEntity<HotelDto> getHotelByName(String name);

    List<List<AvailabilityDto>> getAvailabilityByNameAndDate(String name, LocalDate startDate, LocalDate endDate);

    List<AvailabilityDto> getAvailabilityByNameAndDateAndRoomType(String name, String roomType, LocalDate startDate, LocalDate endDate);

    ResponseEntity<HotelDto> addHotel(HotelDto requestHotel);

    ResponseEntity<HotelDto> editHotel(String name, Map<String, String> description);

    ResponseEntity<Void> deleteHotel(String name);

    ResponseEntity<BookingDto> createHotelBooking(BookingDto bookingDto, String bookingId);
}
