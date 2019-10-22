package com.tensquare.user.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "tensquare-qa")
public interface ProblemClient {
    @GetMapping("/problem/{userid}/{page}/{size}")
    public Result findUserProblem(@PathVariable("userid")String userid, @PathVariable("page")int page, @PathVariable("size")int size);

    @GetMapping("/problem/state/{page}/{size}")
    public Result findByState(@PathVariable("page")int page,@PathVariable("size")int size);
}
