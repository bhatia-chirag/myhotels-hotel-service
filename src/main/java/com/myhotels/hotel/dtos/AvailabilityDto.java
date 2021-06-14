package com.myhotels.hotel.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class AvailabilityDto {
    private String roomType;
    private LocalDate date;
    private int available;
}
