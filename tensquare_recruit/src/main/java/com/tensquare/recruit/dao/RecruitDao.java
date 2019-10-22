package com.tensquare.recruit.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Recruit;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{
    /**
     * 推荐职位，状态为2且按时间排序
     * @param pageable
     * @return
     */
    @Query(value = "select * from tb_recruit where state=2 order by createtime desc",nativeQuery = true)
    public Page<Recruit> findRecommendRecruit(Pageable pageable);

    /**
     * 最新职位，要求时间排序且状态！=0
     * @param pageable
     * @return
     */
    @Query(value = "select * from tb_recruit where state!=0 order by createtime desc",nativeQuery = true)
    public Page<Recruit> findNewRecruit(Pageable pageable);
}
