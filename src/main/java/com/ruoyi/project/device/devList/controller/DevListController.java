package com.ruoyi.project.device.devList.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devModel.service.IDevModelService;
import com.ruoyi.project.system.user.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.service.IDevListService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 硬件 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-04-08
 */
@Controller
@RequestMapping("/device/devList")
public class DevListController extends BaseController
{
    private String prefix = "device/devList";
	
	@Autowired
	private IDevListService devListService;

	
	@RequiresPermissions("device:devList:view")
	@GetMapping()
	public String devList()
	{
	    return prefix + "/devList";
	}
	
	/**
	 * 查询硬件列表
	 */
	@RequiresPermissions("device:devList:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(DevList devList,HttpServletRequest request)
	{
		startPage();
        List<DevList> list = devListService.selectDevListList(devList,request);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出硬件列表
	 */
	@RequiresPermissions("device:devList:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DevList devList,HttpServletRequest request)
    {
    	List<DevList> list = devListService.selectDevListList(devList,request);
        ExcelUtil<DevList> util = new ExcelUtil<DevList>(DevList.class);
        return util.exportExcel(list, "devList");
    }

	/**
	 * 添加
	 * @return
	 */
    @GetMapping("/add")
	public String add(){
		return prefix+"/add";
	}
	/**
	 * 新增保存硬件
	 */
	@RequiresPermissions("device:devList:add")
	@Log(title = "硬件", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(int devModelId)
	{		
		return toAjax(devListService.insertDevList(devModelId));
	}

	/**
	 * 修改硬件
	 */
	@RequiresPermissions("device:devList:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		DevList devList = devListService.selectDevListById(id);
		mmap.put("devList", devList);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存硬件
	 */
	@RequiresPermissions("device:devList:edit")
	@Log(title = "硬件", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(DevList devList,HttpServletRequest request)
	{		
		return toAjax(devListService.updateDevList(devList,request));
	}
	
	/**
	 * 删除硬件
	 */
	@RequiresPermissions("device:devList:remove")
	@Log(title = "硬件", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(devListService.deleteDevListByIds(ids));
	}


	@Log(title = "硬件状态", businessType = BusinessType.UPDATE)
	@PostMapping("/changeStatus")
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions("device:devList:edit")
	public AjaxResult changeStatus(DevList devList,HttpServletRequest request){
		return toAjax(devListService.updateDevList(devList,request));
	}
	/**
	 * 获取所有没有配置的硬件编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/no/config")
	public List<String> selectNoConfigDevice(){
		return  devListService.selectNoConfigDevice();
	}

	/**
	 * 修改是否配置硬件状态、一般是服务器管理员操作权限
	 * @param devList
	 * @return
	 */
	@PostMapping("/configDev")
	@ResponseBody
	@RequiresPermissions("device:devList:configDev")
	public AjaxResult configDev(DevList devList,HttpServletRequest request){
		return toAjax(devListService.updateDevList(devList,request));
	}

	/**
	 * 生产条形码
	 * @return
	 */
	@GetMapping("/code")
	public String initCode(int id,ModelMap mmap){
		mmap.put("dev",devListService.selectDevListAndIoById(id));
		return prefix +"/code";
	}

	/**
	 * 用户添加硬件信息
	 * @return
	 */
	//获取添加硬件
	@GetMapping("/add1")
	public String addDev(){
		return  prefix +"/add1";
	}

	/**
	 * 硬件验证是否存在，及其是否被使用
	 * @param code
	 * @return
	 */
	@ResponseBody
	@PostMapping("/validate")
	public int deviceValidate(String code){
		return  devListService.deviceValidate(code);
	}

	/**
	 * 修改保存硬件
	 */
	@RequiresPermissions("device:devList:addsave")
	@Log(title = "用户添加硬件", businessType = BusinessType.UPDATE)
	@PostMapping("/addSave")
	@ResponseBody
	public AjaxResult add1Save(DevList devList,HttpServletRequest request)
	{
		User user = JwtUtil.getTokenUser(request);
		if(user == null){
			return  toAjax(0);
		}
		devList.setCompanyId(user.getCompanyId());
		return toAjax(devListService.addSave(devList));
	}

	@RequiresPermissions("production:productionLine:devconfig")
	@Log(title = "查询硬件配置", businessType = BusinessType.UPDATE)
	@PostMapping("/all")
	@ResponseBody
	public Map<String,Object> selectAll(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("code",devListService.selectAll(request.getCookies()));
		return map;
	}

	/**
	 * 硬件扫描
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/devscan")
	public AjaxResult devScan(int id){
		try {
			devListService.devScan(id);
			return AjaxResult.success();
		}catch (Exception e){
			e.printStackTrace();
		}
		return AjaxResult.error();
	}
}
