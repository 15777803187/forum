package com.tensquare.search.service;

import com.tensquare.search.dao.ProblemDao;
import com.tensquare.search.dao.SearchDao;
import com.tensquare.search.pojo.Article;
import com.tensquare.search.pojo.Problem;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Transactional
public class ProblemService {
    @Autowired
    private ProblemDao problemDao;

    public void saveProblem(Problem problem){
        problemDao.save(problem);
    }

    public Result searchProblem(String keyword,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        Page<Problem> pages = problemDao.findByTitleOrContent(keyword,keyword,pageable);
        if(pages==null){
            return new Result(true,StatusCode.OK,"没有找到相关问题");
        }
        /**
         * 对于没有回答或未解决的问题将搜索时间更新，实现顶贴
         */
        for(Problem problem : pages){
            if(problem.getReply()==0||problem.getSolve().equals("0")){
                problem.setSearchtime(new Date());
                saveProblem(problem);
            }
        }
        return new Result(true,StatusCode.OK,"搜索成功",new PageResult<Problem>(pages.getTotalPages(),pages.getContent()));
    }
}
