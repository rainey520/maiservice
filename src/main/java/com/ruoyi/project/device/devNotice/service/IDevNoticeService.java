package com.ruoyi.project.device.devNotice.service;

import com.ruoyi.project.device.devNotice.domain.DevNotice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 公司消息通知 服务层
 * 
 * @author zqm
 * @date 2019-04-18
 */
public interface IDevNoticeService 
{
	/**
     * 查询公司消息通知信息
     * 
     * @param id 公司消息通知ID
     * @return 公司消息通知信息
     */
	public DevNotice selectDevNoticeById(Integer id);
	
	/**
     * 查询公司消息通知列表
     * 
     * @param devNotice 公司消息通知信息
     * @return 公司消息通知集合
     */
	public List<DevNotice> selectDevNoticeList(DevNotice devNotice, HttpServletRequest request);
	
	/**
     * 新增公司消息通知
     * 
     * @param devNotice 公司消息通知信息
     * @return 结果
     */
	public int insertDevNotice(DevNotice devNotice,HttpServletRequest request);
	
	/**
     * 修改公司消息通知
     * 
     * @param devNotice 公司消息通知信息
     * @return 结果
     */
	public int updateDevNotice(DevNotice devNotice);
		
	/**
     * 删除公司消息通知信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteDevNoticeByIds(String ids);

	/**
	 * 发布消息
	 * @param id
	 * @return
	 */
    public int publish(Integer id);

	/**
	 * 下线消息
	 * @param id
	 * @return
	 */
	public int publishEnd(Integer id);
}
