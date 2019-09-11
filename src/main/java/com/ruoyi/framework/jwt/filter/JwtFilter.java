package com.ruoyi.framework.jwt.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.framework.jwt.JwtToken;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;

@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return super.preHandle(request, response);
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String token = getToken((HttpServletRequest) request);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        User u = JwtUtil.getUserByToken(token);
        if (u == null) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String token = getToken((HttpServletRequest) request);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        JwtToken jwtToken = new JwtToken(token);
        getSubject(request, response).login(jwtToken);
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                return executeLogin(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    writer(request, response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        try {
            writer(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * header 中就是APP 请求方法  cookie 是pc端请求方法
     * @param request
     * @param response
     * @throws Exception
     */
    private void writer(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
//        String token = req.getHeader("token");
        String devtype = req.getHeader("devtype");
        if (!StringUtils.isEmpty(devtype)) {
            response.setContentType("text/html; charset=utf-8");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter writer = resp.getWriter();
            writer.print(JSON.toJSONString(AjaxResult.error("未登录或登录超时。请重新登录")));
            writer.close();
        } else {
            responseindex(response);
        }
    }

    public String getToken(HttpServletRequest req){
        String token = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        } else {
            token = req.getHeader("token");
        }
        return token;
    }

    /**
     * 将非法请求跳转到 /401
     */
    private void responseindex(ServletResponse resp) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.sendRedirect(PathUtils.path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
