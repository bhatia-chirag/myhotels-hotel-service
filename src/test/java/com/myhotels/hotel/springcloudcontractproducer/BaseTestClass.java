package com.myhotels.hotel.springcloudcontractproducer;

import com.myhotels.hotel.controllers.HotelController;
import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.entities.Room;
import com.myhotels.hotel.repositories.AvailabilityRepo;
import com.myhotels.hotel.repositories.HotelRepo;
import com.myhotels.hotel.repositories.RoomRepo;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BaseTestClass {
    @Autowired
    private HotelController hotelController;

    @MockBean
    private HotelRepo repo;
    @MockBean
    private RoomRepo roomRepo;
    @MockBean
    private AvailabilityRepo availabilityRepo;

    @BeforeEach
    public void setup() {
        Hotel savedHotel = new Hotel();
        savedHotel.setName("Myhotel1");
        savedHotel.setStatus(true);
        savedHotel.setDescription("desc1");
        savedHotel.setLocation("loc1");

        Set<Room> rooms = new HashSet<>();
        Room room = new Room();
        room.setRoomType("queen");
        room.setPrice(1000);
        rooms.add(room);
        savedHotel.setRooms(rooms);

        Availability availability = new Availability();
        availability.setAvailable(10);
        availability.setRoom(room);
        availability.setDate(LocalDate.now());
        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(availability);

        when(repo.save(any(Hotel.class))).thenReturn(savedHotel);
        when(roomRepo.findByHotelNameAndRoomTypeAndHotelStatus(anyString(), anyString(), anyBoolean())).thenReturn(room);
        when(availabilityRepo.findByRoomAndDateBetween(any(Room.class), any(), any())).thenReturn(availabilities);
        when(availabilityRepo.saveAll(anyList())).thenReturn(availabilities);

        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(hotelController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
    }
}
