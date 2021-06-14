package com.myhotels.hotel;

import com.myhotels.hotel.dtos.HotelDto;
import com.myhotels.hotel.dtos.errors.ApiError;
import com.myhotels.hotel.repositories.HotelRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HotelServiceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private HotelRepo repo;

	@Test
	void testGetAllActiveHotels() {

		ResponseEntity<List> entity = restTemplate.getForEntity("/hotels/", List.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());

	}

	@Test
	void testGetAllHotelsByStatus() {

		ResponseEntity<Object> entity = restTemplate.getForEntity("/hotels/active/true", Object.class);
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());

		entity = restTemplate.getForEntity("/hotels/active/false", Object.class);
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());

	}

	@Test
	void testGetAllHotelsByStatus_handleNegative() {

		ResponseEntity<ApiError> entity = restTemplate.getForEntity("/hotels/active/abcd", ApiError.class);
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
		assertNotNull(entity.getBody());
		assertEquals("Invalid request. Please check the request and try again",
				entity.getBody().getMessage());
	}

	@Test
	void testGetHotelByName() {

		ResponseEntity<HotelDto> entity = restTemplate
				.getForEntity("/hotels/name/myhotel1", HotelDto.class);
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());

	}

	@Test
	void testGetAvailabilityByNameAndDate() {

		ResponseEntity<Object> entity = restTemplate
				.getForEntity("/hotels/name/myhotel1/date/start/2019-07-20/end/2019-07-31", Object.class);
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
	}

	@Test
	void testGetAvailabilityByNameAndRoomTypeAndDate() {

		ResponseEntity<Object> entity = restTemplate
				.getForEntity("/hotels/name/myhotel1/roomType/queen/date/start/2019-07-20/end/2019-07-31", Object.class);
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
	}
}
