package com.tensquare.friend.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("tensquare-user")
public interface UserClient {
    @PutMapping("/user/fans/{x}/{uid}")
    public Result updateFans(@PathVariable("x") int x ,@PathVariable("uid") String uid);

    @PutMapping("/user/concern/{x}/{uid}")
    public Result updateConcern(@PathVariable("x") int x ,@PathVariable("uid") String uid);
}
