package com.myhotels.hotel.mappers;

import com.myhotels.hotel.dtos.AvailabilityDto;
import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface AvailabilityMapper {

    @Mapping(source = "room", target = "roomType", qualifiedByName = "roomToRoomType")
    AvailabilityDto availabilityToAvailabilityDto(Availability availability);

    @Named("roomToRoomType")
    static String roomToRoomType(Room room) {
        return room.getRoomType();
    }
}
