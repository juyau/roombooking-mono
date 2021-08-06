package org.thebreak.roombooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.Room;
import org.thebreak.roombooking.model.response.CommonCode;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private String oId;

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void beforeEach(@Autowired MongoTemplate mongoTemplate) {
        // drop all data in the test db
        mongoTemplate.dropCollection(Room.class);

        //create dictionary entry for test
        Room room = new Room("Big party room with plenty of utils1", "101 kings ave", 101);

        Room savedRoom = mongoTemplate.save(room);
        // get an ObjectId for method tests, such as findById
        oId = savedRoom.getId();
    }

    @Test
    void addRoom_withValidFields_shouldReturnSavedRoom() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rooms/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Room( "Big party room with plenty of utils", "99 kings ave", 99))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.address", is("99 kings ave")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roomNumber", is(99)));
    }

    @Test
    void addRoom_withMissingFields_shouldThrowException() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rooms/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Room( null, "99 kings ave", 99))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonCode.REQUEST_FIELD_MISSING.getMessage()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(CommonCode.REQUEST_FIELD_MISSING.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonCode.REQUEST_FIELD_MISSING.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }
    @Test
    void addRoom_withEmptyFields_shouldThrowException() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rooms/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Room( "", "99 kings ave", 99))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonCode.REQUEST_FIELD_EMPTY.getMessage()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(CommonCode.REQUEST_FIELD_EMPTY.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonCode.REQUEST_FIELD_EMPTY.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }
    @Test
    void addRoom_withExistAddressAndRoomNumber_shouldThrowAlreadyExist() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rooms/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Room( "random title", "101 kings ave", 101))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(CommonCode.ROOM_ENTRY_ALREADY_EXIST.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(CommonCode.ROOM_ENTRY_ALREADY_EXIST.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }
}