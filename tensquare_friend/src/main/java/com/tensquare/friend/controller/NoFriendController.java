package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import com.tensquare.friend.service.NoFriendService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/nofriend")
public class NoFriendController {
    @Autowired
    private NoFriendService nofriendService;
    @PostMapping("/add/{uid}/{fid}")
    public Result addNoFriend(@PathVariable("uid") String uid,@PathVariable("fid") String fid){
        return nofriendService.addNoFriend(uid,fid);
    }
}
