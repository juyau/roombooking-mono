package com.brothermiles.studyon.cms.dao;

import com.brothermiles.studyon.model.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {

    CmsPage findByPageAliase(String pageAliase);

    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String
            pageWebPath);
}
