package org.thebreak.roombooking.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.dao.RoomRepository;
import org.thebreak.roombooking.model.Room;
import org.thebreak.roombooking.model.response.CommonCode;
import org.thebreak.roombooking.service.impl.RoomServiceImpl;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)  // same as using MockitoAnnotations.initMocks(this);
class RoomServiceTest {

    @InjectMocks
    RoomServiceImpl roomService;  // dependencies with @Mock annotation will be injected to this service

    @Mock
    RoomRepository repository;

    @Captor
    ArgumentCaptor<Pageable> pageCaptor;

    @Test
    void canAddRoom_withValidRoomFields() {
        Room room = new Room("meeting room", "meeting");
        when(repository.save(room)).thenReturn(room);
        Room savedRoom = roomService.add(room);

        assertThat(savedRoom).isNotNull();
        verify(repository).save(room);
    }

    @Test
    void addRoom_withInValidRoomFields_shouldThrowException() {
        Room room = new Room(null, "meeting");
        Boolean success = Assertions.assertThrows(CustomException.class, () -> roomService.add(room)).getCommonCode().getSuccess();
        assertThat(success).isFalse();
    }

    @Test
    void addRoom_withExistedName_shouldThrowException() {
        Room room = new Room("meeting room", "meeting");
        when(repository.findByName(room.getName())).thenReturn(room);
        String message = Assertions.assertThrows(CustomException.class, () -> roomService.add(room)).getCommonCode().getMessage();
        assertThat(message).isEqualTo(CommonCode.DB_ENTRY_ALREADY_EXIST.getMessage());
    }

    @Test
    void listRooms_withoutPageAndSize_shouldUseDefault() {
        List<Room> roomList = getRoomList();

        // use PageImpl to create paged room list;
        Page<Room> pagedRooms = new PageImpl(roomList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedRooms);

        // controller default is page=1, size=5 if no params provided;
        List<Room> findRoomList = roomService.findPage(1, 5);

        assertThat(findRoomList.size()).isEqualTo(4);

        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size are 0 and 5 by default(1 and 5 default by controller)
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(5);
    }
    @Test
    void listRooms_withPageAndSize_shouldListCorrectPageAndSize() {
        List<Room> roomList = getRoomList();

        // use PageImpl to create paged room list;
        Page<Room> pagedRooms = new PageImpl(roomList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedRooms);

        // controller default is page=1, size=5 if no params provided;
        List<Room> findRoomList = roomService.findPage(1, 10);

        // check that the repository method is called with correct page and size;

        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(10);
    }

    @Test
    void listRooms_withSizeExceedingMaxSizeLimit_shouldUseDefinedMaxSize() {
        List<Room> roomList = getRoomList();

        // use PageImpl to create paged room list;
        Page<Room> pagedRooms = new PageImpl(roomList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedRooms);

        // pass in a size bigger than Max size (50)
        List<Room> findRoomList = roomService.findPage(1, 200);

        // check that the repository method is called with max size limit;
        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(Constants.MAX_PAGE_SIZE);
    }



    private List<Room> getRoomList() {
        List<Room> roomList = new ArrayList<>();
        Room room1 = new Room("room1", "type1");
        Room room2 = new Room("room2", "type2");
        Room room3 = new Room("room3", "type3");
        Room room4 = new Room("room4", "type4");
        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room3);
        roomList.add(room4);
        return roomList;
    }

}
