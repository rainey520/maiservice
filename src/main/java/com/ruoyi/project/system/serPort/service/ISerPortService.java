package com.ruoyi.project.system.serPort.service;

import com.ruoyi.project.system.serPort.domain.SerPort;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * 服务器端口配置 服务层
 * 
 * @author zqm
 * @date 2019-06-10
 */
public interface ISerPortService 
{
	/**
     * 查询服务器端口配置信息
     * 
     * @param id 服务器端口配置ID
     * @return 服务器端口配置信息
     */
	public SerPort selectSerPortById(Integer id);

	/**
	 * 根据公司id查询对应的服务器端口
	 * @param id
	 * @return
	 */
	public SerPort selectSerPortByCompanyId(Integer id);

	/**
     * 查询服务器端口配置列表
     * 
     * @param serPort 服务器端口配置信息
     * @return 服务器端口配置集合
     */
	public List<SerPort> selectSerPortList(SerPort serPort);
	
	/**
     * 新增服务器端口配置
     * 
     * @param serPort 服务器端口配置信息
     * @return 结果
     */
	public int insertSerPort(SerPort serPort) throws Exception;
	
	/**
     * 修改服务器端口配置
     * 
     * @param serPort 服务器端口配置信息
     * @return 结果
     */
	public int updateSerPort(SerPort serPort) throws Exception;
		
	/**
     * 删除服务器端口配置信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSerPortByIds(String ids);

	/**
	 * 根据id删除
	 * @return
	 */
	public int deleteSerPortById(Integer id);

	/**
	 * 检查对应的端口是否已经配置
	 * @param id 端口id
	 * @return
	 */
	boolean portIsConfig(int id);

	/**
	 * 指定公司
	 * @param serPort
	 * @return
	 */
	int configCompanyEdit(SerPort serPort);

	/**
	 * 根据服务器id查询对应可用端口
	 * @param serId 服务器id
	 * @return
	 */
	List<SerPort> selectSerPortBySerId(int serId);
	
}
