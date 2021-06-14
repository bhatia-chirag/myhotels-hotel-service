package com.myhotels.hotel.repositories;

import com.myhotels.hotel.entities.Hotel;
import com.myhotels.hotel.entities.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class HotelRepoIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HotelRepo repo;

    private Hotel hotelTrue;
    private Hotel hotelFalse;

    @BeforeEach
    private void setup() {
        // given
        Room roomQueen = new Room();
        roomQueen.setRoomType("queen");
        roomQueen.setPrice(1000);
        roomQueen.setTotal(10);
        Room roomDouble = new Room();
        roomDouble.setRoomType("double");
        roomDouble.setPrice(2000);
        roomDouble.setTotal(5);
        Set<Room> rooms = new HashSet<>();
        rooms.add(roomDouble);
        rooms.add(roomQueen);
        hotelTrue = new Hotel();
        hotelTrue.setDescription("desc");
        hotelTrue.setStatus(true);
        hotelTrue.setLocation("location");
        hotelTrue.setName("True name");
        hotelTrue.setRooms(rooms);
        hotelFalse = new Hotel();
        hotelFalse.setDescription("desc");
        hotelFalse.setStatus(false);
        hotelFalse.setLocation("location");
        hotelFalse.setName("False name");
        hotelFalse.setRooms(rooms);
        entityManager.persist(hotelTrue);
        entityManager.persist(hotelFalse);
        entityManager.flush();
    }

    @Test
    @Disabled
    void testFindByStatus() {
        // when
        List<Hotel> foundTrue = repo.findByStatus(true);
        List<Hotel> foundFalse = repo.findByStatus(false);

        // then
        assertEquals(foundTrue.get(0).getName(), hotelTrue.getName());
        assertEquals(foundFalse.get(0).getName(), hotelFalse.getName());

    }

    @Test
    @Disabled
    void testFindByNameAndStatus() {
        // when
        Hotel foundTrue = repo.findByNameAndStatus(hotelTrue.getName(), true);
        Hotel foundFalse = repo.findByNameAndStatus(hotelFalse.getName(), false);

        // then
        assertEquals(hotelTrue.getName(), foundTrue.getName());
        assertEquals(hotelFalse.getName(), foundFalse.getName());
    }

}
