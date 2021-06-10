package com.myhotels.hotel.mappers;

import com.myhotels.hotel.dtos.HotelDto;
import com.myhotels.hotel.entities.Hotel;
import org.mapstruct.Mapper;

@Mapper
public interface HotelMapper {

    HotelDto hotelToHotelDto(Hotel hotel);

    Hotel hotelDtoToHotel(HotelDto hotelDto);

}
