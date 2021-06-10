package com.myhotels.hotel.services;

import com.myhotels.hotel.entities.Hotel;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;

@ExtendWith(SpringExtension.class)
public class HotelServiceTests {

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

    @BeforeEach
    private void setup() {
        hotels = new ArrayList<>();
        Hotel hotel = new Hotel(1234l, "name", "desc", "location", true);
        hotels.add(hotel);
        Mockito.when(repo.findByStatus(anyBoolean())).thenReturn(hotels);
    }

    @Test
    public void testGetAllHotelsByStatus_True() {
        List<Hotel> hotelList = service.getAllHotelsByStatus(true);

        assertNotNull(hotelList);
        assertThat(hotelList).hasSameElementsAs(hotels);
    }

    @Test
    public void testGetAllHotelsByStatus_False() {
        List<Hotel> hotelList = service.getAllHotelsByStatus(false);

        assertNotNull(hotels);
        assertThat(hotelList).hasSameElementsAs(hotels);
    }

}
