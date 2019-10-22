package com.tensquare.article.dao;

import com.tensquare.article.pojo.Commen;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleCommenDao extends MongoRepository<Commen,String>{
    public List<Commen> findByArticleid(String articleid);
}
