package com.ruoyi.project.system.indexSetting;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.service.IDevCompanyService;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 首页设置控制层
 *
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.project.system.user.controller
 * @Author: Administrator
 * @Date: 2019/3/30 17:23
 * @Description //TODO
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/indexSetting")
public class IndexSettingController {

    private String prefix = "system/indexSetting";

    @Autowired
    private IDevCompanyService companyService;

    /**
     * 跳转到首页设置界面
     *
     * @return
     */
    @GetMapping("/indexSetting")
    public String userDetail(ModelMap mmap, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        DevCompany company = companyService.selectDevCompanyById(user.getCompanyId());
        if (company == null || company.getComPicture() == null) {
            mmap.put("comPictures", null);
        } else {
            mmap.put("comPictures", JSON.parseArray(company.getComPicture()));
        }
        mmap.put("company", company);
        return prefix + "/indexSetting";
    }
}
