package com.myhotels.hotel.services;

import com.myhotels.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {
    List<Hotel> getAllHotelsByStatus(boolean status);
    Hotel getHotelByName(String name);
}
