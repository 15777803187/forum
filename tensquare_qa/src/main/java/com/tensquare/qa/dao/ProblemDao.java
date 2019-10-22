package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    /**
     * 查询某个标签下的最新问题(根据中间表得到该标签的所有问题id，然后从问题表中获取问题)
     * @param
     * @param
     * @return
     */
    @Query(value = "SELECT * FROM tb_problem  WHERE id IN(SELECT problemid FROM tb_pl WHERE labelid = ?) ORDER BY createtime DESC" ,nativeQuery = true)
    public Page<Problem> findNewListByLabelId(String labelId,Pageable pageable);

    /**
     * 热门问题
     * @param labelId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM tb_problem  WHERE id IN(SELECT problemid FROM tb_pl WHERE labelid = ?) ORDER BY reply DESC",nativeQuery = true)
    public Page<Problem> findHotListByLabelId(String labelId,Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem  WHERE id IN(SELECT problemid FROM tb_pl WHERE labelid = ?) AND reply=0 ORDER BY createtime DESC",nativeQuery = true)
    public Page<Problem> findWaitListByLabelId(String labelId,Pageable pageable);

    /**
     * 查询该用户的问题
     * @param userid
     * @return
     */
    public Page<Problem> findByUserid(String userid,Pageable pageable);

    /**
     * 查询出未审核的问题，由user微服务的admin角色使用
     * @param state
     * @param pageable
     * @return
     */
    public Page<Problem> findByState(String state,Pageable pageable);
}
