package org.thebreak.roombooking.service.impl;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.thebreak.roombooking.dao.DictionaryRepository;
import org.thebreak.roombooking.model.Dictionary;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class DictionaryServiceImplUnitTest {

    private final DictionaryRepository dictionaryDao = Mockito.mock(DictionaryRepository.class);

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
       Mockito.when(dictionaryDao.findById("6101f950f5b795162645c6d3")).thenReturn(Optional.of(dictionary));
    }
}