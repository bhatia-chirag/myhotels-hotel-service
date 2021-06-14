package com.myhotels.hotel.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myhotels.hotel.utilities.CustomLocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingDto {

    private String hotelName;
    private String roomType;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate startDate;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate endDate;

}
