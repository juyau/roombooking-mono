package org.thebreak.roombooking.dao.impl;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.dao.DictionaryDao;
import org.thebreak.roombooking.model.Dictionary;

import java.util.List;

@Repository
public class DictionaryDaoImpl implements DictionaryDao {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Dictionary> listDictionaries(Query query) {
        return  mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public Dictionary findByName(Query query) {
        return mongoTemplate.findOne(query, Dictionary.class);
    }

    @Override
    public Dictionary findById(ObjectId id) {
        return mongoTemplate.findById(id, Dictionary.class);
    }
    @Override
    public Dictionary save(Dictionary dictionary) {
        return  mongoTemplate.save(dictionary);
    }

    @Override
    public DeleteResult delete(Dictionary dictionary) {
        return  mongoTemplate.remove(dictionary);
    }

}
