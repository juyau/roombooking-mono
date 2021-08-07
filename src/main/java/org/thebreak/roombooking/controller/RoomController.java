package org.thebreak.roombooking.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.model.Room;
import org.thebreak.roombooking.model.response.CommonCode;
import org.thebreak.roombooking.model.response.PageResult;
import org.thebreak.roombooking.model.response.ResponseResult;
import org.thebreak.roombooking.model.vo.RoomVO;
import org.thebreak.roombooking.service.RoomService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping()
    public ResponseResult<PageResult<RoomVO>> findRoomsPage(@RequestParam @Nullable Integer page,
                                                            @RequestParam @Nullable Integer size){
        Page<Room> roomPage = roomService.findPage(page, size);

        // map the list content to VO list
        List<Room> roomList = roomPage.getContent();
        List<RoomVO> voList = new ArrayList<>();
        for (Room room : roomList) {
            RoomVO roomVO = new RoomVO();
            BeanUtils.copyProperties(room, roomVO);
            voList.add(roomVO);
        }
        // assemble pageResult
        PageResult<RoomVO> pageResult = new PageResult<>(roomPage, voList);

        return ResponseResult.success(pageResult);
    }

    @GetMapping(value = "/byId/{id}")
    public ResponseResult<RoomVO> getById(@PathVariable String id){
        Room r = roomService.findById(id);
        RoomVO roomVO = new RoomVO();
        BeanUtils.copyProperties(r, roomVO);
        return ResponseResult.success(roomVO);
    }

    @PostMapping(value = "/add")
    public ResponseResult<RoomVO> addRoom(@RequestBody Room room){
        Room r = roomService.add(room);
        RoomVO roomVO = new RoomVO();
        BeanUtils.copyProperties(r, roomVO);
        return ResponseResult.success(roomVO);
    }

    @PutMapping(value = "/update")
    public ResponseResult<RoomVO> getById(@RequestBody Room room){
        Room r = roomService.update(room);
        RoomVO roomVO = new RoomVO();
        BeanUtils.copyProperties(r, roomVO);
        return ResponseResult.success(roomVO);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseResult<CommonCode> deleteRoomById(@PathVariable @Nullable String id){
        roomService.deleteById(id);
        log.debug("Room added.");
        return ResponseResult.success();
    }
}
