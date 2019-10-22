package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;

@Service
@Transactional
public class SpitService {
    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    public Result saveSpit(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        //如果该吐槽有父节点则父节点的回复数+1
        if(spit.getParentid()!=null&&!"".equals(spit.getParentid())&&!"0".equals(spit.getParentid())){
            /**
            Spit spit12 = spitDao.findById(spit.getParentid()).get();
            spit12.setComment(spit12.getComment()+1);
            spitDao.save(spit12);
             */
            //与上面的先查询再添加的方式比起来，使用mongoTemplate可以只与数据库交互一次
            //不过我觉得先查询再添加更加保险
            Query query = new Query();//匹配条件
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();//更新对象
            update.inc("comment",1);//属性commoen(回复)增1
            mongoTemplate.updateFirst(query,update,"spit");//操作集合(表)spit
        }
        spitDao.save(spit);
        return new Result(true, StatusCode.OK,"吐槽成功");
    }
    public Result findSpitByParentId(String parentId,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        Page<Spit> pages = spitDao.findByParentid(parentId,pageable);
        return new Result(true,StatusCode.OK,"查询吐槽",new PageResult<Spit>(pages.getTotalPages(),pages.getContent()));
    }

    /**
     * 点赞，使用缓存限制不可重复点赞
     * @param id
     * @return
     */
    public Result addThumbup(String id){
        String user_id = "222";
        if(redisTemplate.opsForValue().get("user_id"+user_id)!=null){
            return new Result(false,StatusCode.ERROR,"您已经赞过了");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
        redisTemplate.opsForValue().set("user_id"+user_id,"1");
        return new Result(true,StatusCode.OK,"点赞成功");
    }

    public Result addShare(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("share",1);
        mongoTemplate.updateFirst(query,update,"spit");
        return new Result(true,StatusCode.OK,"分享成功");
    }
    //visits
    public Result addVisits(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("visits",1);
        mongoTemplate.updateFirst(query,update,"spit");
        return new Result(true,StatusCode.OK,"分享成功");
    }
}
