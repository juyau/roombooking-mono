package com.brothermiles.studyon.cms.service;

import com.brothermiles.studyon.cms.dao.SysDictionaryRepository;
import com.brothermiles.studyon.model.cms.SysDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysDictionaryService {
    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary findByDName(String dName) {
        return sysDictionaryRepository.findByDName(dName);
    }
}
