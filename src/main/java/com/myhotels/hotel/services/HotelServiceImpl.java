package com.myhotels.hotel.services;

import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.repositories.HotelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepo repo;

    @Override
    public List<Hotel> getAllHotelsByStatus(boolean status) {
        return repo.findByStatus(status);
    }
}
