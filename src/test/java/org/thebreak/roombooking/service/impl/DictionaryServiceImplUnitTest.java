package org.thebreak.roombooking.service.impl;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.IfProfileValue;
import org.thebreak.roombooking.dao.DictionaryDao;
import org.thebreak.roombooking.model.Dictionary;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryServiceImplUnitTest {

    private final DictionaryDao dictionaryDao = Mockito.mock(DictionaryDao.class);

    @BeforeEach
    void setUp() {
    }

    @Test
    void listDictionary_shouldListAllDicts() {
    }

    @Test
    void findByName() {

    }

    @Test
    @DisplayName("should find the dictionary by id")
    void findById_shouldFindDictById() {
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        Dictionary dictionary = new Dictionary("6101f950f5b795162645c6d3", "name1", values, LocalDateTime.now(), LocalDateTime.now());
       Mockito.when(dictionaryDao.findById(new ObjectId("6101f950f5b795162645c6d3"))).thenReturn(dictionary);
    }
}