package com.ruoyi.common.feign.initdata.api;

import com.ruoyi.common.feign.initdata.domain.InitData;
import feign.Headers;
import feign.RequestLine;

import java.util.HashMap;

public interface InitDataFeignApi {
    /**
     * 初始化用户数据
     * @param initData
     * @return
     */
    @RequestLine("POST /init/data")
    @Headers("Content-Type: application/json")
    HashMap<String,Object> initData(InitData initData);
}
