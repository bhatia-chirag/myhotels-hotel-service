package com.myhotels.hotel.mappers;

import com.myhotels.hotel.dtos.RoomDto;
import com.myhotels.hotel.entities.Room;
import org.mapstruct.Mapper;

@Mapper
public interface RoomMapper {

    RoomDto roomToRoomDto(Room room);
    Room roomDtoToRoom(RoomDto roomDto);
}
