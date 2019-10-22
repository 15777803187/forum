package com.tensquare.qa.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.servlet.http.HttpServletRequest;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.tensquare.qa.dao.ProblemDao;
import com.tensquare.qa.pojo.Problem;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class ProblemService {

	@Autowired
	private ProblemDao problemDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Problem> findAll() {
		return problemDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Problem> findSearch(Map whereMap, int page, int size) {
		Specification<Problem> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return problemDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Problem> findSearch(Map whereMap) {
		Specification<Problem> specification = createSpecification(whereMap);
		return problemDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Problem findById(String id) {
		return problemDao.findById(id).get();
	}

	/**
	 * 增加
	 * 需要进行登录认证得到用户user角色
	 * @param problem
	 */
	public void add(Problem problem) {
		String role = (String) request.getAttribute("role");
		if(role==null||"".equals(role)||!"user".equals(role)){
			throw new RuntimeException("请先登录");
		}
		problem.setId( idWorker.nextId()+"" );
		problemDao.save(problem);
	}

	/**
	 * 修改
	 * @param problem
	 */
	public void update(Problem problem) {
		problemDao.save(problem);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		problemDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Problem> createSpecification(Map searchMap) {

		return new Specification<Problem>() {

			@Override
			public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 内容
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 是否解决
                if (searchMap.get("solve")!=null && !"".equals(searchMap.get("solve"))) {
                	predicateList.add(cb.like(root.get("solve").as(String.class), "%"+(String)searchMap.get("solve")+"%"));
                }
                // 回复人昵称
                if (searchMap.get("replyname")!=null && !"".equals(searchMap.get("replyname"))) {
                	predicateList.add(cb.like(root.get("replyname").as(String.class), "%"+(String)searchMap.get("replyname")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}
	public Result findNewProblem(String labelId,int page,int size){
		Pageable pageable = initPageable(labelId,page,size);
		Page<Problem> pages = problemDao.findNewListByLabelId(labelId,pageable);
		return new Result(true, StatusCode.OK,"分页查询最新问题",new PageResult<Problem>(pages.getTotalPages(),pages.getContent()));
	}
	public Result findHotListByLabelId(String labelId,int page,int size){
		Pageable pageable = initPageable(labelId,page,size);
		Page<Problem> pages = problemDao.findHotListByLabelId(labelId,pageable);
		return new Result(true, StatusCode.OK,"分页查询最热问题",new PageResult<Problem>(pages.getTotalPages(),pages.getContent()));
	}
	public Result findWaitListByLabelId(String labelId,int page,int size){
		Pageable pageable = initPageable(labelId,page,size);
		Page<Problem> pages = problemDao.findWaitListByLabelId(labelId,pageable);
		return new Result(true, StatusCode.OK,"分页查询等待回答问题",new PageResult<Problem>(pages.getTotalPages(),pages.getContent()));
	}

	/**
	 * 初始化和封装分页参数
	 * @param labelId
	 * @param page
	 * @param size
	 * @return
	 */
	private Pageable initPageable(String labelId,int page,int size){
		if(labelId==null){
			throw new RuntimeException("参数有误");
		}
		if(page<=0){
			page = 1;
		}
		if(size<=0){
			size = 2;
		}
		Pageable pageable = PageRequest.of(page-1,size);
		return pageable;
	}
	public Result findByUserid(String userid,int page,int size){
		Pageable pageable = PageRequest.of(page-1,size);
		Page<Problem> pages = problemDao.findByUserid(userid,pageable);
		if(pages==null){
			return new Result(true,StatusCode.OK,"还没有发布任何问题");
		}
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<Problem>(pages.getTotalPages(),pages.getContent()));
	}
	public Result findByState(int page,int size){
		Pageable pageable = PageRequest.of(page-1,size);
		Page<Problem> pages = problemDao.findByState("0",pageable);
		return new Result(true,StatusCode.OK,"未审核文章",new PageResult<Problem>(pages.getTotalPages(),pages.getContent()));
	}
}
