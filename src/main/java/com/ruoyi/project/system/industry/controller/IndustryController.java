package com.ruoyi.project.system.industry.controller;

import java.util.List;
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
import com.ruoyi.project.system.industry.domain.Industry;
import com.ruoyi.project.system.industry.service.IIndustryService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 行业管理 信息操作处理
 * 
 * @author zqm
 * @date 2019-07-10
 */
@Controller
@RequestMapping("/system/industry")
public class IndustryController extends BaseController
{
    private String prefix = "system/industry";
	
	@Autowired
	private IIndustryService industryService;
	
	@RequiresPermissions("system:industry:view")
	@GetMapping()
	public String industry()
	{
	    return prefix + "/industry";
	}
	
	/**
	 * 查询行业管理列表
	 */
	@RequiresPermissions("system:industry:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Industry industry)
	{
		startPage();
        List<Industry> list = industryService.selectIndustryList(industry);
		return getDataTable(list);
	}


	@ResponseBody
	@RequestMapping("/listAll")
	public AjaxResult listAll(){
		try {
			return AjaxResult.success("ok",industryService.selectIsAll());
		}catch (Exception e){
			return error(e.getMessage());
		}
	}

	/**
	 * 导出行业管理列表
	 */
	@RequiresPermissions("system:industry:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Industry industry)
    {
    	List<Industry> list = industryService.selectIndustryList(industry);
        ExcelUtil<Industry> util = new ExcelUtil<Industry>(Industry.class);
        return util.exportExcel(list, "industry");
    }
	
	/**
	 * 新增行业管理
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存行业管理
	 */
	@RequiresPermissions("system:industry:add")
	@Log(title = "行业管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Industry industry)
	{		
		return toAjax(industryService.insertIndustry(industry));
	}

	/**
	 * 修改行业管理
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Industry industry = industryService.selectIndustryById(id);
		mmap.put("industry", industry);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存行业管理
	 */
	@RequiresPermissions("system:industry:edit")
	@Log(title = "行业管理", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Industry industry)
	{		
		return toAjax(industryService.updateIndustry(industry));
	}
	
	/**
	 * 删除行业管理
	 */
	@RequiresPermissions("system:industry:remove")
	@Log(title = "行业管理", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(industryService.deleteIndustryByIds(ids));
	}
	
}
