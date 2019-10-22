package com.tensquare.user.dao;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User,String> {
    public User findByMobile(String mobile);
    public void deleteById(String id);

    @Modifying
    @Query(value = "update tb_user set fans=fans+? where id=?",nativeQuery = true)
    public void updateFans(int x,String uid);


    @Modifying
    @Query(value = "update tb_user set concern=concern+? where id=?",nativeQuery = true)
    public void updateConcert(int x,String uid);
}
