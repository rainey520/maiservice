package com.ruoyi.project.device.devCompany.api;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import feign.Headers;
import feign.RequestLine;

import java.util.HashMap;

/**
 * 公司操作信息
 */
public interface CompanyFeignApi {

    /**
     * 初始化公司信息
     * @param company 公司信息
     * @return
     */
    @RequestLine("POST /c/init")
    @Headers("Content-Type: application/json")
    HashMap<String,Object> initCompanyInfo(DevCompany company);
}
