package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend,String>{
    /**
     * 用来查询两个用户之间是否有联系(单向，双向)
     * @param userid
     * @param friendid
     * @return
     */
    @Query(value = "select count(*) from tb_friend where userid=? and friendid=?",nativeQuery = true)
    public int selectCount(String userid,String friendid);

    @Modifying
    @Query(value = "update tb_friend set islike=? where userid=? and friendid=?",nativeQuery = true)
    public void updateFriend(String islike,String uid,String friendid);

    @Modifying
    @Query(value = "delete from tb_friend where userid=? and friendid=?",nativeQuery = true)
    public void delteFriend(String uid,String fid);
}
