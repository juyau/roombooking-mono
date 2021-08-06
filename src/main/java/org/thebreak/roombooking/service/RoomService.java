package org.thebreak.roombooking.service;

import org.springframework.data.domain.Page;
import org.thebreak.roombooking.model.Room;

import java.util.List;

public interface   RoomService {
    Room add(Room room);
    Page<Room> findPage(Integer page, Integer size);
}
