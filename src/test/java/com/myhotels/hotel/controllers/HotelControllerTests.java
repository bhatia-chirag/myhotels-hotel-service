package com.myhotels.hotel.controllers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhotels.hotel.dtos.AvailabilityDto;
import com.myhotels.hotel.dtos.BookingDto;
import com.myhotels.hotel.dtos.HotelDto;
import com.myhotels.hotel.dtos.RoomDto;
import com.myhotels.hotel.entities.Availability;
import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.entities.Room;
import com.myhotels.hotel.mappers.AvailabilityMapper;
import com.myhotels.hotel.mappers.HotelMapper;
import com.myhotels.hotel.services.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
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
    @MockBean
    private AvailabilityMapper availabilityMapper;

    private List<Hotel> hotels;
    private HotelDto hotelDto;
    private Hotel hotel;
    private Set<Room> rooms;
    private Room room1;

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
        given(service.getHotelByNameAndStatus(anyString(), anyBoolean())).willReturn(hotel);
        given(mapper.hotelToHotelDto(any())).willReturn(hotelDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/" + hotel.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(hotelDto.getName()))
                .andExpect(jsonPath("description").value(hotelDto.getDescription()))
                .andExpect(jsonPath("location").value(hotelDto.getLocation()))
                .andExpect(jsonPath("status").value(hotelDto.isStatus()));
    }

    @Test
    void testGetHotelByName_NotFound() throws Exception {
        given(service.getHotelByNameAndStatus(anyString(), anyBoolean())).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("No active hotel found for name: myhotel0"));
    }

    @Test
    void testGetAvailabilityByNameAndDate() throws Exception {
        Availability availability = new Availability();
        availability.setAvailable(9);
        availability.setRoom(room1);
        List<List<Availability>> availabilitiesList = new ArrayList<>();
        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(availability);
        availabilitiesList.add(availabilities);
        AvailabilityDto availabilityDto = new AvailabilityDto();
        availabilityDto.setAvailable(availability.getAvailable());
        availabilityDto.setRoomType(availability.getRoom().getRoomType());
        given(service.getAvailabilityByNameAndDateAndStatus(anyString(), any(), any(), anyBoolean()))
                .willReturn(availabilitiesList);
        given(availabilityMapper.availabilityToAvailabilityDto(any()))
                .willReturn(availabilityDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel1/date/start/" + LocalDate.now().plusDays(5) + "/end/" + LocalDate.now().plusDays(10)))
                .andExpect(status().isOk())
                .andExpect(content().json("[[{\"roomType\":\"double\",\"date\":null,\"available\":9}]]"));
    }

    @Test
    void testGetAvailabilityByNameAndDate_notFound() throws Exception {
        given(service.getAvailabilityByNameAndDateAndStatus(anyString(), any(), any(), anyBoolean()))
                .willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel1/date/start/" + LocalDate.now().plusDays(1) + "/end/" + LocalDate.now().plusDays(5)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message")
                        .value("Hotel data not found for specified details."));

        given(service.getAvailabilityByNameAndDateAndStatus(anyString(), any(), any(), anyBoolean()))
                .willReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel1/date/start/" + LocalDate.now().plusDays(1) + "/end/" + LocalDate.now().plusDays(5)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message")
                        .value("Hotel data not found for specified details."));
    }

    @Test
    void testGetAvailabilityByNameAndDate_wrongStartDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel1/date/start/" + LocalDate.now().minusDays(1) + "/end/" + LocalDate.now().plusDays(5)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("Start date cannot be less than today"));
    }

    @Test
    void testGetAvailabilityByNameAndDate_wrongEndDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel1/date/start/" + LocalDate.now().plusDays(5) + "/end/" + LocalDate.now().plusDays(3)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Start date cannot be greater than endDate"));
    }

    @Test
    void testGetAvailabilityByNameAndRoomTypeAndDate() throws Exception {
        Availability availability = new Availability();
        availability.setAvailable(9);
        availability.setRoom(room1);
        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(availability);
        AvailabilityDto availabilityDto = new AvailabilityDto();
        availabilityDto.setAvailable(availability.getAvailable());
        availabilityDto.setRoomType(availability.getRoom().getRoomType());
        availabilityDto.setDate(LocalDate.now());
        given(service.getAvailabilityByNameAndRoomTypeAndDateAndStatus(anyString(), anyString(), any(), any(), anyBoolean()))
                .willReturn(availabilities);
        given(availabilityMapper.availabilityToAvailabilityDto(any()))
                .willReturn(availabilityDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel1/roomType/double/date/start/" + LocalDate.now().plusDays(5) + "/end/" + LocalDate.now().plusDays(10)))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"roomType\":\"double\",\"date\":\"" + LocalDate.now() + "\",\"available\":9}]"));
    }

    @Test
    void testGetAvailabilityByNameAndRoomTypeAndDate_notFound() throws Exception {
        given(service.getAvailabilityByNameAndRoomTypeAndDateAndStatus(anyString(), anyString(), any(), any(), anyBoolean()))
                .willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel1/roomType/double/date/start/"
                + LocalDate.now().plusDays(1) + "/end/" + LocalDate.now().plusDays(5)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message")
                        .value("Hotel name not found."));

        given(service.getAvailabilityByNameAndDateAndStatus(anyString(), any(), any(), anyBoolean()))
                .willReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel1/roomType/double/date/start/"
                + LocalDate.now().plusDays(1) + "/end/" + LocalDate.now().plusDays(5)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message")
                        .value("Hotel name not found."));
    }

    @Test
    void testGetAvailabilityByNameAndRoomTypeAndDate_wrongStartDate() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/hotels/name/myhotel1/roomType/double/date/start/"
                                + LocalDate.now().minusDays(1) + "/end/" + LocalDate.now().plusDays(5)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("Start date cannot be less than today"));
    }

    @Test
    void testGetAvailabilityByNameAndRoomTypeAndDate_wrongEndDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/name/myhotel1/roomType/double/date/start/"
                + LocalDate.now().plusDays(5) + "/end/" + LocalDate.now().plusDays(3)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("Start date cannot be greater than endDate"));
    }

    @Test
    void testAddHotel() throws Exception {
        given(mapper.hotelToHotelDto(any())).willReturn(hotelDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/hotels/add")
                .content(new ObjectMapper().writeValueAsString(hotelDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(hotelDto.getName()));
    }

    @Test
    void addHotel_Exception() throws Exception {
        given(service.addHotel(any())).willThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/hotels/add")
                .content(new ObjectMapper().writeValueAsString(hotelDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("message")
                        .value("Invalid request. Correct the root cause and try again."));
    }

    @Test
    void editHotel() throws Exception {
        given(service.updateHotel(anyString(), anyMap())).willReturn(hotel);
        given(mapper.hotelToHotelDto(any())).willReturn(hotelDto);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("description", "updated hotel desc");

        mockMvc.perform(MockMvcRequestBuilders
                .put("/hotels/name/" + hotel.getName())
                .params(map)
        ).andExpect(status().isAccepted())
                .andExpect(jsonPath("description").value(hotelDto.getDescription()));
    }

    @Test
    void testDeleteHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/hotels/name/" + hotel.getName()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateHotelBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setHotelName(hotel.getName());
        bookingDto.setRoomType(room1.getRoomType());
        bookingDto.setStartDate(LocalDate.now());
        bookingDto.setEndDate(LocalDate.now().plusDays(5));

        mockMvc.perform(MockMvcRequestBuilders
        .post("/hotels/booking/bookingId")
        .content(new ObjectMapper().writeValueAsString(bookingDto))
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateHotelBooking_dateLessThanToday() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setHotelName(hotel.getName());
        bookingDto.setRoomType(room1.getRoomType());
        bookingDto.setStartDate(LocalDate.now().minusDays(1));
        bookingDto.setEndDate(LocalDate.now().plusDays(5));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/hotels/booking/bookingId")
                .content(new ObjectMapper().writeValueAsString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Start date cannot be less than today's date."));
    }

    @Test
    void testCreateHotelBooking_startDateLessThanEndDate() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setHotelName(hotel.getName());
        bookingDto.setRoomType(room1.getRoomType());
        bookingDto.setStartDate(LocalDate.now().plusDays(10));
        bookingDto.setEndDate(LocalDate.now().plusDays(5));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/hotels/booking/bookingId")
                .content(new ObjectMapper().writeValueAsString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Start date cannot be less than and equal to end date."));
    }
}
