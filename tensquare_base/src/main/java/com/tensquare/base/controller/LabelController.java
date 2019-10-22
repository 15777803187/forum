package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @GetMapping()
    public Result findAll(){
        return labelService.findAll();
    }
    @GetMapping("/{labelId}")
    public Result findById(@PathVariable String labelId){
        return labelService.findById(labelId);
    }
    @PostMapping
    public Result save(@RequestBody Label label){
        return labelService.save(label);
    }
    @PutMapping("")
    public Result update(@RequestBody Label label){
        return labelService.update(label);
    }
    @DeleteMapping("/{labelId}")
    public Result delete(@PathVariable String labelId){
        return labelService.delete(labelId);
    }

    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label){
        return labelService.findSearch(label);
    }

    @PostMapping("/search/{page}/{size}")
    public Result findSearchWithPage(@RequestBody Label label,@PathVariable("page") int page,@PathVariable("size") int size){
        return labelService.findSearchWithPage(label,page,size);
    }
}
