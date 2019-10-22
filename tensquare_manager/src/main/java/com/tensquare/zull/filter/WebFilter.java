package com.tensquare.zull.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

public class WebFilter extends ZuulFilter {
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 过滤器类型，前置，后置。。。
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器等级，越小越高
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * true表示开始过滤器
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 业户代码
     * @return
     * @throws ZuulException
     */
    /**
     * @return 后台管理网关与web网关不同，web网关面向用户，对于访问总是放行，具体权限在业户中判断，
     * 而后台网关必须在过滤器校验用户是否有"admin"权限
     * 对登录操作总是放行
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String url = request.getRequestURI();
        //等于登录总是放行
        if (url.indexOf("login") > 0) {
            return null;
        }
        String token = request.getHeader("Authorization");//Bearer
        if (token != null && !"".equals(token)) {
            if (token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                Claims claims = jwtUtil.parseJWT(jwt);
                if (claims != null) {
                    String role = (String) claims.get("roles");
                    if ("admin".equals(role)) {
                        //将头信息传递
                        requestContext.addZuulRequestHeader("Authorization", token);
                        return null;//放行
                    }
                }
            }
        }
        //以上未得到权限信息，则表示没有"admin"角色，无法访问后台资源
        requestContext.setSendZuulResponse(false);//终止运行
        requestContext.setResponseStatusCode(401);//http状态码
        requestContext.setResponseBody("无权访问");
        requestContext.getResponse().setContentType("text/html;charset=UTF‐8");
        return null;
    }
}
