package com.myhotels.hotel.controllers;

import com.myhotels.hotel.dtos.HotelDto;
import com.myhotels.hotel.dtos.RoomDto;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.entities.Room;
import com.myhotels.hotel.mappers.HotelMapper;
import com.myhotels.hotel.services.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HotelControllerImpl.class)
class HotelControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService service;

    @MockBean
    private HotelMapper mapper;

    private List<Hotel> hotels;
    private HotelDto hotelDto;
    private Hotel hotel;

    @BeforeEach
    private void setup() {
        Set<Room> rooms = new HashSet<>();
        Room room1 = new Room(1L, "double", 10, 2000);
        Room room2 = new Room(2L, "queen", 20, 1000);
        rooms.add(room1);
        hotels = new ArrayList<>();
        hotel = new Hotel(1234L, "name", "desc", "location", true, rooms);
        hotels.add(hotel);
        Set<RoomDto> roomDtos = new HashSet<>();
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomType("double");
        roomDto.setTotal(10);
        roomDto.setPrice(2000);
        roomDtos.add(roomDto);
        hotelDto = new HotelDto("name", "desc", "location", true, roomDtos);
    }

    @Test
    void testGetAllActiveHotels() throws Exception {

        given(service.getAllHotelsByStatus(anyBoolean())).willReturn(hotels);
        given(mapper.hotelToHotelDto(any())).willReturn(hotelDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"name\",\"description\":\"desc\",\"location\":\"location\",\"status\":true,\"rooms\":[{\"roomType\":\"double\",\"total\":10,\"price\":2000}]}]"));
    }

    @Test
    void testGetAllActiveHotels_null() throws Exception {
        given(service.getAllHotelsByStatus(anyBoolean())).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("No hotel found."));
    }

    @Test
    void testGetAllHotelsByStatus() throws Exception {

        given(service.getAllHotelsByStatus(anyBoolean())).willReturn(hotels);
        given(mapper.hotelToHotelDto(any())).willReturn(hotelDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/active/true"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"name\",\"description\":\"desc\",\"location\":\"location\",\"status\":true,\"rooms\":[{\"roomType\":\"double\",\"total\":10,\"price\":2000}]}]"));

    }

    @Test
    void testGetAllHotelsByStatus_null() throws Exception {
        given(service.getAllHotelsByStatus(anyBoolean())).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/active/true"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("No hotel found for specified value."));
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/active/false"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("No hotel found for specified value."));
    }

    @Test
    void testGetAllHotelsByStatus_Exception() throws Exception {
        given(service.getAllHotelsByStatus(anyBoolean())).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/active/abcd"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Invalid request. Please check the request and try again"));
    }

    @Test
    void testGetHotelByName() throws Exception {
        given(service.getHotelByName(anyString())).willReturn(hotel);
        given(mapper.hotelToHotelDto(any())).willReturn(hotelDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/"+hotel.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(hotelDto.getName()))
                .andExpect(jsonPath("description").value(hotelDto.getDescription()))
                .andExpect(jsonPath("location").value(hotelDto.getLocation()))
                .andExpect(jsonPath("status").value(hotelDto.isStatus()));
    }
}
