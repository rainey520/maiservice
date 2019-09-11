package com.ruoyi.project.system.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.sql.visitor.functions.If;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.shiro.service.LoginService;
import com.ruoyi.project.system.user.domain.Login;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        if (ServletUtils.isAjaxRequest(request)) {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(@RequestBody Login login) {
        try {
            return loginService.login(login);
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return error(msg);
        } catch (BaseException b){
            return error(b.getMessage());
        }
    }


    @PostMapping("/logout")
    @ResponseBody
    public AjaxResult loginOut(HttpServletRequest request){
        return loginService.loginOut(JwtUtil.getTokenUser(request));
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "/error/unauth";
    }

}
