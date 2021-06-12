package com.myhotels.hotel.controllers;

import com.myhotels.hotel.dtos.HotelDto;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.exceptions.DataNotFoundException;
import com.myhotels.hotel.mappers.HotelMapper;
import com.myhotels.hotel.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotels")
public class HotelControllerImpl implements HotelController {

    @Autowired
    private HotelService service;

    @Autowired
    private HotelMapper hotelMapper;

    @GetMapping("/")
    @Override
    public ResponseEntity<List<HotelDto>> getAllActiveHotels() {
        List<Hotel> hotels = service.getAllHotelsByStatus(true);
        if (hotels == null) {
            throw new DataNotFoundException("No hotel found.");
        } else {
            List<HotelDto> hotelDtos = hotels.stream()
                    .map(h -> hotelMapper.hotelToHotelDto(h))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(hotelDtos);
        }
    }

    @GetMapping("/active/{active}")
    @Override
    public ResponseEntity<List<HotelDto>> getAllHotelsByStatus(@PathVariable(name = "active") boolean active) {
        List<Hotel> hotels = service.getAllHotelsByStatus(active);
        if (hotels == null || hotels.isEmpty()) {
                throw new DataNotFoundException("No hotel found for specified value.");
        } else {
            List<HotelDto> hotelDtos = hotels.stream()
                    .map(h -> hotelMapper.hotelToHotelDto(h))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(hotelDtos);
        }
    }

    @GetMapping("/name/{name}")
    @Override
    public ResponseEntity<HotelDto> getHotelByName(@PathVariable(name = "name") String name) {
        Hotel hotel = service.getHotelByName(name);
        return ResponseEntity.ok(hotelMapper.hotelToHotelDto(hotel));
    }
}
