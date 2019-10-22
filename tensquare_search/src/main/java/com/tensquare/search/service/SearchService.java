package com.tensquare.search.service;

import com.tensquare.search.dao.SearchDao;
import com.tensquare.search.pojo.Article;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SearchService {
    @Autowired
    private SearchDao searchDao;

    public Result saveIndex(Article article){
        searchDao.save(article);
        return new Result(true, StatusCode.OK,"文章索引保存成功");
    }

    public Result findByKeyWord(String keyword,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        //一个关键字会与标题，简介等索引字段匹配
        Page<Article> pages = searchDao.findByTitleOrContentLike(keyword,keyword,pageable);
        return new Result(true,StatusCode.OK,"关键词搜索文章",new PageResult<Article>(pages.getTotalPages(),pages.getContent()));
    }
}
