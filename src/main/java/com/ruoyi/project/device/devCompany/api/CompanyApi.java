package com.ruoyi.project.device.devCompany.api;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.service.IDevCompanyService;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 公司信息接口
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.project.device.devCompany.api
 * @Author: Administrator
 * @Date: 2019/6/11 9:12
 * @Description //TODO
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/company/api")
public class CompanyApi {

    @Autowired
    private IDevCompanyService companyService;

    /**
     * 修改公司基本信息
     */
    @RequestMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult edit(@RequestBody DevCompany company, HttpServletRequest request){
        try {
            companyService.apiEditCompany(company);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error();
    }
}
