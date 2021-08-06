package org.thebreak.roombooking.dao.impl;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.dao.DictionaryRepository;
import org.thebreak.roombooking.model.Dictionary;

@Repository
public class DictionaryDaoImpl_backupOnly {
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//
//    @Override
//    public Page<Dictionary> findPage(Query query) {
//        return  mongoTemplate.find(query, Dictionary.class);
//    }
//
//    @Override
//    public Dictionary findByName(Query query) {
//        return mongoTemplate.findOne(query, Dictionary.class);
//    }
//
//    @Override
//    public Dictionary findById(ObjectId id) {
//        return mongoTemplate.findById(id, Dictionary.class);
//    }
//    @Override
//    public Dictionary save(Dictionary dictionary) {
//        return  mongoTemplate.save(dictionary);
//    }
//
//    @Override
//    public DeleteResult delete(Dictionary dictionary) {
//        return  mongoTemplate.remove(dictionary);
//    }

}
