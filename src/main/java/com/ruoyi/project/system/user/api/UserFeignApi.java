package com.ruoyi.project.system.user.api;

import com.ruoyi.project.system.user.domain.User;
import feign.Headers;
import feign.RequestLine;

import java.util.HashMap;

/**
 * 调用从服务器用户操作接口
 */
public interface UserFeignApi {

    @RequestLine("POST /u/init")
    @Headers("Content-Type: application/json")
    HashMap<String,Object> initUserInfo(User user);
}
