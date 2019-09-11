package com.ruoyi.framework.shiro.service;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.PasswordUtil;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.device.devCompany.service.IDevCompanyService;
import com.ruoyi.project.system.ser.domain.Ser;
import com.ruoyi.project.system.ser.service.ISerService;
import com.ruoyi.project.system.serPort.domain.SerPort;
import com.ruoyi.project.system.serPort.service.ISerPortService;
import com.ruoyi.project.system.user.domain.Login;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.UserBlockedException;
import com.ruoyi.common.exception.user.UserDeleteException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.domain.UserStatus;
import com.ruoyi.project.system.user.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class LoginService
{
    //@Autowired
    //private PasswordService passwordService;



    @Autowired
    private ISerService serService;

    @Autowired
    private ISerPortService serPortService;

    @Autowired
    private IUserService userService;

    /**
     * 登录
     */
    public AjaxResult login(Login login)
    {
        // 验证码校验
        if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA)))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(login.getUsername(), Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(login.getUsername()) || StringUtils.isEmpty(login.getUsername()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(login.getUsername(), Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (login.getPassword().length() < UserConstants.PASSWORD_MIN_LENGTH
                || login.getPassword().length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(login.getUsername(), Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 用户名不在指定范围内 错误
        if (login.getUsername().length() < UserConstants.USERNAME_MIN_LENGTH
                || login.getUsername().length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(login.getUsername(), Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 查询用户信息
        User user = userService.selectByLoginName(login.getUsername());
        if (user == null && maybeMobilePhoneNumber(login.getUsername()))
        {
            user = userService.selectUserByPhoneNumber(login.getUsername());
        }

        if (user == null && maybeEmail(login.getUsername()))
        {
            user = userService.selectUserByEmail(login.getUsername());
        }

        if (user == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(login.getUsername(), Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }
        
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(login.getUsername(), Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete")));
            throw new UserDeleteException();
        }
        
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(login.getUsername(), Constants.LOGIN_FAIL, MessageUtils.message("user.blocked", user.getRemark())));
            throw new UserBlockedException();
        }
        // 用户名密码错误
        if (!PasswordUtil.matches(user, login.getPassword())) {
            throw new UserPasswordNotMatchException();
        }
        String path ="/s";
        String ip =null;
        if(user.getTag().equals("0") && user.getCompanyId() > 0){
            //查询对应服务器端口配置
            SerPort serPort = serPortService.selectSerPortByCompanyId(user.getCompanyId());
            //查询对应服务器信息
            if(serPort !=null) {
                Ser ser = serService.selectSerById(serPort.getSid());
                if(ser != null){
                    ip = ser.getSpath()+":"+serPort.getPort();
                    path = ser.getSpath()+":"+serPort.getPort()+"/s";
                }
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(login.getUsername(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        user.setDevType(login.getDevType());
        user.setDevCode(login.getDevCode());
        recordLoginInfo(user);
        Map<String,Object> map = new HashMap<>(16);
        map.put(JwtUtil.CLAIM_KEY_USER, JSON.toJSONString(user));
        return AjaxResult.login(path,JwtUtil.getToken(map),Integer.parseInt(user.getTag()),
                login.getUsername(),ip,user.getCompanyId() == -1?0:1,user.getUserId().intValue(),user.getUserName(),user.getEmail());
    }

    private boolean maybeEmail(String username)
    {
        if (!username.matches(UserConstants.EMAIL_PATTERN))
        {
            return false;
        }
        return true;
    }

    private boolean maybeMobilePhoneNumber(String username)
    {
        if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN))
        {
            return false;
        }
        return true;
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(User user)
    {
        user.setLoginIp(ShiroUtils.getIp());
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserInfo(user);
    }

    public AjaxResult loginOut(User user){
        if (com.ruoyi.common.utils.StringUtils.isNotNull(user))
        {
            String loginName = user.getLoginName();
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        return AjaxResult.success();
    }
}
