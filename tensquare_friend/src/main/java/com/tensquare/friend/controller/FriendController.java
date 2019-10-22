package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @PostMapping("/add/{uid}/{fid}")
    public Result addFriend(@PathVariable("uid") String uid,@PathVariable("fid") String fid){
        return friendService.addFriend(uid,fid);
    }
    @DeleteMapping("/delete/{uid}/{fid}")
    public Result deleteFriend(@PathVariable("uid") String uid,@PathVariable("fid") String fid){
        return friendService.deleteFriend(uid,fid);
    }
}
