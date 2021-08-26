package org.thebreak.roombooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.thebreak.roombooking.service.BookingService;
import org.thebreak.roombooking.service.MailSenderService;

import javax.mail.MessagingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerIT {

    @Autowired
    private BookingService bookingService;

    @Value("${braintree.ENVIRONMENT}")
    private String environment;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MailSenderService mailSenderService;

    @Test
    void testEmail() throws MessagingException {
//        mailSenderService.sendSMimiMessage("samuelju2017@gmail.com","Test Mime", "Ryan Ju", "Thank you for blablabla...");
        LocalDateTime time = LocalDateTime.now();
        mailSenderService.sendBookingConfirmEmailNotification("samuelju2017@gmail.com", "Aria Ju", "Huge party room at lake side",time,5, 1999);
    }

    @Test
    void name() {
        System.out.println(environment);

    }

    @Test
    void cityToLocalTime() {
        String currentTime = "2021-08-15T15:17:12.611";
        String city = "sydney";
        String cityCapitalized = city.trim().substring(0, 1).toUpperCase() + city.trim().substring(1).toLowerCase();
        String targetCityZone = "Australia/" + cityCapitalized;
        System.out.println(Instant.now());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(targetCityZone));
//        ZonedDateTime zonedDateTime = utcTime.atZone(ZoneId.of(targetCityZone));

        LocalDateTime ldt = zonedDateTime.toLocalDateTime();
//        ldt.getDayOfWeek();
        System.out.println(ldt);

    }


    @Test
    void findFutureBookedTimesByRoom() {
//        String currentTime = "2021-08-22T05:15+11:00";
        String currentTime = "202108220515";
        long test = Long.parseLong(currentTime);
        System.out.println(test);
    }

    @Test
    void testTimeformate() {
//        String currentTime = "2021-08-22T05:15+11:00";
        String currentTime = "202108220515";
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt);

        ZonedDateTime zdt = ZonedDateTime.now();
        System.out.println(zdt);

    }

    @Test
    void testBookingService() {
//        BookingTimeRange bookingTimeRangeBO1 = new BookingTimeRange("2021-08-14T06:15+11:00","2021-08-14T08:15+11:00");
//        BookingTimeRange bookingTimeRangeBO2 = new BookingTimeRange("2021-08-14T10:15+11:00","2021-08-14T13:15+11:00");
//        List<BookingTimeRange> list = new ArrayList<>();
//        list.add(bookingTimeRangeBO1);
//        list.add(bookingTimeRangeBO2);
//        BookingBO bookingBO = new BookingBO("fakeId", list);
//
//        bookingService.add(bookingBO);

    }


    @Test
    void testBooking() {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Australia/Sydney"));
        System.out.println(zdt);
        ZonedDateTime zdtPerth = ZonedDateTime.now(ZoneId.of("Australia/Perth"));
        System.out.println(zdtPerth);
    }



    @Test
    void testGreaterThan1Hour() {
        // 2011-12-03T20:15+11:00

//        System.out.println(BookingUtils.checkDurationInHour(BookingUtils.convertBoStringToZonedDateTime("2021-08-14T08:15+11:00"), BookingUtils.convertBoStringToZonedDateTime("2021-08-14T11:15+11:00")));
    }

    @Test
    void testGetBookingHours() {
//        ZonedDateTime zdStart = ZonedDateTime.parse(start);
//        ZonedDateTime zdEnd = ZonedDateTime.parse(end);

//        System.out.println(BookingUtils.checkDurationInHour("2021-08-14T08:15+11:00", "2021-08-14T11:15+11:00"));
//        System.out.println(BookingUtils.getBookingHours(start, end));
    }

    @Test
    void testQuoter() {

    }
}