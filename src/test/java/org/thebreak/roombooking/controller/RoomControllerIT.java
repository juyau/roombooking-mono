package org.thebreak.roombooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.model.Room;
import org.thebreak.roombooking.model.response.CommonCode;

import static org.hamcrest.Matchers.is;

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
        Room room = new Room("title1", "101 kings ave", 101);

        Room savedRoom = mongoTemplate.save(room);
        // get an ObjectId for method tests, such as findById
        oId = savedRoom.getId();
        System.out.println(oId);
    }

    @Test
    void addRoom_withValidFields_shouldReturnSavedRoom() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rooms/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Room( "title9", "99 kings ave", 99))))
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(CommonCode.REQUEST_FIELD_EMPTY.getSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonCode.REQUEST_FIELD_EMPTY.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonCode.REQUEST_FIELD_EMPTY.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }
    @Test
    void addRoom_withExistAddressAndRoomNumber_shouldThrowAlreadyExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rooms/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Room( "random title", "101 kings ave", 101))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(CommonCode.ROOM_ENTRY_ALREADY_EXIST.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(CommonCode.ROOM_ENTRY_ALREADY_EXIST.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void getRoomsPage_withoutPageAndSize_shouldUseDefault() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(Constants.DEFAULT_PAGE_SIZE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].title").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].roomNumber").isNumber());
    }

    @Test
    void getRoomsPage_withValidPageAndSize_shouldReturnPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms")
                .param("page","1")
                .param("size","5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].title").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].roomNumber").isNumber());
    }

    @Test
    void getRoomById_withValidId_shouldReturnRoom() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/byId/" + oId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title", is("title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roomNumber", is(101)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.address").isString());
    }

    @Test
    void getRoomById_withInValidId_shouldThrowNotFoundException() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/byId/" + "thisIsARandomInValidId123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(CommonCode.DB_ENTRY_NOT_FOUND.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(CommonCode.DB_ENTRY_NOT_FOUND.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteRoomById_withValidId_shouldReturnSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/rooms/delete/" + oId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(CommonCode.SUCCESS.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(CommonCode.SUCCESS.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteRoomById_withInValidId_shouldThrowNotFoundException() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/rooms/delete/" + "thisIsNotExistOrAlreadyDeletedId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(CommonCode.DB_ENTRY_NOT_FOUND.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(CommonCode.DB_ENTRY_NOT_FOUND.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void updateRoom_withValidFields_shouldReturnSavedRoom() throws Exception{
        Room roomToUpdate = new Room( "title9", "99 kings ave", 99);
        roomToUpdate.setId(oId);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/rooms/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(roomToUpdate)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(oId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title", is("title9")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.address", is("99 kings ave")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roomNumber", is(99)));
    }
}