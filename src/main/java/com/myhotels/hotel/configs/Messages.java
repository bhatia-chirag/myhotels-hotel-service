package com.myhotels.hotel.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("messages")
public class Messages {

    private String noHotelFound;
    private String hotelNameNotFound;
    private String noActiveHotelFound;
    private String startDateLessThanToday;
    private String startDateGreaterThanEnd;
}
