package org.thebreak.roombooking.dao.impl;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.dao.RoomDao;
import org.thebreak.roombooking.model.Room;

import java.util.List;

@Repository
public class RoomDaoImpl implements RoomDao {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Room> listRooms(Query query) {
        return  mongoTemplate.find(query, Room.class);
    }

    @Override
    public Room findRoomByName(Query query) {
        return mongoTemplate.findOne(query, Room.class);
    }

    @Override
    public Room findById(ObjectId id) {
        return mongoTemplate.findById(id, Room.class);
    }

    @Override
    public Room save(Room room) {
        return  mongoTemplate.save(room);
    }

    @Override
    public DeleteResult delete(Room room) {
        return  mongoTemplate.remove(room);
    }

}
