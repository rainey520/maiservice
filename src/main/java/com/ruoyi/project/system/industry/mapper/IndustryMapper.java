package com.ruoyi.project.system.industry.mapper;

import com.ruoyi.project.system.industry.domain.Industry;
import java.util.List;	

/**
 * 行业管理 数据层
 * 
 * @author zqm
 * @date 2019-07-10
 */
public interface IndustryMapper 
{
	/**
     * 查询行业管理信息
     * 
     * @param id 行业管理ID
     * @return 行业管理信息
     */
	public Industry selectIndustryById(Integer id);
	
	/**
     * 查询行业管理列表
     * 
     * @param industry 行业管理信息
     * @return 行业管理集合
     */
	public List<Industry> selectIndustryList(Industry industry);
	
	/**
     * 新增行业管理
     * 
     * @param industry 行业管理信息
     * @return 结果
     */
	public int insertIndustry(Industry industry);
	
	/**
     * 修改行业管理
     * 
     * @param industry 行业管理信息
     * @return 结果
     */
	public int updateIndustry(Industry industry);
	
	/**
     * 删除行业管理
     * 
     * @param id 行业管理ID
     * @return 结果
     */
	public int deleteIndustryById(Integer id);
	
	/**
     * 批量删除行业管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteIndustryByIds(String[] ids);

	/**
	 * 查询所以可用行业
	 * @return
	 */
	List<Industry> selectIsAll();
	
}