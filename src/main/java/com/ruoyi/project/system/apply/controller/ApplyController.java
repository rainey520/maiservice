package com.ruoyi.project.system.apply.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.system.ser.domain.Ser;
import com.ruoyi.project.system.ser.service.ISerService;
import com.ruoyi.project.system.serPort.domain.SerPort;
import com.ruoyi.project.system.serPort.service.ISerPortService;
import com.ruoyi.project.system.user.controller.ProfileController;
import com.ruoyi.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.apply.domain.Apply;
import com.ruoyi.project.system.apply.service.IApplyService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 资质认证 信息操作处理
 * 
 * @author zqm
 * @date 2019-07-11
 */
@Controller
@RequestMapping("/system/apply")
public class ApplyController extends BaseController
{

    private String prefix = "system/apply";
	
	@Autowired
	private IApplyService applyService;

	@Autowired
	private ISerService serService;

	@Value("${img.license}")
	private String imgLicense;
	
	@RequiresPermissions("system:apply:view")
	@GetMapping()
	public String apply()
	{
	    return prefix + "/apply";
	}
	
	/**
	 * 查询资质认证列表
	 */
	@RequiresPermissions("system:apply:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Apply apply)
	{
		startPage();
        List<Apply> list = applyService.selectApplyList(apply);
		return getDataTable(list);
	}


	/**
	 * 保存头像
	 */
	@Log(title = "执照上传", businessType = BusinessType.INSERT)
	@PostMapping("/license")
	@ResponseBody
	public AjaxResult updateAvatar(@RequestParam("file") MultipartFile file) {
		try {
			if (!file.isEmpty()) {
				String license = FileUploadUtils.upload(RuoYiConfig.getLicense(), file);
				String path = imgLicense+license;
				return AjaxResult.success("ok",path);
			}
			return error();
		} catch (Exception e) {
			return error("上传失败");
		}
	}
	
	/**
	 * 新增资质认证
	 */
	@GetMapping("/add")
	public String add(ModelMap modelMap)
	{
		User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
	    //查询对应用户当前最新申请记录
		modelMap.put("apply",applyService.selectApplyByCreateId(u.getUserId().intValue()));
		return prefix + "/add";
	}

	/**
	 * 查询当前申请进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectApplyStatus")
	public AjaxResult selectApplyStatus(){
		User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
		Apply apply = applyService.selectApplyByCreateId(u.getUserId().intValue());
		if(apply != null){
			Map<String,Object> map = new HashMap<>();
			map.put("examineStatus",apply.getExamineStatus());
			map.put("examineRemark",apply.getExamineRemark());
			return AjaxResult.success("ok",map);
		}
		return AjaxResult.success("ok",null);
	}
	/**
	 * 新增保存资质认证
	 */
	@Log(title = "资质认证", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@RequestBody Apply apply)
	{		
		return toAjax(applyService.insertApply(apply));
	}


	/**
	 * 修改资质认证
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		mmap.put("apply", applyService.selectApplyById(id));
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存资质认证
	 */
	@RequiresPermissions("system:apply:edit")
	@Log(title = "资质认证", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@RequestBody Apply apply)
	{		
		return toAjax(applyService.updateApply(apply));
	}

	/**
	 * 查看资质认证详情
	 */
	@RequiresPermissions("system:apply:detailView")
	@Log(title = "资质认证详情预览", businessType = BusinessType.DETAILS)
	@RequestMapping("/details/{id}")
	public String details(@PathVariable("id") int id,ModelMap modelMap){
		modelMap.put("details",applyService.selectApplyById(id));
		return prefix+"/details";
	}

	/**
	 * 分发服务器
	 */
	@RequiresPermissions("system:apply:server")
	@Log(title = "分发服务器", businessType = BusinessType.SERVICE)
	@GetMapping("/configService/{id}")
	public String configService(@PathVariable("id")int id,ModelMap modelMap){
		//查询对应申请记录
		Apply apply = applyService.selectApplyById(id);
		modelMap.put("apply",apply);
		//查询对应行业的服务器
		if(apply != null){
			List<Ser> sers = serService.selectSerByInId(apply.getApplyIndustry());
			modelMap.put("sers",sers);
		}
		return  prefix+"/configService";
	}

	/**
	 * 保存分发服务器
	 * @param apply 信息
	 * @return
	 */
	@RequiresPermissions("system:apply:server")
	@Log(title = "保存分发服务器", businessType = BusinessType.UPDATE)
	@PostMapping("/configService")
	@ResponseBody
	public AjaxResult configService(@RequestBody Apply apply){
		try {
			applyService.configService(apply);
			return success();
		}catch (Exception e){
			return error(e.getMessage());
		}
	}
	
}
