package org.thebreak.roombooking.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.dao.RoomRepository;
import org.thebreak.roombooking.model.Room;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.service.RoomService;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomRepository repository;

    public Room add(Room room) {
//        if(room == null){
//            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
//        }
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

        Page<Room> roomPage = repository.findAll(PageRequest.of(page, size, Sort.by("updatedAt").descending()));

        if(roomPage.getContent().size() == 0 ){
            CustomException.cast(CommonCode.DB_EMPTY_LIST);
        }
        return roomPage;
    }

    @Override
    public Room findById(String id) {
        if(id == null){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(id.trim().isEmpty()){
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
        Optional<Room> optional = repository.findById(id);
        if(!optional.isPresent()){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        return optional.get();
    }

    @Override
    public void deleteById(String id) {
        if(id == null) {
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(id.trim().isEmpty()){
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
        Optional<Room> optional = repository.findById(id);
        if(optional.isPresent()){
            repository.deleteById(id);
        } else {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
    }

    @Override
    public Room update(Room room) {
        if(room == null){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(null == room.getTitle()){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(null == room.getId()
                || StringUtils.isEmpty(room.getAddress())
                || null == room.getRoomNumber()){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }

        Optional<Room> optional = repository.findById(room.getId());
        if(!optional.isPresent()){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        };

        // to implement update ignore null fields
        Room roomReturn = optional.get();
        java.lang.reflect.Field[] fields = room.getClass().getDeclaredFields();

        for(Field field: fields) {
            System.out.println(field.getName());
           }

        return repository.save(room);
    }
}
