package org.thebreak.roombooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.common.response.PageResult;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.vo.DictionaryVO;
import org.thebreak.roombooking.service.DictionaryService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "api/v1/dicts")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;


    @PostMapping(value = "/add")
    public ResponseResult<DictionaryVO> addDictionary(@RequestParam String name){
        Dictionary dictionary = dictionaryService.addDictionary(name);
        DictionaryVO dictionaryVO = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, dictionaryVO);
        return ResponseResult.success(dictionaryVO);
    }

    @GetMapping()
    @Operation(summary = "Get dictionary",
            description = "Get paged list of dictionary, default is page 1 and size 10 if not provided.")
    public ResponseResult<PageResult<DictionaryVO>> findPage(
            @RequestParam @Nullable @Parameter(description = "default is 1 if not provided") Integer page,
            @RequestParam @Nullable @Parameter(description = "ax limited to 50, default is 10 if not provided") Integer size){
        Page<Dictionary> dictPage = dictionaryService.findPage(page, size);
        List<DictionaryVO> voList = new ArrayList<>();
        for (Dictionary dictionary : dictPage.getContent()) {
            DictionaryVO dictionaryVO = new DictionaryVO();
            BeanUtils.copyProperties(dictionary, dictionaryVO);
            voList.add(dictionaryVO);
        }

        // assemble page result
        PageResult<DictionaryVO> pageResult = new PageResult<>(dictPage, voList);

        return ResponseResult.success(pageResult);
    }

    @GetMapping(value = "/byName")
    @Operation(summary = "Get dictionary by name",
            description = "name provided as request parameters.")
    public ResponseResult<DictionaryVO> findByName(@RequestParam String name){
        Dictionary dictionary = dictionaryService.findByName(name);
        DictionaryVO dictionaryVO = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, dictionaryVO);
        return ResponseResult.success(dictionaryVO);
    }

    @GetMapping(value = "/byId/{id}")
    @Operation(summary = "Get dictionary by id",
            description = "id provided as path variable.")
    public ResponseResult<DictionaryVO> findById(@PathVariable String id){
        Dictionary dictionary = dictionaryService.findById(id);
        DictionaryVO dictionaryVO = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, dictionaryVO);
        return ResponseResult.success(dictionaryVO);
    }

    @PutMapping(value = "/update")
    @Operation(summary = "Update a room",
            description = "please send over all the fields when update.")
    public ResponseResult<DictionaryVO> update(@RequestParam String id, String name){
        Dictionary dictionary = dictionaryService.updateById(id, name);
        DictionaryVO dictionaryVO = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, dictionaryVO);
        return ResponseResult.success(dictionaryVO);
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseResult<?> deleteById(@PathVariable String id){
        dictionaryService.deleteById(id);
        return ResponseResult.success();
    }

    @PostMapping(value = "/addValue")
    public ResponseResult<DictionaryVO> addValue(@RequestParam String id, String value){
        DictionaryVO dictionaryVO = new DictionaryVO();
        BeanUtils.copyProperties(dictionaryService.addValueById(id, value), dictionaryVO);
        return ResponseResult.success(dictionaryVO);
    }

    @DeleteMapping(value = "/deleteValue")
    public ResponseResult<DictionaryVO> deleteValue(@RequestParam String id, String value){
        DictionaryVO dictionaryVO = new DictionaryVO();
        BeanUtils.copyProperties(dictionaryService.deleteValue(id, value), dictionaryVO);
        return ResponseResult.success(dictionaryVO);
    }

}
