package com.ruoyi.project.device.devList.service;

import com.ruoyi.common.constant.DevConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.feign.FeignUtils;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.spring.DevId;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devIo.domain.DevIo;
import com.ruoyi.project.device.devIo.mapper.DevIoMapper;
import com.ruoyi.project.device.devList.api.DevListApi;
import com.ruoyi.project.device.devList.api.DevListFeignApi;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.domain.DevListResult;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.device.devModel.domain.DevModel;
import com.ruoyi.project.device.devModel.mapper.DevModelMapper;
import com.ruoyi.project.system.user.domain.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 硬件 服务层实现
 * 
 * @author ruoyi
 * @date 2019-04-08
 */
@Service("devList")
public class DevListServiceImpl implements IDevListService 
{
	@Autowired
	private DevListMapper devListMapper;

	@Autowired
	private DevModelMapper devModelMapper;

	@Autowired
	private DevIoMapper devIoMapper;

	/**
     * 查询硬件信息
     * 
     * @param id 硬件ID
     * @return 硬件信息
     */
    @Override
	public DevList selectDevListById(Integer id)
	{
	    return devListMapper.selectDevListById(id);
	}
	
	/**
     * 查询硬件列表
     * 
     * @param devList 硬件信息
     * @return 硬件集合
     */
	@Override
	public List<DevList> selectDevListList(DevList devList, HttpServletRequest request)
	{
		User user = JwtUtil.getTokenUser(request);
		if(user == null){
			return Collections.emptyList();
		}
		if(!User.isSys(user)){ devList.setCompanyId(user.getCompanyId()); }
		return devListMapper.selectDevListList(devList);
	}
	
    /**
     * 新增硬件
     * 
     * @param
     * @return 结果
     */
	@Override
	public int insertDevList(int devModelId)
	{

		DevModel devModel = devModelMapper.selectDevModelById(devModelId);
		if(devModel == null)return  0;
		DevList devList =null;
		DevIo devIo = null;
		int i =1;
		while (i<=30){
			String devId = DevId.getPageCode();
			if(StringUtils.isEmpty(devId)){
				continue;
			}
			devList = new DevList();
			devList.setDeviceId(devModel.getModelName()+devId);
			devList.setDeviceStatus(1);
			devList.setDefId(0);
			devList.setDeviceUploadTime(15);
			devList.setDevModelId(devModelId);
			devList.setCreateDate(new Date());
			DevList dev = devListMapper.selectDevListByDevId(devList.getDeviceId());
			if(dev == null){
				devListMapper.insertDevList(devList);
//				for(int j =1;j <=4;j++){
//					devIo = new DevIo();
//					devIo.setDevId(devList.getId());
//					devIo.setIoOrder(j);
//					devIo.setIsSign(0);
//					devIo.setLineId(0);
//					devIo.setRemark("上传数据");
//					devIo.setCreateTime(new Date());
//					devIoMapper.insertDevIo(devIo);
//				}
				i++;
			}
		}
	    return 1;
	}
	
	/**
     * 修改硬件
     * 
     * @param devList 硬件信息
     * @return 结果
     */
	@Override
	public int updateDevList(DevList devList,HttpServletRequest request)
	{
		DevListFeignApi devListApi = Feign.builder()
				.decoder(new GsonDecoder())
				.encoder(new GsonEncoder())
				.target(DevListFeignApi.class, FeignUtils.MAIN_PATH);
		DevListResult devListResult = new DevListResult();
		devListResult.setId(devList.getId() != null ? devList.getId() : null);
		devListResult.setRemark(devList.getRemark() != null ? devList.getRemark() : null);
		devListResult.setDeviceName(devList.getDeviceName() != null ? devList.getDeviceName() : null);
		devListResult.setDeviceStatus(devList.getDeviceStatus() != null ? devList.getDeviceStatus() : null);
		HashMap<String, Object> result = devListApi.editDevList(devListResult, JwtUtil.getToken(request));
		if (Double.valueOf(result.get("code").toString()) == 0) {
			return devListMapper.updateDevList(devList);
		}
	    return 0;
	}

	/**
	 * 用户添加硬件信息
	 * @param devList 硬件信息
	 * @return
	 */
	@Override
	public int addSave(DevList devList) {
		return devListMapper.addSave(devList);
	}

	/**
     * 删除硬件对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteDevListByIds(String ids)
	{

		return devListMapper.deleteDevListByIds(Convert.toStrArray(ids));
	}

	/**
	 * 查询对应的硬件信息和对应的IO数据
	 * @param id
	 * @return
	 */
	@Override
	public DevList selectDevListAndIoById(Integer id) {
		return devListMapper.selectDevListAndIoById(id);
	}

	/**
	 * 获取前20个未配置的硬件编号
	 * @return
	 */
	@Override
	public List<String> selectNoConfigDevice() {
		return devListMapper.selectNoConfigDevice();
	}

	@Override
	public List<DevList> selectAll(Cookie[] cookies) {
		User user = JwtUtil.getTokenCookie(cookies);
		if(user == null){
			return Collections.emptyList();
		}
		return devListMapper.selectAll(user.getCompanyId());
	}

	/**
	 * 根据硬件编号验证对应的硬件是否存在
	 * @param code 硬件编号
	 * @return
	 */
	@Override
	public int deviceValidate(String code) {
		DevList devList = devListMapper.selectDevListByCode(code);
		if(devList == null){
			return DevConstants.DEV_NOT_EXSIT;
		}else if(devList.getCompanyId() != null && devList.getCompanyId() >0){
			return DevConstants.DEV_BEING_USE;
		}
		return DevConstants.DEV_VALIDATE_TRUE;
	}

	/**
	 * api验证硬件编号是否存在
	 * @param code 硬件编号
	 * @return 结果
	 */
	@Override
	public int apiDeviceValidate(String code) {
		String replace = code.replace("\"", "").replace("\"", "");
		DevList devList = devListMapper.selectDevListByCode(replace);
		if(devList == null){
			throw new BusinessException("硬件不存在");
		}else if(devList.getCompanyId() != null && devList.getCompanyId() >0){
			throw new BusinessException("硬件正在使用");
		}
		return DevConstants.DEV_VALIDATE_TRUE;
	}

	/**
	 * api接口添加硬件信息
	 * @param devList 硬件信息
	 * @return 结果
	 */
	@Override
	public int apiAddSave(DevList devList) {
		devListMapper.addSave(devList);
		Integer devId = devListMapper.selectDevListByCode(devList.getDeviceId()).getId();
		return devId;
	}

	/**
	 * api接口修改硬件
	 * @param devList 硬件信息
	 * @return 结果
	 */
	@Override
	public int apiEdit(DevList devList) {
		return devListMapper.updateDevList(devList);
	}

	/**
	 * api接口删除硬件
	 * @param ids 硬件id
	 * @return 结果
	 */
	@Override
	public int apiRemove(String ids) {
		String replace = ids.replace("\"", "").replace("\"", "");
		return devListMapper.deleteDevListByIds(Convert.toStrArray(replace));
	}

	/**
	 * 硬件扫描
	 * @param id 硬件id
	 * @return
	 */
	@Override
	public int devScan(int id) {
		return devListMapper.devScan(id);
	}
}
