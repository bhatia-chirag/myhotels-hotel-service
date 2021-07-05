package com.myhotels.hotel.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HotelBookingDto {
    private String hotel;
    private String roomType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String bookingId;
}
