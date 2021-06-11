package com.myhotels.hotel.controllers;

import com.myhotels.hotel.dtos.HotelDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HotelController {
    ResponseEntity<List<HotelDto>> getAllActiveHotels();
    ResponseEntity<List<HotelDto>> getAllHotelsByStatus(boolean active);
}
