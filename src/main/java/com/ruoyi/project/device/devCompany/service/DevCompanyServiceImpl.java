package com.ruoyi.project.device.devCompany.service;

import java.util.List;

import com.ruoyi.common.constant.CompanyConstants;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.service.IDevCompanyService;
import com.ruoyi.common.support.Convert;

/**
 * 公司 服务层实现
 * 
 * @author ruoyi
 * @date 2019-01-31
 */
@Service("devCompany")
public class DevCompanyServiceImpl implements IDevCompanyService 
{
	@Autowired
	private DevCompanyMapper devCompanyMapper;

	/**
     * 查询公司信息
     * 
     * @param id 公司ID
     * @return 公司信息
     */
    @Override
	public DevCompany selectDevCompanyById(Integer id)
	{
	    return devCompanyMapper.selectDevCompanyById(id);
	}
	
	/**
     * 查询公司列表
     * 
     * @param devCompany 公司信息
     * @return 公司集合
     */
	@Override
	public List<DevCompany> selectDevCompanyList(DevCompany devCompany)
	{
	    return devCompanyMapper.selectDevCompanyList(devCompany);
	}

	@Override
	public List<DevCompany> selectDevCompanyAll() {
		return devCompanyMapper.selectDevCompanyAll();
	}

	/**
     * 新增公司
     * 
     * @param devCompany 公司信息
     * @return 结果
     */
	@Override
	public int insertDevCompany(DevCompany devCompany)
	{
	    return devCompanyMapper.insertDevCompany(devCompany);
	}
	
	/**
     * 修改公司
     * 
     * @param devCompany 公司信息
     * @return 结果
     */
	@Override
	public int updateDevCompany(DevCompany devCompany)
	{
	    return devCompanyMapper.updateDevCompany(devCompany);
	}

	/**
     * 删除公司对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteDevCompanyByIds(String ids)
	{
		return devCompanyMapper.deleteDevCompanyByIds(Convert.toStrArray(ids));
	}

	@Override
	public DevCompany selectDevCompanyByComCode(String uniqueCode) {
		return devCompanyMapper.selectDevCompanyByComCode(uniqueCode);
	}

	@Override
	public DevCompany selectDevCompanyByComName(String comName) {
		return devCompanyMapper.selectDevCompanyByComName(comName);
	}

	/**
	 * 校验公司名称是否存在
	 * @param comName 公司名称
	 * @return 结果
	 */
	@Override
	public String checkComNameUnique(String comName) {
		DevCompany devCompany = devCompanyMapper.selectDevCompanyByComName(comName);
		if (!StringUtils.isNull(devCompany)) { // 数据库存在记录
		    return CompanyConstants.COM_NAME_NOT_UNIQUE;
		}
		return CompanyConstants.COM_NAME_UNIQUE;
	}

	/**
	 * 修改公司信息
	 * @param company 公司信息
	 * @return
	 */
	@Override
	public int apiEditCompany(DevCompany company) {
		return devCompanyMapper.updateDevCompany(company);
	}
}
