package org.thebreak.roombooking.service.impl;


import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.service.DictionaryService;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@SpringBootTest
class DictionaryServiceImplTest {

    @Resource
    private DictionaryService dictionaryService;

    @BeforeAll
    static void beforeAll(@Autowired MongoTemplate mongoTemplate) {
        // drop all data in the test db
        System.out.println("beforeAll");
        mongoTemplate.dropCollection(Dictionary.class);

        //create one dictionary entry for later use
        Dictionary dictionary = new Dictionary();
        dictionary.setdName("name1");
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        dictionary.setdValue(list);
        mongoTemplate.save(dictionary);
    }

    @Test
    void listDictionary() {
        // when
        List<Dictionary> list = dictionaryService.listDictionary(0, 1);
        // then
        assertThat(list.size()).isGreaterThanOrEqualTo(1);
        assertThat(list.get(0).getdName()).isEqualTo("name1");
    }

    @Test
    void addDictionary() {
        // given
        Dictionary dict = dictionaryService.addDictionary("addedName");
        // when
        String addedName = dict.getdName();

        // then
        assertThat("addedName").isEqualTo(addedName);
        assertThat(dict.getId()).isNotNull();
        assertThat(dict.getCreatedAt()).isBefore(LocalDateTime.now());

    }
    @Test
    void findByDName() {
        Dictionary dict = dictionaryService.findByDName("name1");
        assertThat("name1").isEqualTo(dict.getdName());
    }




    @Test
    void addValueById() {
    }

    @Test
    void deleteValue() {
    }




    @Test
    void findById() {
    }

    @Test
    void updateById() {
        // given
        Dictionary dict = dictionaryService.addDictionary("name1");
        ObjectId id = new ObjectId(dict.getId());
        // when
        Dictionary dict2 = dictionaryService.updateById(id, "newName");
        // then
        assertThat(dict2.getdName()).isEqualTo("newName");
    }

    @Test
    void deleteById() {
    }

}