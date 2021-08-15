package org.thebreak.roombooking.controller;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.response.PageResult;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.model.Booking;
import org.thebreak.roombooking.model.BookingTimeRange;
import org.thebreak.roombooking.model.bo.BookingBO;
import org.thebreak.roombooking.model.vo.BookingPreviewVO;
import org.thebreak.roombooking.model.vo.BookingVO;
import org.thebreak.roombooking.service.BookingService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Booking Controller", description = "Controller for booking operations"))
@RequestMapping(value = "api/v1/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping(value = "/add")
    @Operation(summary = "Add a new booking",
            description = "provide roomId and list of booking range")
    public ResponseResult<BookingPreviewVO> addBooking(@RequestBody @Parameter( description = "room details, no need to provide id") BookingBO bookingBO){
        BookingPreviewVO b = bookingService.add(bookingBO);
        return ResponseResult.success(b);
    }

    @GetMapping()
    @Operation(summary = "Get bookings",
            description = "Get paged list of bookings, default is page 1 and size 10 if not provided.")
    public ResponseResult<PageResult<BookingVO>> findBookingsPage(
            @RequestParam @Nullable @Parameter(description = "default is 1 if not provided") Integer page,
            @RequestParam @Nullable @Parameter(description = "Max limited to 50, default is 10 if not provided") Integer size){
        Page<Booking> bookingsPage = bookingService.findPage(page, size);

        // map the list content to VO list
        List<Booking> bookingList = bookingsPage.getContent();
        List<BookingVO> voList = new ArrayList<>();
        for (Booking booking : bookingList) {
            BookingVO bookingVO = new BookingVO();
            BeanUtils.copyProperties(booking, bookingVO);
            voList.add(bookingVO);
        }
        // assemble pageResult
        PageResult<BookingVO> pageResult = new PageResult<>(bookingsPage, voList);

        return ResponseResult.success(pageResult);
    }

    @Operation(summary = "Get booking detail by id",
            description = "id provided as path variable.")
    @GetMapping(value = "/byId/{id}")
    public ResponseResult<BookingVO> getById(@PathVariable String id){
        Booking b = bookingService.findById(id);
        BookingVO bookingVO = new BookingVO();
        BeanUtils.copyProperties(b, bookingVO);
        return ResponseResult.success(bookingVO);
    }

    @GetMapping(value = "/test/{id}")
    public ResponseResult<List<BookingTimeRange>> getTest(@PathVariable String id){
        List<BookingTimeRange> b = bookingService.findFutureBookedTimesByRoom(id, "sydney");

        return ResponseResult.success(b);
    }

    @GetMapping(value = "/getZdt")
    public ResponseResult<ZonedDateTime> getTest(){
//        ZonedDateTime zdt = ZonedDateTime.now();
//        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Australia/Sydney"));

        return ResponseResult.success(zdt);
    }


    @PutMapping(value = "/update")
    @Operation(summary = "Update a booking",
            description = "for admin only, please send over all the fields when update.")
    public ResponseResult<BookingVO> updateById(@RequestBody Booking booking){
        Booking b = bookingService.updateById(booking);
        BookingVO bookingVO = new BookingVO();
        BeanUtils.copyProperties(b, bookingVO);
        return ResponseResult.success(bookingVO);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Delete a booking",
            description = "booking id provided in path variable.")
    public ResponseResult<CommonCode> deleteById(@PathVariable @Nullable String id){
        bookingService.deleteById(id);
        log.debug("Booking deleted.");
        return ResponseResult.success();
    }
}
