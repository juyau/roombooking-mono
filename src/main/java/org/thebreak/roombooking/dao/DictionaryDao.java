package org.thebreak.roombooking.dao;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.thebreak.roombooking.model.Dictionary;

import java.util.List;


public interface DictionaryDao {
    List<Dictionary> listDictionaries(Query query);
    Dictionary addDictionary(Dictionary dictionary);
    Dictionary findDictionaryByDName(Query query);
    Dictionary findById(ObjectId id);
    Dictionary save(Dictionary dictionary);
    DeleteResult delete(Dictionary dictionary);


}
