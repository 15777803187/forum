package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;

    /**
     * 发布吐槽：如果是子吐槽则父吐槽+1
     *
     * @param spit
     * @return
     */
    @PostMapping
    public Result saveSpit(@RequestBody Spit spit) {
        return spitService.saveSpit(spit);
    }

    /**
     * 根据父id查询吐槽
     * * @param parentId
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/common/{parentId}/{page}/{size}")
    public Result findSpitByParentId(@PathVariable("parentId") String parentId, @PathVariable("page") int page, @PathVariable("size  ") int size) {
        return spitService.findSpitByParentId(parentId, page, size);
    }

    /**
     * 点赞，使用redis控制同一个用户不能重复点赞
     *
     * @param id
     * @return
     */
    @PutMapping("/thumbup/{id}")
    public Result addThumbup(@PathVariable("id") String id) {
        return spitService.addThumbup(id);
    }
    @PutMapping("/share/{id}")
    public Result addShare(@PathVariable("id") String id) {
        return spitService.addShare(id);
    }
    @PutMapping("/visits/{id}")
    public Result addVisit(@PathVariable("id") String id) {
        return spitService.addVisits(id);
    }
}
