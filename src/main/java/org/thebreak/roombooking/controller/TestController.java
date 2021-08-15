package org.thebreak.roombooking.controller;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.model.BookingTimeRange;
import org.thebreak.roombooking.model.Booking;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@CrossOrigin
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Room Controller", description = "Controller for Room operations"))
@RequestMapping(value = "api/v1/test")
public class TestController {


    @GetMapping
    public ResponseResult<String> getTestString(){
        return ResponseResult.success("Hello from roombooking server...");
    }

    @GetMapping(value = "/testbooking/{roomId}")
    public ResponseResult<Booking> testPlaceOrder(@PathVariable String roomId, @RequestBody BookingTimeRange timeRange){
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Australia/Sydney"));

        System.out.println(zdt);
        ZonedDateTime zdtPerth = ZonedDateTime.now(ZoneId.of("Australia/Perth"));
        System.out.println(zdtPerth);

        return ResponseResult.success();
    }
}
