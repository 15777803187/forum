package com.tensquare.search.dao;

import com.tensquare.search.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface ProblemDao extends ElasticsearchCrudRepository<Problem,String>{
    /**
     * 根据一个关键字匹配标题和内容
     * @param title
     * @param content
     * @return
     */
    public Page<Problem> findByTitleOrContent(String title, String content, Pageable pageable);
}
