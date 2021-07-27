package com.brothermiles.studyon.cms.controller;

import com.brothermiles.studyon.api.cms.CmsPageControllerApi;
import com.brothermiles.studyon.cms.service.PageService;
import com.brothermiles.studyon.common.model.response.QueryListResponseResult;
import com.brothermiles.studyon.common.model.response.ResponseResult;
import com.brothermiles.studyon.model.cms.CmsPage;
import com.brothermiles.studyon.model.cms.request.QueryPageRequest;
import com.brothermiles.studyon.model.cms.response.CmsPageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;


    @GetMapping("list/{page}/{size}")
    public QueryListResponseResult findList(@PathVariable int page, @PathVariable int size, QueryPageRequest queryPageRequest) {

        return pageService.findList(page, size, queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {

        return pageService.add(cmsPage);
    }

    @Override
    @GetMapping("get/{id}")
    public CmsPage findById(@PathVariable String id) {
        return pageService.findById(id);
    }

    @Override
    @PutMapping("edit/{id}")
    public CmsPageResult edit(@PathVariable String id, @RequestBody CmsPage cmsPage) {

        return pageService.update(id, cmsPage);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteById(@PathVariable String id) {
        return pageService.deleteById(id);
    }
}
