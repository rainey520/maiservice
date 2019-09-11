package com.ruoyi.project.system.industry.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.industry.mapper.IndustryMapper;
import com.ruoyi.project.system.industry.domain.Industry;
import com.ruoyi.common.support.Convert;

/**
 * 行业管理 服务层实现
 * 
 * @author zqm
 * @date 2019-07-10
 */
@Service("industry")
public class IndustryServiceImpl implements IIndustryService 
{
	@Autowired
	private IndustryMapper industryMapper;

	/**
     * 查询行业管理信息
     * 
     * @param id 行业管理ID
     * @return 行业管理信息
     */
    @Override
	public Industry selectIndustryById(Integer id)
	{
	    return industryMapper.selectIndustryById(id);
	}
	
	/**
     * 查询行业管理列表
     * 
     * @param industry 行业管理信息
     * @return 行业管理集合
     */
	@Override
	public List<Industry> selectIndustryList(Industry industry)
	{
	    return industryMapper.selectIndustryList(industry);
	}
	
    /**
     * 新增行业管理
     * 
     * @param industry 行业管理信息
     * @return 结果
     */
	@Override
	public int insertIndustry(Industry industry)
	{
		industry.setcTime(new Date());
	    return industryMapper.insertIndustry(industry);
	}
	
	/**
     * 修改行业管理
     * 
     * @param industry 行业管理信息
     * @return 结果
     */
	@Override
	public int updateIndustry(Industry industry)
	{
	    return industryMapper.updateIndustry(industry);
	}

	/**
     * 删除行业管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteIndustryByIds(String ids)
	{
		return industryMapper.deleteIndustryByIds(Convert.toStrArray(ids));
	}

    /**
     * 查询所以可用行业
     * @return
     */
    @Override
    public List<Industry> selectIsAll() {
        return industryMapper.selectIsAll();
    }
}
