package com.tensquare.recruit.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Enterprise;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface EnterpriseDao extends JpaRepository<Enterprise,String>,JpaSpecificationExecutor<Enterprise>{
    /**
     * 热门企业
     * @param pageable
     * @return
     */
    @Query(value = "select * from tb_enterprise where ishot = 1",nativeQuery = true)
	public Page<Enterprise> findIsHost(Pageable pageable);
}
