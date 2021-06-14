package com.myhotels.hotel.services;

import com.myhotels.hotel.dtos.BookingDto;
import com.myhotels.hotel.dtos.RoomDto;
import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.entities.Room;
import com.myhotels.hotel.exceptions.DataNotFoundException;
import com.myhotels.hotel.exceptions.InvalidRequestException;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        availabilitySetup();

        List<Availability> responseAvailabilitiesList = service.getAvailabilityByNameAndRoomTypeAndDateAndStatus(
                "name", room1.getRoomType(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), true);
        assertNotNull(responseAvailabilitiesList);
    }

    private void availabilitySetup() {
        given(roomRepo.findByHotelNameAndRoomTypeAndHotelStatus(anyString(), any(), anyBoolean())).willReturn(room1);
        List<Availability> availabilities = new ArrayList<>();
        Availability availability = new Availability();
        availability.setAvailable(9);
        availability.setRoom(room1);
        availability.setDate(LocalDate.now().plusDays(1));
        availabilities.add(availability);
        given(availabilityRepo.findByRoomAndDateBetween(any(), any(), any()))
                .willReturn(availabilities);
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
                "name", room1.getRoomType(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), true);
        assertNull(responseAvailabilitiesList);
    }

    @Test
    void testAddHotel() {
        given(repo.save(any())).willReturn(hotel);

        Hotel hotel = service.addHotel(this.hotel);
        assertEquals(hotel, this.hotel);
    }

    @Test
    void testUpdateHotel() {
        given(repo.findByName(anyString())).willReturn(this.hotel);
        given(repo.save(any())).willReturn(this.hotel);
        Map<String, String> map = new HashMap<>();
        map.put("description", "updated hotel description");
        map.put("name", "updated hotel description");
        map.put("location", "updated hotel description");
        map.put("status", "updated hotel description");

        Hotel hotel = service.updateHotel(this.hotel.getName(), map);
        assertEquals(this.hotel, hotel);
    }

    @Test
    void testUpdateHotel_notFoundException() {
        given(repo.findByName(anyString())).willReturn(null);

        try {
            service.updateHotel(this.hotel.getName(), new HashMap<>());
        }catch (DataNotFoundException ex) {
            assertEquals("Hotel not found with specified name", ex.getMessage());
        }

    }

    @Test
    void testUpdateHotel_invalidDataException() {
        given(repo.findByName(anyString())).willReturn(this.hotel);

        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("abcd", "1234");
            service.updateHotel(this.hotel.getName(), params);
        } catch (InvalidRequestException ex) {
            assertEquals("Parameter abcd is invalid", ex.getMessage());
        }
    }

    @Test
    void testDeactivateHotel() {
        given(repo.findByNameAndStatus(anyString(), anyBoolean())).willReturn(this.hotel);

        service.deactivateHotel(this.hotel.getName());
        verify(repo, times(1)).save(any());
    }

    @Test
    void testDeactivateHotel_notFound() {
        given(repo.findByNameAndStatus(anyString(), anyBoolean())).willReturn(null);

        try {
            service.deactivateHotel(this.hotel.getName());
        } catch (DataNotFoundException ex) {
            assertEquals("No active hotel exists for specified name.", ex.getMessage());
        }
    }

    @Test
    void testCreateBooking() {
        availabilitySetup();

        service.createBooking(hotel.getName(), room1.getRoomType(), LocalDate.now(), LocalDate.now().plusDays(5), true);
        verify(availabilityRepo, times(1)).saveAll(anyList());
    }

    @Test
    void testCreateBooking_invalidRequest() {
        given(roomRepo.findByHotelNameAndRoomTypeAndHotelStatus(anyString(), any(), anyBoolean())).willReturn(room1);
        List<Availability> availabilities = new ArrayList<>();
        Availability availability = new Availability();
        availability.setAvailable(0);
        availability.setRoom(room1);
        availability.setDate(LocalDate.now().plusDays(1));
        availabilities.add(availability);
        given(availabilityRepo.findByRoomAndDateBetween(any(), any(), any()))
                .willReturn(availabilities);

        try {
            service.createBooking(hotel.getName(), room1.getRoomType(), LocalDate.now(), LocalDate.now().plusDays(5), true);
        } catch (InvalidRequestException ex) {
            assertEquals("Rooms not available for selected dates.", ex.getMessage());
        }
    }

    @TestConfiguration
    static class HotelServiceImplTestContextConfiguration {

        @Bean
        public HotelService hotelService() {
            return new HotelServiceImpl();
        }
    }

}
