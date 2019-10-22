package com.tensquare.base.dao;

import com.tensquare.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;//包装简单查询，指定pojo和主键类型
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;//复杂查询，需要手写sql

public interface LabelDao extends JpaRepository<Label,String>,JpaSpecificationExecutor<Label> {
}
