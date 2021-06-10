package com.myhotels.hotel.controllers;

import com.myhotels.hotel.dtos.HotelDto;
import com.myhotels.hotel.entities.Hotel;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HotelControllerImpl.class)
public class HotelControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService service;

    @MockBean
    private HotelMapper mapper;

    private ArrayList<Hotel> hotels;
    private HotelDto hotelDto;

    @BeforeEach
    private void setup() {
        hotels = new ArrayList<>();
        Hotel hotel = new Hotel(1234l, "name", "desc", "location", true);
        hotels.add(hotel);
        hotelDto = new HotelDto("name", "desc", "location", true, null);
    }

    @Test
    public void testGetAllAvailableHotels() throws Exception {

        given(service.getAllHotelsByStatus(anyBoolean())).willReturn(hotels);
        given(mapper.hotelToHotelDto(any())).willReturn(hotelDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"name\",\"description\":\"desc\",\"location\":\"location\",\"status\":true,\"rooms\":null}]"));
    }

    @Test
    public void testGetAllActiveHotels_null() throws Exception {
        given(service.getAllHotelsByStatus(anyBoolean())).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

}
