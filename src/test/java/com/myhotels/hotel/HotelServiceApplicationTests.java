package com.myhotels.hotel;

import com.myhotels.hotel.dtos.HotelDto;
import com.myhotels.hotel.dtos.RoomDto;
import com.myhotels.hotel.dtos.errors.ApiError;
import com.myhotels.hotel.repositories.HotelRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

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

	@Test
	void testAddHotel() {
		HotelDto hotelDto = new HotelDto();
		hotelDto.setLocation("locationTest");
		hotelDto.setName("TestName");
		hotelDto.setDescription("TestDesc");
		hotelDto.setStatus(true);
		Set<RoomDto> set = new HashSet<>();
		RoomDto dto1 = new RoomDto();
		dto1.setTotal(10);
		dto1.setRoomType("queen");
		dto1.setPrice(1000);
		RoomDto dto2 = new RoomDto();
		dto2.setPrice(2000);
		dto2.setTotal(5);
		dto2.setRoomType("double");
		set.add(dto1);
		set.add(dto2);
		hotelDto.setRooms(set);
		ResponseEntity<HotelDto> entity = restTemplate.postForEntity("/hotels/add", hotelDto, HotelDto.class);
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
	}
}
