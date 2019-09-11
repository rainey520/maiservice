package com.ruoyi.project.device.devList.mapper;

import com.ruoyi.project.device.devList.domain.DevList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 硬件 数据层
 * 
 * @author ruoyi
 * @date 2019-04-08
 */
public interface DevListMapper 
{
	/**
     * 查询硬件信息
     * 
     * @param id 硬件ID
     * @return 硬件信息
     */
	public DevList selectDevListById(Integer id);

	/**
	 * 根据硬件编号查询
	 * @param device_id
	 * @return
	 */
	DevList selectDevListByDevId(@Param("device_id") String device_id);
	
	/**
     * 查询硬件列表
     * 
     * @param devList 硬件信息
     * @return 硬件集合
     */
	public List<DevList> selectDevListList(DevList devList);
	
	/**
     * 新增硬件
     * 
     * @param devList 硬件信息
     * @return 结果
     */
	public int insertDevList(DevList devList);
	
	/**
     * 修改硬件
     * 
     * @param devList 硬件信息
     * @return 结果
     */
	public int updateDevList(DevList devList);

	/**
	 * 用户添加硬件信息
	 * @param devList 硬件信息
	 * @return
	 */
	int addSave(DevList devList);
	
	/**
     * 删除硬件
     * 
     * @param id 硬件ID
     * @return 结果
     */
	public int deleteDevListById(Integer id);
	
	/**
     * 批量删除硬件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteDevListByIds(String[] ids);

	/**
	 * 查询对应的硬件信息和对应的IO数据
	 * @param id
	 * @return
	 */
	DevList selectDevListAndIoById(@Param("id")int id);
	/**
	 * 获取前20个未配置的硬件编号
	 * @return
	 */
	List<String> selectNoConfigDevice();

	/**
	 * 查询所以的硬件信息
	 * @return
	 */
	List<DevList> selectAll(@Param("company_id")int company_id);

	/**
	 * 根据硬件编号查询对应的硬件是否存在
	 * @param code 硬件编号
	 * @return
	 */
	DevList selectDevListByCode(@Param("code")String code);

	/**
	 * 硬件扫描
	 * @param id
	 * @return
	 */
	int devScan(@Param("id")int id);

	/**
	 * api更新硬件
	 * @param devList
	 * @return 结果
	 */
	int updateDevListByDevCode(DevList devList);
}