package com.tensquare.wenzull.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;

public class WebFilter extends ZuulFilter{
    /**
     * 过滤器类型，前置，后置。。。
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器等级，越小越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    /**
     * 业户代码
     * @return
     * @throws ZuulException
     */
    /**
     * 由于使用网关转发会导致头信息丢失，所以对于web网关如果携带了jwt头信息，则需要取出来重新封装到request,这个网关总是放行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String token = request.getHeader("Authorization");
        if(token!=null&&!"".equals(token)){
            //重新封装到头信息
            requestContext.addZuulRequestHeader("Authorization",token);
        }
        return null;
    }
}
