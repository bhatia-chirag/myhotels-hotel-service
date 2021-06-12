package com.myhotels.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {

    private String name;
    private String description;
    private String location;
    private boolean status;
    private Set<RoomDto> rooms;

}
