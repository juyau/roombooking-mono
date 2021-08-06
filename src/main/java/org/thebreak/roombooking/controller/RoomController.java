package org.thebreak.roombooking.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.Room;
import org.thebreak.roombooking.model.response.PageResult;
import org.thebreak.roombooking.model.response.ResponseResult;
import org.thebreak.roombooking.model.vo.DictionaryVO;
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

//    @GetMapping(value = "")
//    public ResponseResult<List<RoomVO>> findRoomsPage(
//            @RequestParam @Nullable Integer page,
//            @RequestParam @Nullable Integer size){
//        System.out.println(page);
//        System.out.println(size);
//
//        List<Room> roomList = roomService.findPage(page, size);
//
//        List<RoomVO> voList = new ArrayList<>();
//        for (Room room : roomList) {
//            RoomVO roomVO = new RoomVO();
//            BeanUtils.copyProperties(room, roomVO);
//            voList.add(roomVO);
//        }
//
//        return ResponseResult.success(voList);
//    }

    @GetMapping(value = "")
    public ResponseResult<PageResult<List<RoomVO>>> findRoomsPage(
            @RequestParam @Nullable Integer page, @RequestParam @Nullable Integer size){

        Page<Room> roomPage = roomService.findPage(page, size);

        // map the list content to VO list
        List<Room> roomList = roomPage.getContent();
        List<RoomVO> voList = new ArrayList<>();
        for (Room room : roomList) {
            RoomVO roomVO = new RoomVO();
            BeanUtils.copyProperties(room, roomVO);
            voList.add(roomVO);
        }

        // assemble page result with page info
        PageResult<List<RoomVO>> pageResult = new PageResult<>();
        pageResult.setTotalRows(roomPage.getTotalElements());
        pageResult.setPageSize(roomPage.getSize());                // request page size
        pageResult.setTotalPages(roomPage.getTotalPages());
        pageResult.setContentSize(roomPage.getNumberOfElements());  // actual content size returned
        pageResult.setCurrentPage(roomPage.getNumber() + 1);       // mongo start with 0, so need to add 1


        pageResult.setContent(voList);

        return ResponseResult.success(pageResult);
    }

    @PostMapping(value = "/add")
    public ResponseResult<RoomVO> addRoom(@RequestBody Room room){

        Room r = roomService.add(room);
        RoomVO roomVO = new RoomVO();
        BeanUtils.copyProperties(r, roomVO);

        log.debug("Room added.");

        return ResponseResult.success(roomVO);
    }
}
