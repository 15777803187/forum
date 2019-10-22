package com.tensquare.friend.service;

import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.NoFriend;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoFriendService {
    @Autowired
    private NoFriendDao nofriendDao;
    public Result addNoFriend(String uid,String fid){
        NoFriend nofriend = new NoFriend();
        nofriend.setFriendid(fid);
        nofriend.setUserid(uid);
        nofriendDao.save(nofriend);
        return new Result(true, StatusCode.OK,"删除成功");
    }
}
