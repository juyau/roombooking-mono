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
    public Dictionary addDictionary(String dName){
        if(dName.isEmpty()){
            CustomException.cast(CommonCode.INVALID_PARAM);
        }

        // Check if dictionary name already exists;
        Query query = new Query().addCriteria(Criteria.where("dName").is(dName));
        Dictionary dictionary = dictionaryDao.findDictionaryByDName(query);

        if(null != dictionary){
            CustomException.cast(CommonCode.DB_ENTRY_ALREADY_EXIST);
        }

        // Add new dictionary to db;
        Dictionary dictionary1 = new Dictionary();
        dictionary1.setdName(dName);
        List<String> dvalue = new ArrayList<>();
        dictionary1.setdValue(dvalue);

        return dictionaryDao.addDictionary(dictionary1);
    }

    @Override
    public Dictionary updateById(ObjectId id, String dName) {

        // check if id exist;
        Dictionary dictionary = this.findById(id);

        // check if new dName already exist;
        Query query = new Query().addCriteria(Criteria.where("dName").is(dName));
        Dictionary dictionary1 = dictionaryDao.findDictionaryByDName(query);

        if(dictionary1 != null){
            CustomException.cast(CommonCode.DB_ENTRY_ALREADY_EXIST);
        }

        dictionary.setdName(dName);

        return dictionaryDao.save(dictionary);
    }

    @Override
    public Dictionary addValueById(ObjectId id, String dValue) {
        // check params;
//        if(null == id || null == dValue){
//            CustomException.cast(CommonCode.INVALID_PARAM);
//        }

        // check if dictionary exist, if not, findById method will throw exception;
        Dictionary dictionary = this.findById(id);

        System.out.println(dictionary);

//        Dictionary dictionary = dictionaryDao.findById(id);
//        if(dictionary == null){
//            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
//        }


        // check if value already exist in the dValue list;
        for(String value: dictionary.getdValue()){
            if(value.equals(dValue)){
                CustomException.cast(CommonCode.DB_ENTRY_ALREADY_EXIST);
            }
        }
        dictionary.getdValue().add(dValue);

        return dictionaryDao.save(dictionary);
    }

    @Override
    public Dictionary deleteValue(ObjectId id, String dValue) {
        // check if dictionary exist, if not, findById method will throw exception;
        Dictionary dictionary = this.findById(id);

        // check if value already exist in the dValue list;
        if(!dictionary.getdValue().contains(dValue)){
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }

        dictionary.getdValue().removeIf(value -> value.equals(dValue));

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

        System.out.println("Page = " + page +" Size = "+size);

        // list by page and size; default sort by ascending;
        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query();
        query.with(pageable).with(Sort.by("dName").ascending());
        List<Dictionary> list = dictionaryDao.listDictionaries(query);

        System.out.println(list.toString());
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
    public Dictionary findByDName(String dName){

        Query query = new Query();
        query.addCriteria(Criteria.where("dName").is(dName));

        Dictionary dict = dictionaryDao.findDictionaryByDName(query);

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
