package com.ruoyi.project.system.work;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 生产管理
 */
@Controller
@RequestMapping("/work")
public class WorkController {

    @GetMapping("/list")
    public String list(){
        return "/work/work";
    }

    @GetMapping("/add")
    public String add(){
        return "/work/add";
    }

}
