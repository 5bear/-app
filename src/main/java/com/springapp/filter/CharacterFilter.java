package com.springapp.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by 11369 on 2016/10/6.
 */
public class CharacterFilter implements Filter {
    //字符编码
    String encoding=null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //获取初始化参数
        encoding="UTF-8";
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(encoding!=null){
            //设置request字符编码
            servletRequest.setCharacterEncoding(encoding);
            //设置response字符编码
            servletResponse.setContentType("text/html;charset="+encoding);
        }
        //传递给下一个过滤器
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        encoding=null;
    }
}
