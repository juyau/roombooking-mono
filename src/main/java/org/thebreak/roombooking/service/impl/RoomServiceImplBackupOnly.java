package org.thebreak.roombooking.service.impl;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.dao.RoomRepository;
import org.thebreak.roombooking.model.Room;

import java.util.Optional;

@Service
public class RoomServiceImplBackupOnly {
    @Autowired
    RoomRepository repository;


    @Autowired
    MongoTemplate mongoTemplate;

    public Room update(Room room) {
        if(room == null){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if(null == room.getId()){
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }

        Optional<Room> optional = repository.findById(room.getId());
        if(!optional.isPresent()){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        };

        Query query = Query.query(Criteria.where("id").is(room.getId()));
        Update update = new Update();

        if(room.getTitle() != null){
            update.set("title", room.getTitle());
        }
        if(room.getDescription() != null){
            update.set("description", room.getDescription());
        }
        if(room.getAddress() != null){
            update.set("address", room.getAddress());
        }
        if(room.getRoomNumber() != null){
            update.set("roomNumber", room.getRoomNumber());
        }
        if(room.getCity() != null){
            update.set("city", room.getCity());
        }
        if(room.getType() != null){
            update.set("type", room.getType());
        }
        if(room.getFloor() != 0){
            update.set("floor", room.getFloor());
        }
        if(room.getSize() != 0){
            update.set("size", room.getSize());
        }

        UpdateResult result = mongoTemplate.updateFirst(query, update,  Room.class );
        result.getModifiedCount();
        // TODO to implement update ignore null fields

        Optional<Room> byId = repository.findById(room.getId());
        if(!byId.isPresent()){
            CustomException.cast(CommonCode.FAILED);
        }
        return byId.get();
    }
}
