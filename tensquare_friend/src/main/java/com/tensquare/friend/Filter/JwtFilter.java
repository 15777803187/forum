package com.tensquare.friend.Filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends HandlerInterceptorAdapter{
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if(token!=null&&!"".equals(token)){
            if(token.startsWith("Bearer ")){
                throw new RuntimeException("权限不足");
            }
            token = token.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            if(claims!=null){
                String role = (String) claims.get("roles");
                if(role!=null) {
                    if("user".equals(role)) {
                        request.setAttribute("role", "user");
                    }else if("admin".equals(role)){
                        request.setAttribute("role", "admin");
                    }
                }else {
                    throw new RuntimeException("权限不足");
                }
            }else {
                throw new RuntimeException("权限不足");
            }
        }
        return true;
    }
}
