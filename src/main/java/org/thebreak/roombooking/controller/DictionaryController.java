package org.thebreak.roombooking.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.model.response.ResponseResult;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.service.DictionaryService;
import org.thebreak.roombooking.service.impl.DictionaryServiceImpl;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "dict")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;


    @PostMapping(value = "/add")
    public ResponseResult<Dictionary> addDictionary(@RequestParam String dName){

        Dictionary d = dictionaryService.addDictionary(dName);
        return ResponseResult.success(d);
    }

    @GetMapping(value = "/list")
    public ResponseResult<List<Dictionary>> listDictionary(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5")int size){

        return ResponseResult.success(dictionaryService.listDictionary(page, size));
    }

    @GetMapping(value = "/byDname")
    public ResponseResult<Dictionary> findByDname(@RequestParam String dName){
        System.out.println(dName);
        return ResponseResult.success(dictionaryService.findByDName(dName));
    }

    @GetMapping(value = "/byId")
    public ResponseResult<Dictionary> findById(@RequestParam ObjectId id){
        return ResponseResult.success(dictionaryService.findById(id));
    }

    @PostMapping(value = "/update")
    public ResponseResult updateName(@RequestParam ObjectId id, String dName){
        return ResponseResult.success(dictionaryService.updateById(id, dName));
    }
    @PostMapping(value = "/delete")
    public ResponseResult deleteById(@RequestParam ObjectId id){
        dictionaryService.deleteById(id);
        return ResponseResult.success();
    }

    @PostMapping(value = "/addValue")
    public ResponseResult addValue(@RequestParam ObjectId id, String dValue){
        return ResponseResult.success(dictionaryService.addValueById(id, dValue));
    }

    @PostMapping(value = "/deleteValue")
    public ResponseResult deleteValue(@RequestParam ObjectId id, String dValue){
        return ResponseResult.success(dictionaryService.deleteValue(id, dValue));
    }

}
