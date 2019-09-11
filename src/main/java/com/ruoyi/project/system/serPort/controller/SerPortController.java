package com.ruoyi.project.system.serPort.controller;

import java.util.List;

import com.ruoyi.project.system.ser.service.ISerService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.serPort.domain.SerPort;
import com.ruoyi.project.system.serPort.service.ISerPortService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 服务器端口配置 信息操作处理
 * 
 * @author zqm
 * @date 2019-06-10
 */
@Controller
@RequestMapping("/system/serPort")
public class SerPortController extends BaseController
{
    private String prefix = "system/serPort";
	
	@Autowired
	private ISerPortService serPortService;

	@Autowired
	private ISerService serService;
	
	@RequiresPermissions("system:serPort:view")
	@GetMapping()
	public String serPort(int sid,ModelMap mmap)
	{
		mmap.put("ser",serService.selectSerById(sid));
	    return prefix + "/serPort";
	}
	
	/**
	 * 查询服务器端口配置列表
	 */
	@RequiresPermissions("system:serPort:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SerPort serPort)
	{
		startPage();
        List<SerPort> list = serPortService.selectSerPortList(serPort);
		return getDataTable(list);
	}

	
	/**
	 * 新增服务器端口配置
	 */
	@GetMapping("/add")
	public String add(int sid,ModelMap mmap)
	{
		mmap.put("sid",sid);
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存服务器端口配置
	 */
	@RequiresPermissions("system:serPort:add")
	@Log(title = "服务器端口配置", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SerPort serPort)
	{
		try {
			return toAjax(serPortService.insertSerPort(serPort));
		}catch (Exception e){
			return AjaxResult.error(e.getMessage());
		}
	}

	/**
	 * 修改服务器端口配置
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		SerPort serPort = serPortService.selectSerPortById(id);
		mmap.put("serPort", serPort);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存服务器端口配置
	 */
	@RequiresPermissions("system:serPort:edit")
	@Log(title = "服务器端口配置", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SerPort serPort)
	{
		try {
			return toAjax(serPortService.updateSerPort(serPort));
		}catch (Exception e){
			return AjaxResult.error(e.getMessage());
		}

	}
	
	/**
	 * 删除服务器端口配置
	 */
	@RequiresPermissions("system:serPort:remove")
	@Log(title = "服务器端口配置", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(Integer id)
	{		
		return toAjax(serPortService.deleteSerPortById(id));
	}

	/**
	 * 检查对应端口是否已经配置
	 * @param id 编号
	 * @return
	 */
	@PostMapping( "/portIsConfig")
	@ResponseBody
	public AjaxResult portIsConfig(int id){
		return AjaxResult.success("检查端口是否配置",serPortService.portIsConfig(id));
	}

	/**
	 * 指定公司
	 * @param id
	 * @return
	 */
	@GetMapping("/configCompany/{id}")
	public String configComany(@PathVariable("id") int id,ModelMap mmap){
		SerPort serPort = serPortService.selectSerPortById(id);
		mmap.put("serPort", serPort);
		return prefix+"/configCompany";
	}

	/**
	 * 指定公司
	 * @param serPort
	 * @return
	 */
	@RequiresPermissions("system:serPort:edit")
	@Log(title = "服务器端口配置", businessType = BusinessType.UPDATE)
	@PostMapping("/configCompany")
	@ResponseBody
	public AjaxResult configCompanyEdit(SerPort serPort){
		return toAjax(serPortService.configCompanyEdit(serPort));
	}

	@Log(title = "根据服务器id查询对应的端口", businessType = BusinessType.UPDATE)
	@PostMapping("/selectBySid")
	@ResponseBody
	public AjaxResult selectSerPortBySerId(int sid){
		try {
			return AjaxResult.success("ok",serPortService.selectSerPortBySerId(sid));
		}catch (Exception e){
			e.printStackTrace();
			return error();
		}
	}

}
