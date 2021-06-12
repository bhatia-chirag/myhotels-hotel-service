package com.myhotels.hotel.services;

import com.myhotels.hotel.dtos.RoomDto;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.entities.Room;
import com.myhotels.hotel.repositories.HotelRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class HotelServiceTests {

    @TestConfiguration
    static class HotelServiceImplTestContextConfiguration {

        @Bean
        public HotelService hotelService() {
            return new HotelServiceImpl();
        }
    }

    @Autowired
    private HotelService service;

    @MockBean
    private HotelRepo repo;

    private ArrayList<Hotel> hotels;
    private Hotel hotel;
    private Set<Room> rooms;
    private Room room1;
    private Set<RoomDto> roomDtos;
    private RoomDto roomDto;

    @BeforeEach
    private void setup() {
        rooms = new HashSet<>();
        room1 = new Room(1L, "double", 10, 2000);
        Room room2 = new Room(2L, "queen", 20, 1000);
        rooms.add(room1);
        rooms.add(room2);
        hotels = new ArrayList<>();
        hotel = new Hotel(1234L, "name", "desc", "location", true, rooms);
        hotels.add(hotel);
        given(repo.findByStatus(anyBoolean())).willReturn(hotels);
        given(repo.findByName(anyString())).willReturn(hotel);
    }

    @Test
    void testGetAllHotelsByStatus_True() {
        List<Hotel> hotelList = service.getAllHotelsByStatus(true);

        assertNotNull(hotelList);
        assertThat(hotelList).hasSameElementsAs(hotels);
    }

    @Test
    void testGetAllHotelsByStatus_False() {
        List<Hotel> hotelList = service.getAllHotelsByStatus(false);

        assertNotNull(hotels);
        assertThat(hotelList).hasSameElementsAs(hotels);
    }

    @Test
    void testGetHotelByName() {
        Hotel hotel1 = service.getHotelByName("name");

        assertNotNull(hotel1);
        assertEquals(hotel, hotel1);
    }

}
