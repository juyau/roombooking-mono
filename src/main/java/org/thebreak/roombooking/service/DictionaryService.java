package org.thebreak.roombooking.service;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.response.CommonCode;

import java.util.List;

public interface DictionaryService {
    Dictionary addDictionary(String name);
    Dictionary updateById(ObjectId id, String name);
    Dictionary addValueById(ObjectId id, String value);
    Dictionary deleteValue(ObjectId id, String value);
    List<Dictionary> listDictionary(int page, int size);
    Dictionary findByName(String name);
    Dictionary findById(ObjectId id);
    CommonCode deleteById(ObjectId id);

}
