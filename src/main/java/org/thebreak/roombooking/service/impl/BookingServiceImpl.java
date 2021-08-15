package org.thebreak.roombooking.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.AvailableTypeEnum;
import org.thebreak.roombooking.common.BookingStatusEnum;
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.util.BookingUtils;
import org.thebreak.roombooking.dao.BookingRepository;
import org.thebreak.roombooking.dao.RoomRepository;
import org.thebreak.roombooking.model.Booking;
import org.thebreak.roombooking.model.BookingTimeRange;
import org.thebreak.roombooking.model.Room;
import org.thebreak.roombooking.model.bo.BookingBO;
import org.thebreak.roombooking.model.vo.BookingPreviewVO;
import org.thebreak.roombooking.service.BookingService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingRepository repository;

    @Autowired
    RoomRepository roomRepository;


    @Override
    public BookingPreviewVO add(BookingBO bookingBO) {
        checkBookingBoEmptyOrNull(bookingBO);

        // find room detail by id
        Optional<Room> optionalRoom = roomRepository.findById(bookingBO.getRoomId());
        if(!optionalRoom.isPresent()){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        Room room = optionalRoom.get();

        // validate booking times
        // get current time in UTC, and convert to same room zoned time;
        LocalDateTime bookedAtUTC = LocalDateTime.now(ZoneId.of("UTC"));

        long totalBookedHours = 0;

        List<BookingTimeRange> bookingTimeList = bookingBO.getBookingTime();
        for (BookingTimeRange bookingTimeRange : bookingTimeList) {

            LocalDateTime start = bookingTimeRange.getStart();
            LocalDateTime end = bookingTimeRange.getEnd();

            // 1. check start or end time must later than current time
            if(!BookingUtils.checkBookingTimeAfterNow(start, room.getCity())){
                CustomException.cast(CommonCode.BOOKING_TIME_EARLIER_THAN_NOW);
            }
            if(!BookingUtils.checkBookingTimeAfterNow(end, room.getCity())){
                CustomException.cast(CommonCode.BOOKING_TIME_EARLIER_THAN_NOW);
            }


            // 2. check start or end time matches available type (weekend or weekday)
            boolean isWeekendType = room.getAvailableType().equals(AvailableTypeEnum.WEEKEND.getDescription());
            if(isWeekendType){
                // cast exception if type is weekend but booking time is weekday
                 if(!BookingUtils.checkIsWeekend(start) || !BookingUtils.checkIsWeekend(end)){
                     CustomException.cast(CommonCode.BOOKING_WEEKEND_ONLY);
                 }
            } else {
                // cast exception if type is weekday but booking time is weekend
                if(BookingUtils.checkIsWeekend(start) || BookingUtils.checkIsWeekend(end)){
                    CustomException.cast(CommonCode.BOOKING_WEEKDAY_ONLY);
                }
            };

            // 3. check start or end time must be in quarter
            if(!BookingUtils.checkTimeIsQuarter(start) || !BookingUtils.checkTimeIsQuarter(end)){
                CustomException.cast(CommonCode.BOOKING_TIME_QUARTER_ONLY);
            }

            // 4. check booking time must be hourly and at least one hour
            if(!BookingUtils.checkDurationInHour(start, end)){
                CustomException.cast(CommonCode.BOOKING_TIME_HOURLY_ONLY);
            }

            // get hourly duration of each booking
            long bookingHours = BookingUtils.getBookingHours(start, end);
            totalBookedHours += bookingHours;

        }


        String userId = "fakeUserIdFromAuth";
        String status = BookingStatusEnum.UNPAID.getDescription();

        Booking booking = new Booking();
        booking.setBookedAt(bookedAtUTC);
        booking.setTotalHours(totalBookedHours);
        booking.setPaidAmount(0);
        booking.setRoom(room);
        booking.setStatus(status);
        booking.setUserId(userId);
        booking.setBookedTime(bookingTimeList);

        Booking booking1 = repository.save(booking);
        BookingPreviewVO bookingPreviewVO = new BookingPreviewVO();

        BeanUtils.copyProperties(booking1, bookingPreviewVO);
        bookingPreviewVO.setRoomCity(room.getCity());
        bookingPreviewVO.setRoomTitle(room.getTitle());

        return bookingPreviewVO;
    }

    private void checkBookingBoEmptyOrNull(BookingBO bookingBO) {
        if(bookingBO == null){
            CustomException.cast(CommonCode.REQUEST_JSON_MISSING);
        }
        if(bookingBO.getRoomId() == null){
            CustomException.cast(CommonCode.REQUEST_ID_FIELD_MISSING);
        }
        if(bookingBO.getRoomId().isEmpty()){
            CustomException.cast(CommonCode.REQUEST_ID_INVALID_OR_EMPTY);
        }
        if(bookingBO.getBookingTime() == null){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(bookingBO.getBookingTime().isEmpty()){
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<BookingTimeRange> findFutureBookedTimesByRoom(String roomId, String city) {
//        roomId = "61177303c4d79b4693762b7b";"2021-08-21T19:15+11:00"

        LocalDateTime nowAtZonedCity = BookingUtils.getNowAtZonedCity(city).plusDays(7);
        LocalDateTime after7days = nowAtZonedCity.plusDays(7);

        Query query = new Query();
//        query.addCriteria(Criteria.where("room.id").is(roomId)).addCriteria(Criteria.where("room.bookedTime").all("start").is("2021-08-21T19:15+11:00"));
        query.addCriteria(Criteria.where("room.id").is(roomId))
                .addCriteria(Criteria.where("bookedTime").elemMatch(Criteria.where("end").gt(nowAtZonedCity)))
                .fields().include("bookedTime");

        List<Booking> list = mongoTemplate.find(query, Booking.class);

        List<BookingTimeRange> futureBookedTimeList = new ArrayList<>();

        for (Booking booking : list) {
            List<BookingTimeRange> timeList = booking.getBookedTime();
            for (BookingTimeRange bookingTimeRange : timeList) {
                LocalDateTime end = bookingTimeRange.getEnd();
                if(end.isAfter(nowAtZonedCity)){
                    futureBookedTimeList.add(bookingTimeRange);
                }
            }
        }

        return futureBookedTimeList;
    }

    @Override
    public Booking findById(String id) {
        if(id == null){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(id.trim().isEmpty()){
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
        Optional<Booking> optional = repository.findById(id);
        if(!optional.isPresent()){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        return optional.get();
    }

    @Override
    public Page<Booking> findPage(Integer page, Integer size) {
        if(page == null || page < 1){
            page = 1;
        }
        // mongo page start with 0;
        page = page -1;

        if(size == null){
            size = Constants.DEFAULT_PAGE_SIZE;
        }
        if(size > Constants.MAX_PAGE_SIZE){
            size = Constants.MAX_PAGE_SIZE;
        }

        Page<Booking> bookingsPage = repository.findAll(PageRequest.of(page, size, Sort.by("updatedAt").descending()));

        if(bookingsPage.getContent().size() == 0 ){
            CustomException.cast(CommonCode.DB_EMPTY_LIST);
        }
        return bookingsPage;
    }

    @Override
    public void deleteById(String id) {
        if(id == null) {
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(id.trim().isEmpty()){
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
        Optional<Booking> optional = repository.findById(id);
        if(optional.isPresent()){
            repository.deleteById(id);
        } else {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
    }


    @Override
    public Booking updateById(Booking booking) {
        return null;
    }

    @Override
    public Booking updateStatusById(String id, String status) {
        if(id == null || status == null){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(status.trim().isEmpty()){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }

        Optional<Booking> optional = repository.findById(id);
        if(!optional.isPresent()){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        };

        // to implement update ignore null fields
        Booking roomReturn = optional.get();


        return repository.save(roomReturn);

    }
}
