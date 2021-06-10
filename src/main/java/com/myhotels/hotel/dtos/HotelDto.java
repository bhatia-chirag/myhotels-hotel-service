package com.myhotels.hotel.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {

    private String name;
    private String description;
    private String location;
    private boolean status;
    private List<RoomDto> rooms;

}
