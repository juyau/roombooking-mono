package org.thebreak.roombooking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.dao.RoomRepository;
import org.thebreak.roombooking.model.Room;
import org.thebreak.roombooking.model.response.CommonCode;
import org.thebreak.roombooking.service.RoomService;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomRepository repository;

    public Room add(Room room) {
        if(room.getTitle() == null || room.getAddress() == null || room.getRoomNumber() == null ){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(room.getTitle().trim().isEmpty() || room.getAddress().trim().isEmpty()){
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
        if(repository.findByAddressAndRoomNumber(room.getAddress(),room.getRoomNumber()) != null){
            CustomException.cast(CommonCode.ROOM_ENTRY_ALREADY_EXIST);
        }
        return repository.save(room);
    }

    public Page<Room> findPage(Integer page, Integer size) {
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

        Page<Room> roomPage = repository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));

        if(roomPage.getContent().size() == 0 ){
            CustomException.cast(CommonCode.DB_EMPTY_LIST);
        }
        return roomPage;
    }
}
