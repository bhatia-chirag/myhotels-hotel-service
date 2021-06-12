package com.myhotels.hotel.repositories;

import com.myhotels.hotel.entities.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

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
        hotelTrue = new Hotel();
        hotelTrue.setDescription("desc");
        hotelTrue.setStatus(true);
        hotelTrue.setLocation("location");
        hotelTrue.setName("True name");
        hotelFalse = new Hotel();
        hotelFalse.setDescription("desc");
        hotelFalse.setStatus(false);
        hotelFalse.setLocation("location");
        hotelFalse.setName("False name");
        entityManager.persist(hotelTrue);
        entityManager.persist(hotelFalse);
        entityManager.flush();
    }

    @Test
    void testFindByStatus() {
        // when
        List<Hotel> foundTrue = repo.findByStatus(true);
        List<Hotel> foundFalse = repo.findByStatus(false);

        // then
        assertEquals(foundTrue.get(0).getName(), hotelTrue.getName());
        assertEquals(foundFalse.get(0).getName(), hotelFalse.getName());

    }

    @Test
    void testFindByName() {
        // when
        Hotel foundTrue = repo.findByName(hotelTrue.getName());
        Hotel foundFalse = repo.findByName(hotelFalse.getName());

        // then
        assertEquals(hotelTrue.getName(), foundTrue.getName());
        assertEquals(hotelFalse.getName(), foundFalse.getName());
    }

}
