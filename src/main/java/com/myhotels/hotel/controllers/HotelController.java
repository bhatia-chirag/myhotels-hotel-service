package com.myhotels.hotel.controllers;

import com.myhotels.hotel.dtos.HotelDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HotelController {
    public ResponseEntity<List<HotelDto>> getAllActiveHotels();
}
