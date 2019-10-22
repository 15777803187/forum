package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface SearchDao extends ElasticsearchCrudRepository<Article,String>{
    /**
     * 两个参数都是使用一个关键字匹配，场景是用户搜素时输入一个关键字，然后后端根据关键字与索引中可被索引的字段匹配
     * @param title_keyword
     * @param connent_keyword
     * @param pageable
     * @return
     */
    public Page<Article> findByTitleOrContentLike(String title_keyword,String connent_keyword, Pageable pageable);
}
