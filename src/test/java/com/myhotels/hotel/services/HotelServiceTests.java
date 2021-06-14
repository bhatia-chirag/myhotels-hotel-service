package com.myhotels.hotel.services;

import com.myhotels.hotel.dtos.RoomDto;
import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.entities.Room;
import com.myhotels.hotel.exceptions.DataNotFoundException;
import com.myhotels.hotel.repositories.AvailabilityRepo;
import com.myhotels.hotel.repositories.HotelRepo;
import com.myhotels.hotel.repositories.RoomRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class HotelServiceTests {

    @Autowired
    private HotelService service;
    @MockBean
    private HotelRepo repo;
    @MockBean
    private AvailabilityRepo availabilityRepo;
    @MockBean
    private RoomRepo roomRepo;

    private ArrayList<Hotel> hotels;
    private Hotel hotel;
    private Set<Room> rooms;
    private Room room1;
    private Set<RoomDto> roomDtos;
    private RoomDto roomDto;

    @BeforeEach
    private void setup() {
        rooms = new HashSet<>();
        room1 = new Room();
        room1.setRoomType("double");
        room1.setPrice(2000);
        room1.setTotal(10);
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(2L);
        room2.setRoomType("queen");
        room2.setTotal(20);
        room2.setPrice(1000);
        rooms.add(room1);
        rooms.add(room2);
        hotels = new ArrayList<>();
        hotel = new Hotel(1234L, "name", "desc", "location", true, rooms);
        hotels.add(hotel);
        given(repo.findByStatus(anyBoolean())).willReturn(hotels);
        given(repo.findByNameAndStatus(anyString(), anyBoolean())).willReturn(hotel);
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
    void testGetHotelByNameAndStatus() {
        Hotel hotel1 = service.getHotelByNameAndStatus("name", true);

        assertNotNull(hotel1);
        assertEquals(hotel, hotel1);
    }

    @Test
    void testGetAvailabilityByNameAndDateAndStatus() {
        given(repo.findByNameAndStatus(anyString(), anyBoolean())).willReturn(hotel);
        List<Availability> availabilities = new ArrayList<>();
        Availability availability = new Availability();
        availability.setAvailable(9);
        availability.setRoom(room1);
        availability.setDate(LocalDate.now().plusDays(1));
        given(availabilityRepo.findByRoomAndDateBetween(any(), any(), any()))
                .willReturn(availabilities);

        List<List<Availability>> responseAvailabilitiesList = service.getAvailabilityByNameAndDateAndStatus(
                "name", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), true);
        assertNotNull(responseAvailabilitiesList);
    }

    @Test
    void testGetAvailabilityByNameAndDateAndStatus_exception() {
        given(repo.findByNameAndStatus(anyString(), anyBoolean())).willReturn(null);

        try {
            service.getAvailabilityByNameAndDateAndStatus(
                    "name", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), true);
        } catch (DataNotFoundException e) {
            assertEquals("No active hotel found with name name", e.getMessage());
        }

    }

    @Test
    void testGetAvailabilityByNameAndDateAndStatus_emptyAvailabilities() {
        given(repo.findByNameAndStatus(anyString(), anyBoolean())).willReturn(hotel);
        given(availabilityRepo.findByRoomAndDateBetween(any(), any(), any()))
                .willReturn(null);

        List<List<Availability>> responseAvailabilitiesList = service.getAvailabilityByNameAndDateAndStatus(
                "name", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), true);
        assertTrue(responseAvailabilitiesList.isEmpty());
    }

    @Test
    void testGetAvailabilityByNameAndRoomTypeAndDateAndStatus() {
        given(roomRepo.findByHotelNameAndRoomTypeAndHotelStatus(anyString(), any(), anyBoolean())).willReturn(room1);
        List<Availability> availabilities = new ArrayList<>();
        Availability availability = new Availability();
        availability.setAvailable(9);
        availability.setRoom(room1);
        availability.setDate(LocalDate.now().plusDays(1));
        given(availabilityRepo.findByRoomAndDateBetween(any(), any(), any()))
                .willReturn(availabilities);

        List<Availability> responseAvailabilitiesList = service.getAvailabilityByNameAndRoomTypeAndDateAndStatus(
                "name", room1.getRoomType(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), true);
        assertNotNull(responseAvailabilitiesList);
    }

    @Test
    void testGetAvailabilityByNameAndRoomTypeAndDateAndStatus_exception() {
        given(roomRepo.findByHotelNameAndRoomTypeAndHotelStatus(anyString(), any(), anyBoolean())).willReturn(null);

        try {
            service.getAvailabilityByNameAndRoomTypeAndDateAndStatus(
                    "name", room1.getRoomType(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), true);
        } catch (DataNotFoundException e) {
            assertEquals("No active hotel and room type combination found for hotel with name name and room type double", e.getMessage());
        }

    }

    @Test
    void testGetAvailabilityByNameAndRoomTypeAndDateAndStatus_emptyAvailabilities() {
        given(roomRepo.findByHotelNameAndRoomTypeAndHotelStatus(anyString(), any(), anyBoolean())).willReturn(room1);
        given(availabilityRepo.findByRoomAndDateBetween(any(), any(), any()))
                .willReturn(null);

        List<Availability> responseAvailabilitiesList = service.getAvailabilityByNameAndRoomTypeAndDateAndStatus(
                "name",room1.getRoomType(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), true);
        assertNull(responseAvailabilitiesList);
    }

    @TestConfiguration
    static class HotelServiceImplTestContextConfiguration {

        @Bean
        public HotelService hotelService() {
            return new HotelServiceImpl();
        }
    }

}
