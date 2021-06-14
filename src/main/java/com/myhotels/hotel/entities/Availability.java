package com.myhotels.hotel.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "availability_info")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "availability", nullable = false)
    private int available;

    @ManyToOne(optional = false)
    private Room room;
}
