package org.thebreak.roombooking.service;

import org.thebreak.roombooking.model.Room;

import java.util.List;

public interface   RoomService {
    Room add(Room room);
    List<Room> findPage(int page, int size);
}
