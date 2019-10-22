package com.tensquare.qa.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该拦截器总是会放行，因为有些资源并不需要权限，所以它只对带有JWT头信息的请求进行JWT格式判断，进行解析，然后拿出其中的
 * 角色信息存入request中，由后面的具体业务再从request拿出角色判断权限是否足够
 *
 * 它起到提取重复代码，各个业户不需要重复进行JWT格式判断和解析
 */
@Component//bean放到容器中
public class JwtFilter extends HandlerInterceptorAdapter{
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String mesg = request.getHeader("Authorization");
        if(mesg!=null&&!"".equals(mesg)){
            if(mesg.startsWith("Bearer ")) {
                String token = mesg.substring(7);
                Claims claims = jwtUtil.parseJWT(token);
                if (claims != null) {
                    String role = (String) claims.get("roles");
                    if ("user".equals(role)) {
                        request.setAttribute("role", "user");
                    } else if ("admin".equals(role)) {
                        request.setAttribute("role", "admin");
                    } else {
                        throw new RuntimeException("权限不足");
                    }
                } else {
                    throw new RuntimeException("权限不足");
                }
            }else{
                throw new RuntimeException("权限不足");
            }
        }
        return true;
    }
}
