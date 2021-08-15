package org.thebreak.roombooking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import org.thebreak.roombooking.dao.RoomRepository;
import org.thebreak.roombooking.model.Room;
import org.thebreak.roombooking.service.impl.RoomServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)  // same as using MockitoAnnotations.initMocks(this);
class RoomServiceUT {

    @InjectMocks
    RoomServiceImpl roomService;  // dependencies with @Mock annotation will be injected to this service

    @Mock
    RoomRepository repository;

    @Captor
    ArgumentCaptor<Pageable> pageCaptor;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    private List<Room> getMockedRoomList() {
        List<Room> roomList = new ArrayList<>();
        Room room1 = new Room("test title1", "13 kings ave", 101);
        Room room2 = new Room("test title2", "14 kings ave", 102);
        roomList.add(room1);
        roomList.add(room2);
        return roomList;
    }

    @Test
    void addRoom_withValidRoomFields_shouldReturnRoom() {
        Room room = new Room("test valid title", "99 valid address", 103);
        when(repository.save(room)).thenReturn(room);
        Room savedRoom = roomService.add(room);
        assertThat(savedRoom).isNotNull();
        verify(repository).save(room);
    }

    @Test
    void addRoom_withMissingTitleField_shouldThrowMissingFieldException() {
        Room room = new Room(null, "address1",201);
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.add(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }

    @Test
    void addRoom_withMissingAddressField_shouldThrowMissingFieldException() {
        Room room = new Room("test title2", null, 202);
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.add(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }
    @Test
    void addRoom_withMissingRoomNumberField_shouldThrowMissingFieldException() {
        Room room = new Room("test title3", "address3",null);
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.add(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }

    @Test
    void addRoom_withEmptyTitleField_shouldThrowEmptyFieldException() {
        Room room = new Room("", "address4",204);
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.add(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
    }
    @Test
    void addRoom_withEmptyAddressField_shouldThrowEmptyFieldException() {
        Room room = new Room("test title5", "",205);
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.add(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
    }

    @Test
    void addRoom_withExistedAddressAndRoomNumer_shouldThrowExistException() {
        Room room = new Room("Big party room with plenty of utils", "99 kings ave", 206);
        when(repository.findByAddressAndRoomNumber(room.getAddress(), room.getRoomNumber())).thenReturn(room);
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.add(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.ROOM_ENTRY_ALREADY_EXIST);
    }

    @Test
    void findRoomsPage_withoutPageAndSize_shouldUseDefaultPageAndSize() {
        List<Room> roomList = getMockedRoomList();

        // use PageImpl to create paged room list;
        Page<Room> pagedRooms = new PageImpl(roomList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedRooms);

        // controller default is page=1, size=DEFAULT_PAGE_SIZE if no params provided;
        List<Room> findRoomList = roomService.findPage(1, Constants.DEFAULT_PAGE_SIZE).getContent();

        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size are 0 and 5 by default(1 and 5 default by controller)
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(Constants.DEFAULT_PAGE_SIZE);

        // check the return list size is correct
        assertThat(findRoomList.size()).isEqualTo(2);
    }
    @Test
    void indRoomsPage_withPageAndSize_shouldListCorrectPageAndSize() {
        List<Room> roomList = getMockedRoomList();

        // use PageImpl to create paged room list;
        Page<Room> pagedRooms = new PageImpl(roomList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedRooms);

        List<Room> findRoomList = roomService.findPage(1, 5).getContent();

        // check that the repository method is called with correct page and size;
        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(5);

        // check the return list size is correct
        assertThat(findRoomList.size()).isEqualTo(2);
    }

    @Test
    void findRoomsPage_withSizeExceedingMaxSizeLimit_shouldUseDefinedMaxSize() {
        List<Room> roomList = getMockedRoomList();

        // use PageImpl to create paged room list;
        Page<Room> pagedRooms = new PageImpl(roomList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedRooms);

        // pass in a size bigger than Max size (50)
        List<Room> findRoomList = roomService.findPage(1, 200).getContent();

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
    void findRoomById_withValidId_shouldReturnRoom() {
        Room room = new Room("title","address", 301);
        when(repository.findById(any())).thenReturn(Optional.of(room));

        roomService.findById("validId");

        verify(repository).findById(stringCaptor.capture());
        String id = stringCaptor.getValue();
        assertThat(id).isEqualTo("validId");
    }

    @Test
    void findRoomById_withMissingId_shouldThrowMissingFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> roomService.findById(null)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }

    @Test
    void findRoomById_withEmptyId_shouldThrowEmptyFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> roomService.findById("")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
    }

    @Test
    void findRoomById_withInValidId_shouldThrowNotFoundException() {
        when(repository.findById(any(String.class))).thenReturn(Optional.empty());
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> roomService.findById("anyString")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.DB_ENTRY_NOT_FOUND);
    }

    @Test
    void deleteRoomById_withValidId_shouldReturnSuccess() {
        Room room = new Room("title","address", 301);
        when(repository.findById(any())).thenReturn(Optional.of(room));

        roomService.deleteById("validId");
        verify(repository).deleteById(stringCaptor.capture());
        String id = stringCaptor.getValue();
        assertThat(id).isEqualTo("validId");
    }

    @Test
    void deleteRoomById_withInValidId_shouldThrowNotFoundException() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.class, () -> roomService.deleteById("InValidId"));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void deleteRoomById_withoutIdField_shouldThrowMissingFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.deleteById(null)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void deleteRoomById_withEmptyId_shouldThrowEmptyFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> roomService.deleteById("")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void updateRoomById_withValidRoomFields_shouldReturnRoom() {
        Room room = new Room("test valid title", "99 valid address", 103);
        room.setId("testId123");
        when(repository.findById(room.getId())).thenReturn(Optional.of(room));
        roomService.update(room);
        verify(repository).save(room);
    }

    @Test
    void updateRoomById_withInValidId_shouldThrowNotFoundException() {
        Room room = new Room("title1", "address1",201);
        room.setId("testId123");
        when(repository.findById(any())).thenReturn(Optional.empty());
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.update(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.DB_ENTRY_NOT_FOUND);
    }

    @Test
    void updateRoomById_withMissingTitleField_shouldThrowMissingFieldException() {
        Room room = new Room(null, "address1",201);
        room.setId("testId123");
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.update(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }

    @Test
    void updateRoomById_withMissingAddressField_shouldThrowMissingFieldException() {
        Room room = new Room("test title2", null, 202);
        room.setId("testId123");
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.update(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }
    @Test
    void updateRoomById_withMissingRoomNumberField_shouldThrowMissingFieldException() {
        Room room = new Room("test title2", "address2", null);
        room.setId("testId123");
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> roomService.update(room)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }


}
