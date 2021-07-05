package com.myhotels.hotel.controllers;

import com.myhotels.hotel.configs.Messages;
import com.myhotels.hotel.dtos.AvailabilityDto;
import com.myhotels.hotel.dtos.BookingDto;
import com.myhotels.hotel.dtos.HotelBookingDto;
import com.myhotels.hotel.dtos.HotelDto;
import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.exceptions.DataNotFoundException;
import com.myhotels.hotel.exceptions.InvalidRequestException;
import com.myhotels.hotel.mappers.AvailabilityMapper;
import com.myhotels.hotel.mappers.HotelMapper;
import com.myhotels.hotel.services.HotelService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/hotels")
public class HotelControllerImpl implements HotelController {

    @Autowired
    private HotelService service;
    @Autowired
    private HotelMapper hotelMapper;
    @Autowired
    private AvailabilityMapper availabilityMapper;
    @Autowired
    private Messages messages;
    @Autowired
    private KafkaTemplate<String, HotelBookingDto> kafkaTemplate;

    private static final String TOPIC = "hotel_bookings";

    @GetMapping("/")
    @Override
    public ResponseEntity<List<HotelDto>> getAllActiveHotels() {
        List<Hotel> hotels = service.getAllHotelsByStatus(true);
        if (hotels == null || hotels.isEmpty()) {
            throw new DataNotFoundException(messages.getNoHotelFound());
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
            throw new DataNotFoundException(messages.getHotelNameNotFound());
        }
        List<HotelDto> hotelDtos = hotels.stream()
                .map(h -> hotelMapper.hotelToHotelDto(h))
                .collect(Collectors.toList());
        return ResponseEntity.ok(hotelDtos);
    }


    @GetMapping("/name/{name}")
    @Override
    public ResponseEntity<HotelDto> getHotelByName(@PathVariable(name = "name") String name) {
        var hotel = service.getHotelByNameAndStatus(name, true);
        if (hotel == null) {
            throw new DataNotFoundException(messages.getNoActiveHotelFound());
        }
        return ResponseEntity.ok(hotelMapper.hotelToHotelDto(hotel));
    }

    @GetMapping("/name/{name}/date/start/{startDate}/end/{endDate}")
    @Override
    public List<List<AvailabilityDto>> getAvailabilityByNameAndDate(@PathVariable("name") String name, @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        // TODO: Use validator for this
        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidRequestException(messages.getStartDateLessThanToday());
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidRequestException(messages.getStartDateGreaterThanEnd());
        }
        List<List<Availability>> availabilityListsList = service.getAvailabilityByNameAndDateAndStatus(name, startDate, endDate, true);
        if (availabilityListsList == null || availabilityListsList.isEmpty()) {
            throw new DataNotFoundException(messages.getNoActiveHotelFound());
        }
        return availabilityListsList.stream()
                .map(availabilities -> availabilities.stream()
                        .map(availability -> availabilityMapper.availabilityToAvailabilityDto(availability))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @GetMapping("/name/{name}/roomType/{roomType}/date/start/{startDate}/end/{endDate}")
    @Override
    public List<AvailabilityDto> getAvailabilityByNameAndDateAndRoomType(@PathVariable("name") String name, @PathVariable("roomType") String roomType, @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        // TODO: Use validator for this
        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidRequestException(messages.getStartDateLessThanToday());
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidRequestException(messages.getStartDateGreaterThanEnd());
        }
        List<Availability> availabilities = service.getAvailabilityByNameAndRoomTypeAndDateAndStatus(name, roomType, startDate, endDate, true);
        if (availabilities == null || availabilities.isEmpty()) {
            throw new DataNotFoundException(messages.getNoActiveHotelFound());
        }
        return availabilities.stream()
                .map(availability -> availabilityMapper.availabilityToAvailabilityDto(availability))
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    @Override
    public ResponseEntity<HotelDto> addHotel(@RequestBody HotelDto requestHotelDto) {
        var requestHotel = hotelMapper.hotelDtoToHotel(requestHotelDto);
        var responseHotel = service.addHotel(requestHotel);
        var responseDto = hotelMapper.hotelToHotelDto(responseHotel);
        return ResponseEntity
                .created(URI.create("/name/" + responseDto.getName()))
                .body(responseDto);
    }

    @PutMapping("/name/{name}")
    @Override
    public ResponseEntity<HotelDto> editHotel(@PathVariable("name") String name,
                                              @RequestParam Map<String, String> params) {
        var updatedHotel = service.updateHotel(name, params);
        return ResponseEntity.accepted().body(hotelMapper.hotelToHotelDto(updatedHotel));
    }

    @DeleteMapping("/name/{name}")
    @Override
    public ResponseEntity<Void> deleteHotel(@PathVariable("name") String name) {
        service.deactivateHotel(name);
        return ResponseEntity.noContent().lastModified(ZonedDateTime.now()).build();
    }

    @PostMapping("/booking/{bookingId}")
    @Override
    public ResponseEntity<BookingDto> createHotelBooking(@RequestBody BookingDto bookingDto, @PathVariable String bookingId) {
        log.info("Creating booking for bookingId: "+bookingId);
        LocalDate startDate = bookingDto.getStartDate();
        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidRequestException(messages.getStartDateLessThanToday());
        }
        LocalDate endDate = bookingDto.getEndDate();
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new InvalidRequestException(messages.getStartDateGreaterThanEnd());
        }
        service.createBooking(bookingDto.getHotelName(), bookingDto.getRoomType(), startDate, endDate, true);
        var hotelBookingDto = new HotelBookingDto();
        hotelBookingDto.setHotel(bookingDto.getHotelName());
        hotelBookingDto.setBookingId(bookingId);
        hotelBookingDto.setRoomType(bookingDto.getRoomType());
        hotelBookingDto.setEndDate(endDate);
        hotelBookingDto.setStartDate(startDate);
        kafkaTemplate.send(TOPIC, hotelBookingDto );
        return ResponseEntity.ok(bookingDto);
    }
}
