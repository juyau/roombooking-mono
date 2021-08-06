package org.thebreak.roombooking.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.response.CommonCode;

import java.util.List;

public interface DictionaryService {
    Dictionary addDictionary(String name);
    Dictionary updateById(String id, String name);
    Dictionary addValueById(String id, String value);
    Dictionary deleteValue(String id, String value);
    Page<Dictionary> findPage(Integer page, Integer size);
    Dictionary findByName(String name);
    Dictionary findById(String id);
    void deleteById(String id);

}
