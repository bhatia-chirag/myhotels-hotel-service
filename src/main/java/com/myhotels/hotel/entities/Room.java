package com.myhotels.hotel.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "room_info")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String roomType;

    @Column(nullable = false)
    private int total;

    @Column(nullable = false)
    private int price;

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

}
