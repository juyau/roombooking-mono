package com.brothermiles.studyon.cms.dao;

import com.brothermiles.studyon.model.cms.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysDictionaryRepository extends MongoRepository<SysDictionary, String> {

    SysDictionary findByDName(String dName);
}
