package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.SearchService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/article")
public class SearchController {
    @Autowired
    private SearchService searchService;
    @PostMapping("/search")
    public Result saveIndex(@RequestBody Article article){
        return searchService.saveIndex(article);
    }

    /**
     * 根据关键字查询
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{keyword}/{page}/{size}")
    public Result findArticleByKeyword(@PathVariable("keyword")String keyword,@PathVariable("page")int page,@PathVariable("size")int size){
        return searchService.findByKeyWord(keyword,page,size);
    }
}
