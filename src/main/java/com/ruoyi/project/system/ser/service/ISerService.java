package com.ruoyi.project.system.ser.service;

import com.ruoyi.project.system.ser.domain.Ser;
import java.util.List;

/**
 * 服务器管理 服务层
 * 
 * @author ruoyi
 * @date 2019-06-03
 */
public interface ISerService 
{
	/**
     * 查询服务器管理信息
     * 
     * @param id 服务器管理ID
     * @return 服务器管理信息
     */
	public Ser selectSerById(Integer id);
	
	/**
     * 查询服务器管理列表
     * 
     * @param ser 服务器管理信息
     * @return 服务器管理集合
     */
	public List<Ser> selectSerList(Ser ser);
	
	/**
     * 新增服务器管理
     * 
     * @param ser 服务器管理信息
     * @return 结果
     */
	public int insertSer(Ser ser);
	
	/**
     * 修改服务器管理
     * 
     * @param ser 服务器管理信息
     * @return 结果
     */
	public int updateSer(Ser ser);
		
	/**
     * 删除服务器管理信息
     * 
     * @param id 需要删除的数据ID
     * @return 结果
     */
	public String deleteSerById(int id) throws Exception;

	/**
	 * 查询对应的服务器端口配置数量是否大于等于服务器的最多用户数
	 * @param sid 服务器id
	 * @return
	 */
	boolean findMax(int sid);

	/**
	 * 根据行业id查询对应的可用共用服务器
	 * @param inId 行业id
	 * @return
	 */
	List<Ser> selectSerByInId(int inId);
	
}
