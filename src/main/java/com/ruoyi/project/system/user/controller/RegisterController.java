package com.ruoyi.project.system.user.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.service.IDevCompanyService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 用户注册
 *
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.project.system.user.controller
 * @Author: Administrator
 * @Date: 2019/4/3 16:29
 * @Description //TODO
 * @Version: 1.0
 **/
@Controller
public class RegisterController extends BaseController {

    @Autowired
    private IUserService userService;

    /**
     * 跳转注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }


    /**
     * 注册新增保存用户
     *
     * @param user
     * @return
     */
    @PostMapping("/registerAdd")
    @ResponseBody
    public AjaxResult addUser(@RequestBody User user) {
        try {
            return toAjax(userService.registerUser(user));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
}
