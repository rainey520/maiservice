package com.ruoyi.project.device.devList.api;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.service.IDevListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.project.device.devList.api
 * @Author: Administrator
 * @Date: 2019/6/11 12:17
 * @Description //TODO
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/dev/api")
public class DevListApi {

    @Autowired
    private IDevListService devListService;

    /**
     * 用户添加硬件时校验硬件编码
     */
    @RequestMapping("/validateCode")
    public AjaxResult validateCode(@RequestBody String code, HttpServletRequest request){
        try {
            return AjaxResult.success("success",devListService.apiDeviceValidate(code));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 用户新增硬件
     */
    @RequestMapping("/addSave")
    public AjaxResult addSave(@RequestBody DevList devList,HttpServletRequest request){
        try {
            return AjaxResult.success("success",devListService.apiAddSave(devList));
        } catch (Exception e) {
            return AjaxResult.error();
        }
    }

    /**
     * 用户修改硬件
     */
    @RequestMapping("/edit")
    public AjaxResult edit(@RequestBody DevList devList,HttpServletRequest request){
        try {
            return AjaxResult.success("success",devListService.apiEdit(devList));
        } catch (Exception e) {
            return AjaxResult.error();
        }
    }

    /**
     * 用户删除硬件
     */
    @RequestMapping("/remove")
    public AjaxResult remove(@RequestBody String ids,HttpServletRequest request){
        try {
            return AjaxResult.success("success",devListService.apiRemove(ids));
        } catch (Exception e) {
            return AjaxResult.error();
        }
    }
}
