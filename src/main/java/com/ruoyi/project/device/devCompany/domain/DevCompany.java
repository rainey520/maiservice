package com.ruoyi.project.device.devCompany.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 公司表 dev_company
 *
 * @author ruoyi
 * @date 2019-04-08
 */
public class DevCompany extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/** 公司主键ID */
	@Excel(name = "公司编号")
	private Integer companyId;
	/** 公司名称 */
	@Excel(name = "公司明称")
	private String comName;
	/** 公司地址 */
	@Excel(name = "公司地址")
	private String comAddress;
	/** 创建时间 */
	//@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
	//private Date createTime;
	/** 公司类型 */
	private Integer comType;
	/** 公司logo */
	@Excel(name = "公司logo")
	private String comLogo;
	/** 公司轮播图片 */
	private String comPicture;
	/**
	 * 公司所属行业
	 */
	private Integer industry;
	/** 公司所属行业名称 */
	private String industryName;

	private String totalIso;//iso 总文件夹名称

	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

	public void setCompanyId(Integer companyId)
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}
	public void setComName(String comName)
	{
		this.comName = comName;
	}

	public String getComName()
	{
		return comName;
	}
	//public void setCreateTime(Date createTime)
	//{
	//	this.createTime = createTime;
	//}
    //
	//public Date getCreateTime()
	//{
	//	return createTime;
	//}
	public void setComAddress(String comAddress)
	{
		this.comAddress = comAddress;
	}

	public String getComAddress()
	{
		return comAddress;
	}

	public Integer getComType() {
		return comType;
	}

	public void setComType(Integer comType) {
		this.comType = comType;
	}

	public void setComLogo(String comLogo)
	{
		this.comLogo = comLogo;
	}

	public String getComLogo()
	{
		return comLogo;
	}
	public void setComPicture(String comPicture)
	{
		this.comPicture = comPicture;
	}

	public String getComPicture()
	{
		return comPicture;
	}

	public String getTotalIso() {
		return totalIso;
	}

	public void setTotalIso(String totalIso) {
		this.totalIso = totalIso;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("companyId", getCompanyId())
				.append("comName", getComName())
				.append("createTime", getCreateTime())
				.append("comAddress", getComAddress())
				.append("comType", getComType())
				.append("comLogo", getComLogo())
				.append("comPicture", getComPicture())
				.toString();
	}
}
