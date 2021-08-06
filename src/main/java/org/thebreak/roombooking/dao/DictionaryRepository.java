package org.thebreak.roombooking.dao;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.model.Dictionary;

import java.util.List;

@Repository
public interface DictionaryRepository extends MongoRepository<Dictionary, String> {
    Dictionary findByName(String name);
    Page<Dictionary> findAll(Pageable pageable);
}
