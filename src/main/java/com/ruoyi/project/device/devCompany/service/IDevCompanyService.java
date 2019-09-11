package com.ruoyi.project.device.devCompany.service;

import com.ruoyi.project.device.devCompany.domain.DevCompany;

import javax.servlet.http.Cookie;
import java.util.List;

/**
 * 公司 服务层
 * 
 * @author ruoyi
 * @date 2019-01-31
 */
public interface IDevCompanyService 
{
	/**
     * 查询公司信息
     * 
     * @param id 公司ID
     * @return 公司信息
     */
	public DevCompany selectDevCompanyById(Integer id);
	
	/**
     * 查询公司列表
     * 
     * @param devCompany 公司信息
     * @return 公司集合
     */
	public List<DevCompany> selectDevCompanyList(DevCompany devCompany);

	/**
	 * 查询所以的公司信息
	 * @return
	 */
	List<DevCompany> selectDevCompanyAll();
	/**
     * 新增公司
     * 
     * @param devCompany 公司信息
     * @return 结果
     */
	public int insertDevCompany(DevCompany devCompany);
	
	/**
     * 修改公司
     * 
     * @param devCompany 公司信息
     * @return 结果
     */
	public int updateDevCompany(DevCompany devCompany);
		
	/**
     * 删除公司信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteDevCompanyByIds(String ids);

	public DevCompany selectDevCompanyByComCode(String uniqueCode);

	/**
	 * 通过公司名称查询公司信息
	 * @param comName
	 * @return
	 */
	public DevCompany selectDevCompanyByComName(String comName);

	/**
	 * 校验公司名称是否存在
	 * @param comName 公司名称
	 * @return 结果
	 */
    String checkComNameUnique(String comName);

	/**
	 * 修改公司信息
	 * @param company
	 * @return
	 */
	int apiEditCompany(DevCompany company);
}
