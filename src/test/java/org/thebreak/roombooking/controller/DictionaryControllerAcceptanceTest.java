package org.thebreak.roombooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import org.bson.types.ObjectId;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.response.CommonCode;
import org.thebreak.roombooking.model.response.ResponseResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class DictionaryControllerAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectId oId;

    @BeforeAll
    static void beforeAll(@Autowired MongoTemplate mongoTemplate) {
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
        oId = new ObjectId(savedDictionary.getId());

    }

    @Test
    void listDictionary_shouldReturnList() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/dict/list"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", is("name1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].values", containsInAnyOrder("value1", "value2")));

    }

    @Test
    void findDictionaryById_shouldReturnDictionary() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/dict/byId").param("id", oId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("name1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.values", containsInAnyOrder("value1", "value2")));
    }

    @Test
    void addDictionary_shouldReturnAddedDictionary() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.post("/dict/add").queryParam("name","name2"))
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

        mockMvc.perform(MockMvcRequestBuilders.post("/dict/add").queryParam("name","name1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(CommonCode.DB_ENTRY_ALREADY_EXIST.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", is(CommonCode.DB_ENTRY_ALREADY_EXIST.getSuccess())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(CommonCode.DB_ENTRY_ALREADY_EXIST.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());

    }
}