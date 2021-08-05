package org.thebreak.roombooking.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.model.response.ResponseResult;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.vo.DictionaryVO;
import org.thebreak.roombooking.service.DictionaryService;
import org.thebreak.roombooking.service.impl.DictionaryServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "api/v1/dict")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;


    @PostMapping(value = "/add")
    public ResponseResult<Dictionary> addDictionary(@RequestParam String name){
        log.error("From logger : addDictionary controller called.");
        log.warn("From logger : addDictionary controller called.");
        log.info("From logger : addDictionary controller called.");
        log.debug("From logger : addDictionary controller called.");
        log.trace("From logger : addDictionary controller called.");
        Dictionary d = dictionaryService.addDictionary(name);
        return ResponseResult.success(d);
    }

    @GetMapping(value = "/list")
    public ResponseResult<List<DictionaryVO>> listDictionary(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5")int size){

        log.error("From logger : addDictionary controller called.");
        log.warn("From logger : addDictionary controller called.");
        log.info("From logger : addDictionary controller called.");
        log.debug("From logger : addDictionary controller called.");

        List<Dictionary> list = dictionaryService.listDictionary(page, size);
        List<DictionaryVO> voList = new ArrayList<>();
        for (Dictionary dictionary : list) {
            DictionaryVO dictionaryVO = new DictionaryVO();
            BeanUtils.copyProperties(dictionary, dictionaryVO);
            voList.add(dictionaryVO);
        }
        return ResponseResult.success(voList);
    }

    @GetMapping(value = "/byName")
    public ResponseResult<DictionaryVO> findByName(@RequestParam String name){
        Dictionary dictionary = dictionaryService.findByName(name);
        DictionaryVO dictionaryVO = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, dictionaryVO);
        return ResponseResult.success(dictionaryVO);
    }

    @GetMapping(value = "/byId")
    public ResponseResult<Dictionary> findById(@RequestParam ObjectId id){
        return ResponseResult.success(dictionaryService.findById(id));
    }

    @PostMapping(value = "/update")
    public ResponseResult<Dictionary> update(@RequestParam ObjectId id, String name){
        return ResponseResult.success(dictionaryService.updateById(id, name));
    }
    @PostMapping(value = "/delete")
    public ResponseResult<Dictionary> deleteById(@RequestParam ObjectId id){
        dictionaryService.deleteById(id);
        return ResponseResult.success();
    }

    @PostMapping(value = "/addValue")
    public ResponseResult<DictionaryVO> addValue(@RequestParam ObjectId id, String value){
        DictionaryVO dictionaryVO = new DictionaryVO();
        BeanUtils.copyProperties(dictionaryService.addValueById(id, value), dictionaryVO);
        return ResponseResult.success(dictionaryVO);
    }

    @PostMapping(value = "/deleteValue")
    public ResponseResult<DictionaryVO> deleteValue(@RequestParam ObjectId id, String value){
        DictionaryVO dictionaryVO = new DictionaryVO();
        BeanUtils.copyProperties(dictionaryService.deleteValue(id, value), dictionaryVO);
        return ResponseResult.success(dictionaryVO);
    }

}
