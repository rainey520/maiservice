package com.ruoyi.project.system.apply.mapper;

import com.ruoyi.project.system.apply.domain.Apply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资质认证 数据层
 * 
 * @author zqm
 * @date 2019-07-11
 */
public interface ApplyMapper 
{
	/**
     * 查询资质认证信息
     * 
     * @param id 资质认证ID
     * @return 资质认证信息
     */
	public Apply selectApplyById(Integer id);

	/**
	 * 根据用户查询当前用户最新的申请信息
	 * @param createId 用户id
	 * @return
	 */
	Apply selectApplyByCreateId(@Param("createId") int createId);
	
	/**
     * 查询资质认证列表
     * 
     * @param apply 资质认证信息
     * @return 资质认证集合
     */
	public List<Apply> selectApplyList(Apply apply);
	
	/**
     * 新增资质认证
     * 
     * @param apply 资质认证信息
     * @return 结果
     */
	public int insertApply(Apply apply);
	
	/**
     * 修改资质认证
     * 
     * @param apply 资质认证信息
     * @return 结果
     */
	public int updateApply(Apply apply);

	
}