package com.ruoyi.project.system.user.api;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息接口
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.project.system.user.api
 * @Author: Administrator
 * @Date: 2019/6/10 18:43
 * @Description //TODO
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/system/user/api")
public class UserApi {

    @Autowired
    private IUserService userService;

    /**
     * 新增用户信息
     */
    @RequestMapping("/add")
    public AjaxResult add(@RequestBody User user,HttpServletRequest request){
        try {
            return AjaxResult.success("success",userService.apiAdd(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error();
    }

    /**
     * 修改用户基本信息
     */
    @RequestMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult edit(@RequestBody User user, HttpServletRequest request){
        try {
            userService.apiEdit(user,request);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error();
    }

    /**
     * 用户登录初始化设置
     */
    @RequestMapping("/changeLoginTag")
    public AjaxResult changeLoginTag(@RequestBody User user,HttpServletRequest request){
        try {
            userService.changeLoginTag(user);
            return AjaxResult.success();
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * 删除用户信息
     */
    @RequestMapping("/remove")
    public AjaxResult remove(@RequestBody String ids,HttpServletRequest request){
        try {
            userService.apiRemove(ids,request);
            return AjaxResult.success();
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改用户删除状态
     * @param id
     * @return
     */
    @RequestMapping("/updateUserDelFlag")
    public AjaxResult updateUserDelFlag(@RequestBody User user){
        try {
            userService.updateUserDelFlag(user.getUserId().intValue(),user.getCompanyId());
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.error();
    }
}
