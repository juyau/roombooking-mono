package org.thebreak.roombooking.dao;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.Room;

import java.util.List;


public interface RoomDao {
    List<Room> listRooms(Query query);
    Room findRoomByName(Query query);
    Room findById(ObjectId id);
    Room save(Room room);
    DeleteResult delete(Room room);

}
