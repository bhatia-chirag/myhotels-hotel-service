package com.myhotels.hotel.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.myhotels.hotel.controllers"))
                .paths(PathSelectors.ant("/hotels/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Hotel Service REST API",
                "Hotel MicroService API, which is part of group MyHotels.",
                "API v1",
                "Terms of service",
                new Contact("Chirag Bhatia", "www.myhotels.com", "myeaddress@company.com"),
                "License of API", "API license URL", Collections.emptyList());
    }

}
