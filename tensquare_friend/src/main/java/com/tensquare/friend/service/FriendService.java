package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {
    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendService noFriendService;

    @Autowired
    private UserClient userClient;
    /**
     * 添加用户，单向和互相
     * @param uid
     * @param fid
     * @return
     */
    public Result addFriend(String uid,String fid){
        if(friendDao.selectCount(uid,fid)>0){
            throw new RuntimeException("你已添加过该好友了");
        }
        Friend friend = new Friend();
        friend.setFriendid(fid);
        friend.setUserid(uid);
        friendDao.save(friend);
        //如果被添加的好友 添加过 该用户，就更新两个用户为互相喜欢
        if(friendDao.selectCount(fid,uid)>0){
            friendDao.updateFriend("1",uid,fid);
            friendDao.updateFriend("1",fid,uid);
        }else{
            friendDao.updateFriend("0",uid,fid);
        }
        userClient.updateConcern(1,uid);//用户关注数+1
        userClient.updateFans(1,fid);//被添加用户粉丝数+1
        return new Result(true,StatusCode.OK,"添加成功");
    }

    public Result deleteFriend(String uid,String fid){
        if(friendDao.selectCount(uid,fid)==0){
            throw new RuntimeException("你们还不是好友");
        }
        friendDao.delteFriend(uid,fid);
        userClient.updateConcern(-1,uid);//用户关注数+1
        userClient.updateFans(-1,fid);//被添加用户粉丝数+1
        if(friendDao.selectCount(fid,uid)>0){
            friendDao.updateFriend("0",fid,uid);
        }
        noFriendService.addNoFriend(uid,fid);
        return new Result(true,StatusCode.OK,"好友删除成功");
    }
}
