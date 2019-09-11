package com.ruoyi.project.device.devList.api;

import com.ruoyi.project.device.devList.domain.DevListResult;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.HashMap;

/**
 * 调用从服务器硬件接口操作
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.project.device.devList.api
 * @Author: Administrator
 * @Date: 2019/6/12 10:42
 * @Description //TODO
 * @Version: 1.0
 **/
public interface DevListFeignApi {

    @RequestLine("POST /cus/dev/edit")
    @Headers({"Content-Type: application/json","Cookie: token={token}"})
    HashMap<String,Object> editDevList(DevListResult devListResult, @Param("token") String token);
}
