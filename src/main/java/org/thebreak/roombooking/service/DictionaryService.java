package org.thebreak.roombooking.service;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.response.CommonCode;

import java.util.List;

public interface DictionaryService {
    Dictionary addDictionary(String dName);
    Dictionary updateById(ObjectId id, String dName);
    Dictionary addValueById(ObjectId id, String dValue);
    Dictionary deleteValue(ObjectId id, String dValue);
    List<Dictionary> listDictionary(int page, int size);
    Dictionary findByDName(String dName);
    Dictionary findById(ObjectId id);
    CommonCode deleteById(ObjectId id);

}
