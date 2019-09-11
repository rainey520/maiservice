package com.ruoyi.project.system.serPort.mapper;

import com.ruoyi.project.system.ser.domain.Ser;
import com.ruoyi.project.system.serPort.domain.SerPort;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 服务器端口配置 数据层
 * 
 * @author zqm
 * @date 2019-06-10
 */
public interface SerPortMapper 
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
	 * @param cid
	 * @return
	 */
	SerPort selectSerPortByCompanyId(@Param("cid") int cid);
	
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
	public int insertSerPort(SerPort serPort);

	/**
	 * 查询对应的服务器的端口是否存在
	 * @param sid 服务器id
	 * @param port 端口
	 * @return
	 */
	Integer  findSidPort(@Param("sid")int sid,@Param("port")int port);
	
	/**
     * 修改服务器端口配置
     * 
     * @param serPort 服务器端口配置信息
     * @return 结果
     */
	public int updateSerPort(SerPort serPort);
	
	/**
     * 删除服务器端口配置
     * 
     * @param id 服务器端口配置ID
     * @return 结果
     */
	public int deleteSerPortById(Integer id);
	
	/**
     * 批量删除服务器端口配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSerPortByIds(String[] ids);

	/**
	 * 查询服务器端口配置总数
	 * @param sid 服务器id
	 * @return
	 */
	int countSerPort(@Param("sid")int sid);

	/**
	 * 查询未配置的服务端口
	 * @return
	 */
	SerPort selectNotConfigSerPort();

	/**
	 * 根据公司id查询对应的服务端口
	 * @param companyId 公司id
	 * @return
	 */
	SerPort selectInfoByCompanyId(@Param("companyId")int companyId);

	/**
	 * 根据服务器id查询对应可用端口
	 * @param serId 服务器id
	 * @return
	 */
	List<SerPort> selectSerPortBySerId(@Param("serId")int serId);
	
}