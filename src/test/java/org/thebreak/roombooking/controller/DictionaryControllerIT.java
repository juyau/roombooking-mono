package org.thebreak.roombooking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.response.CommonCode;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class DictionaryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static String oId;

    @BeforeEach
     void beforeEach(@Autowired MongoTemplate mongoTemplate) {
        // drop all data in the test db
        mongoTemplate.dropCollection(Dictionary.class);

        //create dictionary entry for test
        Dictionary dictionary = new Dictionary();
        dictionary.setName("name1");
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        dictionary.setValues(list);
        Dictionary savedDictionary = mongoTemplate.save(dictionary);
        // get an ObjectId for method tests, such as findById
        oId = savedDictionary.getId();

    }

    @Test
    void getDictionaryPage_withoutPageAndSize_shouldUseDefault() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dicts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(Constants.DEFAULT_PAGE_SIZE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].name", is("name1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].values", containsInAnyOrder("value1", "value2")));
    }

    @Test
    void getDictionaryPage_withValidPageAndSize_shouldReturnPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dicts")
                .param("page","1")
                .param("size","5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].values").isArray());
    }

    @Test
    void findDictionaryById_withValidId_shouldReturnDictionary() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dicts/byId").param("id", oId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("name1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.values", containsInAnyOrder("value1", "value2")));
    }

    @Test
    void findDictionaryById_withInvalidId_shouldThrowNotFoundException() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dicts/byId").param("id", "invalidRandomStringIdHere"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonCode.DB_ENTRY_NOT_FOUND.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonCode.DB_ENTRY_NOT_FOUND.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void findDictionaryByName_withValidName_shouldReturnDictionary() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dicts/byName").param("name", "name1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("name1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.values", containsInAnyOrder("value1", "value2")));
    }

    @Test
    void findDictionaryByName_withInvalidName_shouldThrowNotFoundException() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dicts/byName").param("name", "notExistName"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonCode.DB_ENTRY_NOT_FOUND.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonCode.DB_ENTRY_NOT_FOUND.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void addDictionary_withValidFields_shouldReturnAddedDictionary() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dicts/add").param("name","name2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("name2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.values").isEmpty());
    }

    @Test
    void addDictionary_withExistName_shouldThrowAlreadyExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dicts/add").queryParam("name","name1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(CommonCode.DB_ENTRY_ALREADY_EXIST.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(CommonCode.DB_ENTRY_ALREADY_EXIST.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteDictionaryById_withValidId_shouldReturnSuccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/dicts/delete/" + oId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(CommonCode.SUCCESS.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(CommonCode.SUCCESS.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteDictionaryById_withInValidId_shouldThrowNotFoundException() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/dicts/delete/" + "thisIsNotExistOrAlreadyDeletedId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(CommonCode.DB_ENTRY_NOT_FOUND.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(CommonCode.DB_ENTRY_NOT_FOUND.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }
}