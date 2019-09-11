package com.ruoyi.project.system.apply.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.ruoyi.common.feign.initdata.api.InitDataFeignApi;
import com.ruoyi.common.feign.initdata.domain.InitData;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.device.devCompany.api.CompanyFeignApi;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.system.ser.domain.Ser;
import com.ruoyi.project.system.ser.mapper.SerMapper;
import com.ruoyi.project.system.serPort.domain.SerPort;
import com.ruoyi.project.system.serPort.mapper.SerPortMapper;
import com.ruoyi.project.system.user.api.UserFeignApi;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.apply.mapper.ApplyMapper;
import com.ruoyi.project.system.apply.domain.Apply;
import com.ruoyi.project.system.apply.service.IApplyService;
import com.ruoyi.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资质认证 服务层实现
 * 
 * @author zqm
 * @date 2019-07-11
 */
@Service
public class ApplyServiceImpl implements IApplyService 
{
	@Autowired
	private ApplyMapper applyMapper;

	@Autowired
	private DevCompanyMapper devCompanyMapper;

	@Autowired
	private SerMapper serMapper;

	@Autowired
	private SerPortMapper serPortMapper;

	@Autowired
	private UserMapper userMapper;

	/**
     * 查询资质认证信息
     * 
     * @param id 资质认证ID
     * @return 资质认证信息
     */
    @Override
	public Apply selectApplyById(Integer id)
	{
	    return applyMapper.selectApplyById(id);
	}

	/**
	 * 根据用户查询当前用户最新的申请信息
	 * @param createId 用户id
	 * @return
	 */
	@Override
	public Apply selectApplyByCreateId(int createId) {
		return applyMapper.selectApplyByCreateId(createId);
	}

	/**
     * 查询资质认证列表
     * 
     * @param apply 资质认证信息
     * @return 资质认证集合
     */
	@Override
	public List<Apply> selectApplyList(Apply apply)
	{
	    return applyMapper.selectApplyList(apply);
	}
	
    /**
     * 新增资质认证
     * 
     * @param apply 资质认证信息
     * @return 结果
     */
	@Override
	public int insertApply(Apply apply)
	{
		User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
		apply.setuId(UUID.randomUUID().toString().replaceAll("-",""));
		apply.setCreateId(u.getUserId().intValue());
		apply.setApplyTime(new Date());
	    return applyMapper.insertApply(apply);
	}
	
	/**
     * 修改资质认证
     * 
     * @param apply 资质认证信息
     * @return 结果
     */
	@Override
	public int updateApply(Apply apply)
	{
		User u = JwtUtil.getTokenUser(ServletUtils.getRequest());
		apply.setExaminePeople(u.getUserId().intValue());
		apply.setExamineTime(new Date());
	    return applyMapper.updateApply(apply);
	}

	/**
	 * 保存分发服务器
	 * @param apply
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int configService(Apply apply)throws Exception {
		//查询对应信息存在
		Apply a = applyMapper.selectApplyById(apply.getId());
		if(a == null){
			throw  new Exception("操作异常");
		}
		//查询对应公司是否存在
		DevCompany company = devCompanyMapper.selectDevCompanyByComName(a.getApplyCompany());
		if(company != null){
			throw new Exception("对应公司已经存在");
		}
		//查询对应服务器是否存在
		Ser ser = serMapper.selectSerById(apply.getSerId());
		if(ser == null){
			throw new Exception("对应服务器不存在");
		}
		//查询对应端口是否存在
		SerPort serPort = serPortMapper.selectSerPortById(apply.getPortId());
		if(serPort == null){
			throw new Exception("对应服务端口不存在");
		}
		//查询对应用户是否存在
		User u = userMapper.selectUserById(a.getCreateId().longValue());
		if(u == null){
			throw  new Exception("对应申请用户不存在");
		}
		company = new DevCompany();
		company.setComName(a.getApplyCompany());
		company.setTotalIso("iso"+ser.getId()+"_"+serPort.getPort());
		company.setIndustry(a.getApplyIndustry());
		company.setCreateTime(new Date());
		//新增公司信息
		devCompanyMapper.insertDevCompany(company);
		//修改用户公司归属
		u.setUserName(a.getApplyName());
		u.setTag("0");
		u.setCompanyId(company.getCompanyId());
		userMapper.updateUser(u);
		//从服务器初始化用户
		company.setCreateTime(null);
		u.setCreateTime(null);
		u.setLoginDate(null);
		u.setDevCompany(null);
		Long[] roleIds = {2L};
		u.setRoleIds(roleIds);
		String url = ser.getSpath() + ":" + serPort.getPort();
		System.out.println("++++++++++++++++++++++++++++++++++++++");
		System.out.println(url);
		InitData initData = new InitData();
		initData.setUser(u);
		initData.setCompany(company);
		try {
			InitDataFeignApi feignApi = Feign.builder()
					.encoder(new GsonEncoder())
					.decoder(new GsonDecoder())
					.target(InitDataFeignApi.class,url);
			HashMap<String, Object> result = feignApi.initData(initData);
			if (Double.valueOf(result.get("code").toString()) != 0) {
				throw new Exception("初始化服务器数据异常");
			}
		}catch (Exception e){
			throw new Exception("初始化服务器数据异常");
		}
		//服务器用户加一
		ser.setSuserNum(ser.getSuserNum()+1);
		serMapper.updateSer(ser);
		//修改端口配置
		serPort.setCompanyId(company.getCompanyId());
		serPort.setUpdateTime(new Date());
		serPort.setRemark(apply.getRemark());
		serPortMapper.updateSerPort(serPort);
		//修改对应端口配置信息
		a.setSerId(apply.getSerId());
		a.setPortId(apply.getPortId());
		a.setSerStatus(1);
		applyMapper.updateApply(a);
		return 1;
	}
}
