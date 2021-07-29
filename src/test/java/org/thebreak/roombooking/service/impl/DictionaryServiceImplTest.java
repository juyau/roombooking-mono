package org.thebreak.roombooking.service.impl;


import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.response.CommonCode;
import org.thebreak.roombooking.service.DictionaryService;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DictionaryServiceImplTest {

    @Resource
    private DictionaryService dictionaryService;

    private static ObjectId oId;


    @BeforeAll
    static void beforeAll(@Autowired MongoTemplate mongoTemplate) {
        // drop all data in the test db
        mongoTemplate.dropCollection(Dictionary.class);

        //create one dictionary entry for later use
        Dictionary dictionary = new Dictionary();
        dictionary.setName("name1");
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        dictionary.setValues(list);
        Dictionary savedDictionary = mongoTemplate.save(dictionary);
        // get an ObjectId for method tests, such as findById
        oId = new ObjectId(savedDictionary.getId());

        //create another dictionary entry for later use
        Dictionary dictionary2 = new Dictionary();
        dictionary2.setName("name2");
        List<String> list2 = new ArrayList<>();
        list2.add("value1");
        list2.add("value2");
        dictionary2.setValues(list2);
        Dictionary savedDictionary2 = mongoTemplate.save(dictionary2);
    }

    @Test
    @Order(1)
    void listDictionary() {

        // when
        List<Dictionary> list = dictionaryService.listDictionary(0, 3);

        // then
        assertThat(list.size()).isEqualTo(2);
        for(Dictionary dict: list){
            assertThat(dict.getName().contains("name")).isTrue();
        }

    }


    @Test
    @Order(2)
    void findByDName() {
        // when
        Dictionary dict = dictionaryService.findByName("name1");
        // then
        assertThat(dict.getId()).isNotNull();
        assertThat(dict.getName()).isEqualTo("name1");
    }

    @Test
    @Order(3)
    void findById() {
        // when
        Dictionary dict = dictionaryService.findById(oId);

        // then
        assertThat(dict.getName()).isEqualTo("name1");
        assertThat(dict.getValues().size()).isEqualTo(2);
    }



    @Test
    @Order(4)
    void addValueById() {
        // when
        Dictionary dictionary = dictionaryService.addValueById(oId, "addedValue");

        // then
        assertThat(dictionary.getValues().contains("addedValue")).isTrue();
        assertThat(dictionary.getValues().size()).isEqualTo(3);
    }

    @Test
    @Order(5)
    void deleteValue() {
        // when
        Dictionary dictionary = dictionaryService.deleteValue(oId, "value1");

        // then
        assertThat(dictionary.getValues().contains("value1")).isFalse();
        assertThat(dictionary.getValues().contains("value2")).isTrue();
    }

    @Test
    @Order(6)
    void addDictionary() {
        // given
        Dictionary dict = dictionaryService.addDictionary("addedName");
        // when
        String addedName = dict.getName();

        // then
        assertThat("addedName").isEqualTo(addedName);
        assertThat(dict.getId()).isNotNull();
        assertThat(dict.getCreatedAt()).isBefore(LocalDateTime.now());

    }

    @Test
    void updateById() {
        // when
        Dictionary dict = dictionaryService.updateById(oId,"newName");

        // then
        assertThat(dict.getName()).isEqualTo("newName");
        assertThat(dict.getId()).isEqualTo(oId.toString());
    }

    @Test
    void deleteById() {
        // when
        CommonCode code = dictionaryService.deleteById(oId);
        // then
        assertThat(code.getSuccess()).isTrue();
    }

}