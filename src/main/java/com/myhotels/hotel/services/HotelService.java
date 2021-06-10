package com.myhotels.hotel.services;

import com.myhotels.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {
    public List<Hotel> getAllHotelsByStatus(boolean status);
}
