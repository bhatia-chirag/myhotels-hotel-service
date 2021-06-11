package com.myhotels.hotel.repositories;

import com.myhotels.hotel.entities.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class HotelRepoIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HotelRepo repo;

    @Test
    public void testFindByStatus() {
        // given
        Hotel hotelTrue = new Hotel();
        hotelTrue.setDescription("desc");
        hotelTrue.setStatus(true);
        hotelTrue.setLocation("location");
        hotelTrue.setName("name");
        Hotel hotelFalse = new Hotel();
        hotelFalse.setDescription("desc");
        hotelFalse.setStatus(false);
        hotelFalse.setLocation("location");
        hotelFalse.setName("name");
        entityManager.persist(hotelTrue);
        entityManager.persist(hotelFalse);
        entityManager.flush();

        // when
        List<Hotel> foundTrue = repo.findByStatus(true);
        List<Hotel> foundFalse = repo.findByStatus(false);

        // then
        assertEquals(foundTrue.get(0).getName(), hotelTrue.getName());
        assertEquals(foundFalse.get(0).getName(), hotelFalse.getName());

    }

}
