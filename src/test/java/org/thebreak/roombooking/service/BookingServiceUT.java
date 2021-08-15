package org.thebreak.roombooking.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.dao.BookingRepository;
import org.thebreak.roombooking.model.Booking;
import org.thebreak.roombooking.service.impl.BookingServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest
@Disabled
@ExtendWith(MockitoExtension.class)  // same as using MockitoAnnotations.initMocks(this);
class BookingServiceUT {

    @InjectMocks
    BookingServiceImpl bookingService;  // dependencies with @Mock annotation will be injected to this service

    @Mock
    BookingRepository repository;

    @Captor
    ArgumentCaptor<Pageable> pageCaptor;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    private List<Booking> getMockedBookingList() {
//        List<Booking> bookingList = new ArrayList<>();
//        Booking booking1 = new Booking("id123456", null, null, "paid", 19999, null);
//        Booking booking2 = new Booking("id12345677", null, null, "unpaid", 29999, null);
//        bookingList.add(booking1);
//        bookingList.add(booking2);
//        return bookingList;

        return null;
    }


    @Test
    void addBooking_withValidBookingFields_shouldReturnBooking() {
//        BookingTimeRangeBO booking = new BookingTimeRangeBO(null, null);
//        when(repository.save(booking)).thenReturn(booking);
//        Booking savedBooking = bookingService.add(booking);
//        assertThat(savedBooking).isNotNull();
//        verify(repository).save(booking);
    }

//    @Test
//    void addBooking_withMissingRoomIdField_shouldThrowMissingFieldException() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.add(booking)).getCommonCode();
//        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
//    }

//    @Test
//    void addBooking_withMissingAddressField_shouldThrowMissingFieldException() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.add(booking)).getCommonCode();
//        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
//    }
//    @Test
//    void addBooking_withMissingBookingNumberField_shouldThrowMissingFieldException() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.add(booking)).getCommonCode();
//        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
//    }
//
//    @Test
//    void addBooking_withEmptyTitleField_shouldThrowEmptyFieldException() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.add(booking)).getCommonCode();
//        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
//    }
//    @Test
//    void addBooking_withEmptyAddressField_shouldThrowEmptyFieldException() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.add(booking)).getCommonCode();
//        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
//    }

    @Test
    void findBookingsPage_withoutPageAndSize_shouldUseDefaultPageAndSize() {
        List<Booking> bookingList = getMockedBookingList();

        // use PageImpl to create paged booking list;
        Page<Booking> pagedBookings = new PageImpl(bookingList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedBookings);

        // controller default is page=1, size=DEFAULT_PAGE_SIZE if no params provided;
        List<Booking> findBookingList = bookingService.findPage(1, Constants.DEFAULT_PAGE_SIZE).getContent();

        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size are 0 and 5 by default(1 and 5 default by controller)
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(Constants.DEFAULT_PAGE_SIZE);

        // check the return list size is correct
        assertThat(findBookingList.size()).isEqualTo(2);
    }
    @Test
    void indBookingsPage_withPageAndSize_shouldListCorrectPageAndSize() {
        List<Booking> bookingList = getMockedBookingList();

        // use PageImpl to create paged booking list;
        Page<Booking> pagedBookings = new PageImpl(bookingList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedBookings);

        List<Booking> findBookingList = bookingService.findPage(1, 5).getContent();

        // check that the repository method is called with correct page and size;
        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(5);

        // check the return list size is correct
        assertThat(findBookingList.size()).isEqualTo(2);
    }

    @Test
    void findBookingsPage_withSizeExceedingMaxSizeLimit_shouldUseDefinedMaxSize() {
        List<Booking> bookingList = getMockedBookingList();

        // use PageImpl to create paged booking list;
        Page<Booking> pagedBookings = new PageImpl(bookingList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedBookings);

        // pass in a size bigger than Max size (50)
        List<Booking> findBookingList = bookingService.findPage(1, 200).getContent();

        // check that the repository method is called with max size limit;
        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(Constants.MAX_PAGE_SIZE);
    }

    @Test
    void findBookingById_withValidId_shouldReturnBooking() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        when(repository.findById(any())).thenReturn(Optional.of(booking));
//
//        bookingService.findById("validId");
//
//        verify(repository).findById(stringCaptor.capture());
//        String id = stringCaptor.getValue();
//        assertThat(id).isEqualTo("validId");
    }

    @Test
    void findBookingById_withMissingId_shouldThrowMissingFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> bookingService.findById(null)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }

    @Test
    void findBookingById_withEmptyId_shouldThrowEmptyFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> bookingService.findById("")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
    }

    @Test
    void findBookingById_withInValidId_shouldThrowNotFoundException() {
        when(repository.findById(any(String.class))).thenReturn(Optional.empty());
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> bookingService.findById("anyString")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.DB_ENTRY_NOT_FOUND);
    }

    @Test
    void deleteBookingById_withValidId_shouldReturnSuccess() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        when(repository.findById(any())).thenReturn(Optional.of(booking));
//
//        bookingService.deleteById("validId");
//        verify(repository).deleteById(stringCaptor.capture());
//        String id = stringCaptor.getValue();
//        assertThat(id).isEqualTo("validId");
    }

    @Test
    void deleteBookingById_withInValidId_shouldThrowNotFoundException() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.class, () -> bookingService.deleteById("InValidId"));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void deleteBookingById_withoutIdField_shouldThrowMissingFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.deleteById(null)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void deleteBookingById_withEmptyId_shouldThrowEmptyFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> bookingService.deleteById("")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void updateBookingById_withValidBookingFields_shouldReturnBooking() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        booking.setId("testId123");
//        when(repository.findById(booking.getId())).thenReturn(Optional.of(booking));
//        bookingService.updateById(booking);
//        verify(repository).save(booking);
    }

    @Test
    void updateBookingById_withInValidId_shouldThrowNotFoundException() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        booking.setId("testId123");
//        when(repository.findById(any())).thenReturn(Optional.empty());
//        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.updateById(booking)).getCommonCode();
//        assertThat(code).isEqualTo(CommonCode.DB_ENTRY_NOT_FOUND);
    }

    @Test
    void updateBookingById_withMissingTitleField_shouldThrowMissingFieldException() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        booking.setId("testId123");
//        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.updateById(booking)).getCommonCode();
//        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }

    @Test
    void updateBookingById_withMissingAddressField_shouldThrowMissingFieldException() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        booking.setId("testId123");
//        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.updateById(booking)).getCommonCode();
//        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }
    @Test
    void updateBookingById_withMissingBookingNumberField_shouldThrowMissingFieldException() {
//        Booking booking = new Booking("id123456", null, null, "paid", 19999, null);
//        booking.setId("testId123");
//        CommonCode code = Assertions.assertThrows(CustomException.class, () -> bookingService.updateById(booking)).getCommonCode();
//        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }


}
