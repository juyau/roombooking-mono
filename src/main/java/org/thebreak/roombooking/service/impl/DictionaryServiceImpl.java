package org.thebreak.roombooking.service.impl;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.response.CommonCode;
import org.thebreak.roombooking.dao.DictionaryDao;
import org.thebreak.roombooking.service.DictionaryService;

import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryDao  dictionaryDao;


    @Override
    public Dictionary addDictionary(String name){
        if(name.isEmpty()){
            CustomException.cast(CommonCode.INVALID_PARAM);
        }

        // Check if dictionary name already exists;
        Query query = new Query().addCriteria(Criteria.where("name").is(name));

        Dictionary dictionary = dictionaryDao.findByName(query);

        if(null != dictionary){
            CustomException.cast(CommonCode.DB_ENTRY_ALREADY_EXIST);
        }

        // Add new dictionary to db;
        Dictionary dictionary1 = new Dictionary();
        dictionary1.setName(name);
        List<String> values = new ArrayList<>();
        dictionary1.setValues(values);

        return dictionaryDao.save(dictionary1);
    }

    @Override
    public Dictionary updateById(ObjectId id, String name) {

        // check if id exist;
        Dictionary dictionary = this.findById(id);

        // check if new dname already exist;
        Query query = new Query().addCriteria(Criteria.where("name").is(name));
        Dictionary dictionary1 = dictionaryDao.findByName(query);

        if(dictionary1 != null){
            CustomException.cast(CommonCode.DB_ENTRY_ALREADY_EXIST);
        }

        dictionary.setName(name);

        return dictionaryDao.save(dictionary);
    }

    @Override
    public Dictionary addValueById(ObjectId id, String value) {

        // check if dictionary exist, if not, findById method will throw exception;
        Dictionary dictionary = this.findById(id);

        // check if value already exist in the values list;
        for(String value1: dictionary.getValues()){
            if(value.equals(value1)){
                CustomException.cast(CommonCode.DB_ENTRY_ALREADY_EXIST);
            }
        }
        dictionary.getValues().add(value);

        return dictionaryDao.save(dictionary);
    }

    @Override
    public Dictionary deleteValue(ObjectId id, String value) {
        // check if dictionary exist, if not, findById method will throw exception;
        Dictionary dictionary = this.findById(id);

        // check if value already exist in the dValue list;
        if(!dictionary.getValues().contains(value)){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }

        dictionary.getValues().removeIf(value1 -> value1.equals(value));

        return dictionaryDao.save(dictionary);
    }

    @Override
    public List<Dictionary> listDictionary(int page, int size ){

        // mongo page start with 0;
        if(page < 1){
            page = 1;
        }
        if(size > 100){
            size = 100;
        }
        page = page -1;


        // list by page and size; default sort by ascending;
        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query();
        query.with(pageable).with(Sort.by("dname").ascending());
        List<Dictionary> list = dictionaryDao.listDictionaries(query);

        // check if target not exist or list is empty;
        if(list == null){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        if(list.size() == 0 ){
            CustomException.cast(CommonCode.DB_EMPTY_LIST);
        }
        return list;
    }


    @Override
    public Dictionary findByName(String name){

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));

        Dictionary dict = dictionaryDao.findByName(query);

        if(dict == null){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        return dict;
    }

    @Override
    public Dictionary findById(ObjectId id) {
        Dictionary dictionary = dictionaryDao.findById(id);
        if(dictionary == null){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        return dictionary;
    }

    @Override
    public CommonCode deleteById(ObjectId id) {
        Dictionary dictionary = this.findById(id);
        if(dictionary == null){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        DeleteResult deleteResult = dictionaryDao.delete(dictionary);
        if(deleteResult.getDeletedCount() <= 0){
            return CommonCode.FAILED;
        }
        return CommonCode.SUCCESS;
    }

}
