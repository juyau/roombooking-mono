package org.thebreak.roombooking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.model.Room;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    Room findByAddressAndRoomNumber(String title, Integer roomNumber);
}
