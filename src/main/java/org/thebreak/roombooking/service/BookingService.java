package org.thebreak.roombooking.service;

import org.springframework.data.domain.Page;
import org.thebreak.roombooking.model.Booking;
import org.thebreak.roombooking.model.BookingTimeRange;
import org.thebreak.roombooking.model.bo.BookingBO;
import org.thebreak.roombooking.model.vo.BookingPreviewVO;

import java.util.List;

public interface BookingService {
    BookingPreviewVO add(BookingBO bookingBO);
    Booking findById(String id);
    Page<Booking> findPage(Integer page, Integer size);
    Page<Booking> findPageByUser(String userId, Integer page);
    Page<Booking> findPageActiveBookings(Integer page, Integer size);
    void deleteById(String id);
    Booking updateById(Booking booking);
    // param city can not null, if null, will get city value from db query
    List<BookingTimeRange> findFutureBookedTimesByRoom(String roomId, String city);
    Booking updateStatusById(String id, String status, Long paidAmount);
}
