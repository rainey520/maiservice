package com.ruoyi.project.system.serPort.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.serPort.mapper.SerPortMapper;
import com.ruoyi.project.system.serPort.domain.SerPort;
import com.ruoyi.project.system.serPort.service.ISerPortService;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务器端口配置 服务层实现
 * 
 * @author zqm
 * @date 2019-06-10
 */
@Service
public class SerPortServiceImpl implements ISerPortService 
{
	@Autowired
	private SerPortMapper serPortMapper;

	/**
     * 查询服务器端口配置信息
     * 
     * @param id 服务器端口配置ID
     * @return 服务器端口配置信息
     */
    @Override
	public SerPort selectSerPortById(Integer id)
	{
	    return serPortMapper.selectSerPortById(id);
	}

	/**
	 * selectSerPortByCompanyId
	 * @param id
	 * @return
	 */
	@Override
	public SerPort selectSerPortByCompanyId(Integer id) {
		return serPortMapper.selectSerPortByCompanyId(id);
	}

	/**
     * 查询服务器端口配置列表
     * 
     * @param serPort 服务器端口配置信息
     * @return 服务器端口配置集合
     */
	@Override
	public List<SerPort> selectSerPortList(SerPort serPort)
	{
	    return serPortMapper.selectSerPortList(serPort);
	}
	
    /**
     * 新增服务器端口配置
     * 
     * @param serPort 服务器端口配置信息
     * @return 结果
     */
	@Override
	public int insertSerPort(SerPort serPort) throws Exception
	{
		//查询对应的端口是否存在
		int count = serPortMapper.findSidPort(serPort.getSid(),serPort.getPort());
		if(count >0){
			throw new Exception("端口已经存在");
		}
		serPort.setCreateTime(new Date());
	    return serPortMapper.insertSerPort(serPort);
	}
	
	/**
     * 修改服务器端口配置
     * 
     * @param serPort 服务器端口配置信息
     * @return 结果
     */
	@Override
	public int updateSerPort(SerPort serPort) throws Exception
	{
		//查询对应的端口是否已经配置
		SerPort port = serPortMapper.selectSerPortById(serPort.getId());
		if(port != null && port.getCompanyId() > 0){
			throw  new Exception("端口已经配置，不能修改");
		}
	    return serPortMapper.updateSerPort(serPort);
	}

	/**
     * 删除服务器端口配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteSerPortByIds(String ids)
	{
		return serPortMapper.deleteSerPortByIds(Convert.toStrArray(ids));
	}

	/**
	 * 根据id删除
	 * @return
	 */
	@Override
	public int deleteSerPortById(Integer id) {
		return serPortMapper.deleteSerPortById(id);
	}

	/**
	 * 检查对应的端口是否已经配置
	 * @param id 端口id
	 * @return
	 */
	@Override
	public boolean portIsConfig(int id) {
		SerPort serPort = serPortMapper.selectSerPortById(id);
		if(serPort != null && serPort.getCompanyId() >0){
			return true;
		}
		return false;
	}

	/**
	 * 指定公司
	 * @param serPort
	 * @return
	 */
	@Override
	@Transactional
	public int configCompanyEdit(SerPort serPort) {
		//查询改公司之前是否有过配置
		SerPort port = serPortMapper.selectInfoByCompanyId(serPort.getCompanyId());
		if(port != null){
			//将其配置公司设置为0
			port.setCompanyId(0);
			serPortMapper.updateSerPort(port);
		}
		serPort.setUpdateTime(new Date());
		return serPortMapper.updateSerPort(serPort);
	}

	/**
	 * 根据服务器id查询对应可用端口
	 * @param serId 服务器id
	 * @return
	 */
	@Override
	public List<SerPort> selectSerPortBySerId(int serId) {
		return serPortMapper.selectSerPortBySerId(serId);
	}
}
