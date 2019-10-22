package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.Admin;
import com.tensquare.user.pojo.User;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;


    /**
     * 用户请求获取验证码，随机生成6位数验证码，然后存进redis中等注册时验证，然后将mobile和code发送到消息队列中
     * * @param mobile
     * @return
     */
    public Result sendSms(String mobile){
        if(mobile==null){
            throw new RuntimeException("请填写手机号");
        }
        Random random = new Random();
        int max = 999999;
        int min = 100000;
        int code = random.nextInt(max);
        if(code<min){
            code += min;
        }
        redisTemplate.opsForValue().set("mobile_"+mobile,code+"",1, TimeUnit.HOURS);
        /**暂时取消掉往消息队列发送消息，然后被消费端监听到并发送短信验证码的业务
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("code",code+"");
        rabbitTemplate.convertAndSend("sms",map);
         */
       // return new Result(true, StatusCode.OK,"验证码发送成功");
        return new Result(true, StatusCode.OK,"验证码发送成功",code);//采用便携方式获取验证码用于测试
    }

    /**
     * 用户注册，传入用户在前端界面填写的基本信息和验证码，然后判断验证码是否存在和是否与获取时存入redis中的验证码一致，
     * 都正确的话对用户信息初始化存入数据库中
     * @param user
     * @param code
     * @return
     */
    public Result UserRegist(User user,String code){
       String code2 = (String) redisTemplate.opsForValue().get("mobile_"+user.getMobile());
       if(code2==null){
           throw new RuntimeException("请先获取验证码");
       }
       if(!code2.equals(code)){
           throw new RuntimeException("验证码错误");
       }
       user.setId(idWorker.nextId()+"");
       //密码加密
       user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
       user.setFans("0");
       user.setConcern("0");
       userDao.save(user);
       return new Result(true,StatusCode.OK,"注册成功");
    }

    /**
     * 与admin一样，用户登录不仅要验证账户密码，在验证成功后还要返回JWT令牌
     * @param user
     * @return
     */
    public Result UserLogin(User user){
        User loginUser = userDao.findByMobile(user.getMobile());
        if(loginUser!=null&&bCryptPasswordEncoder.matches(user.getPassword(),loginUser.getPassword())){
            String token = jwtUtil.createJWT(loginUser.getId(),loginUser.getMobile(),"user");
            Map map = new HashMap();
            map.put("token",token);
            map.put("mobile",loginUser.getMobile());
            map.put("roles","user");
            return new Result(true,StatusCode.OK,"User登录成功",map);
        }
        return new Result(false,StatusCode.REPERROR,"登录失败");

    }

    public Result deleteUserById(String id){
        /**
         * 由于请求到来前经过了拦截器，会对带有JWT头信息的请求进行JWT格式判断和解析，然后将角色信息存入request,
         * 所以这里只需要判断角色信息就绪
         */
        String role = (String) request.getAttribute("role");
        if(role==null||"".equals(role)||!"admin".equals(role)){
            throw  new RuntimeException("权限不足");
        }
        userDao.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    public Result updateFans(int x,String uid){
        userDao.updateFans(x,uid);
        return new Result(true,StatusCode.OK,"粉丝+1");
    }

    public Result updateConcern(int x,String uid){
        userDao.updateConcert(x,uid);
        return new Result(true,StatusCode.OK,"关注+1");
    }
}
