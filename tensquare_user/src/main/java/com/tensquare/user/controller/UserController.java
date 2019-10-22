package com.tensquare.user.controller;

import com.tensquare.user.client.ProblemClient;
import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProblemClient problemClient;
    @PostMapping("/{mobile}")
    public Result sendSms(@PathVariable("mobile") String mobile){
        return userService.sendSms(mobile);
    }

    @PostMapping("/regist/{code}")
    public Result sendSms(@RequestBody User user,@PathVariable("code")String code){
        return userService.UserRegist(user,code);
    }
    @PostMapping("/login")
    public Result UserLogin(@RequestBody User user){
        return userService.UserLogin(user);
    }
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable String id){
        return userService.deleteUserById(id);
    }

    @PutMapping("/fans/{x}/{uid}")
    public Result addFans(@PathVariable("x") int x,@PathVariable("uid") String uid){
       return userService.updateFans(x,uid);
    }

    @PutMapping("/concern/{x}/{uid}")
    public Result addConcern(@PathVariable("x") int x,@PathVariable("uid") String uid){
        return userService.updateConcern(x,uid);
    }

    @GetMapping("/problem/{userid}/{page}/{size}")
    public Result findUserProblem(@PathVariable("userid")String userid, @PathVariable("page")int page, @PathVariable("size")int size){
        return problemClient.findUserProblem(userid,page,size);
    }

}
