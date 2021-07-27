     package com.brothermiles.studyon.cms.controller;

import com.brothermiles.studyon.api.cms.SysDictionaryControllerApi;
import com.brothermiles.studyon.cms.service.SysDictionaryService;
import com.brothermiles.studyon.model.cms.SysDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cms/dictionary/")
public class SysDictionaryController implements SysDictionaryControllerApi {

    @Autowired
    private SysDictionaryService sysDictionaryService;
    @Override
    @GetMapping("bydname")
    public SysDictionary findByDName(@RequestParam String dName) {
        return sysDictionaryService.findByDName(dName);
    }

    @Override
    public SysDictionary findByDType(String dType) {
        return null;
    }
}
