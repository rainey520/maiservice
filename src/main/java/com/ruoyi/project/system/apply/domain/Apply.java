package com.ruoyi.project.system.apply.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 资质认证表 tab_apply
 * 
 * @author zqm
 * @date 2019-07-11
 */
public class Apply extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 申请人 */
	private String applyName;
	/** 邮箱 */
	private String applyEmail;
	/** 电话 */
	private String applyPhone;
	/** 公司 */
	private String applyCompany;
	/** 行业 */
	private Integer applyIndustry;
	/** 执照 */
	private String applyLicense;
	/** 备注 */
	private String remark;
	/** 申请唯一识别码 */
	private String uId;
	/** 申请时间 */
	private Date applyTime;
	/** 审核状态 0、待审核 1、未通过 2、已通过 */
	private Integer examineStatus;
	/** 审核人 */
	private Integer examinePeople;
	/** 审核时间 */
	private Date examineTime;
	/** 审核备注 */
	private String examineRemark;
	/** 是否分配服务器 0、否 1、是 */
	private Integer serStatus;
	/** 服务器id */
	private Integer serId;
	/** 服务端口 */
	private Integer portId;
	/** 审核人id 连接用户表 */
	private Integer createId;

	private Integer grade;//申请等级

	/** 用于查询 */
	private String industryName;//行业名称
	private String examinePeopleName;//审核人

	private String serverName;//服务器名称
	private String serverPort;//服务器端口

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplyEmail() {
		return applyEmail;
	}

	public void setApplyEmail(String applyEmail) {
		this.applyEmail = applyEmail;
	}

	public String getApplyPhone() {
		return applyPhone;
	}

	public void setApplyPhone(String applyPhone) {
		this.applyPhone = applyPhone;
	}

	public String getApplyCompany() {
		return applyCompany;
	}

	public void setApplyCompany(String applyCompany) {
		this.applyCompany = applyCompany;
	}

	public Integer getApplyIndustry() {
		return applyIndustry;
	}

	public void setApplyIndustry(Integer applyIndustry) {
		this.applyIndustry = applyIndustry;
	}

	public String getApplyLicense() {
		return applyLicense;
	}

	public void setApplyLicense(String applyLicense) {
		this.applyLicense = applyLicense;
	}

	@Override
	public String getRemark() {
		return remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Integer getExamineStatus() {
		return examineStatus;
	}

	public void setExamineStatus(Integer examineStatus) {
		this.examineStatus = examineStatus;
	}

	public Integer getExaminePeople() {
		return examinePeople;
	}

	public void setExaminePeople(Integer examinePeople) {
		this.examinePeople = examinePeople;
	}

	public Date getExamineTime() {
		return examineTime;
	}

	public void setExamineTime(Date examineTime) {
		this.examineTime = examineTime;
	}

	public String getExamineRemark() {
		return examineRemark;
	}

	public void setExamineRemark(String examineRemark) {
		this.examineRemark = examineRemark;
	}

	public Integer getSerStatus() {
		return serStatus;
	}

	public void setSerStatus(Integer serStatus) {
		this.serStatus = serStatus;
	}

	public Integer getSerId() {
		return serId;
	}

	public void setSerId(Integer serId) {
		this.serId = serId;
	}

	public Integer getPortId() {
		return portId;
	}

	public void setPortId(Integer portId) {
		this.portId = portId;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getExaminePeopleName() {
		return examinePeopleName;
	}

	public void setExaminePeopleName(String examinePeopleName) {
		this.examinePeopleName = examinePeopleName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}


	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("applyName", getApplyName())
            .append("applyEmail", getApplyEmail())
            .append("applyPhone", getApplyPhone())
            .append("applyCompany", getApplyCompany())
            .append("applyIndustry", getApplyIndustry())
            .append("applyLicense", getApplyLicense())
            .append("remark", getRemark())
            .append("uId", getuId())
            .append("applyTime", getApplyTime())
            .append("examineStatus", getExamineStatus())
            .append("examinePeople", getExaminePeople())
            .append("examineTime", getExamineTime())
            .append("examineRemark", getExamineRemark())
            .append("serStatus", getSerStatus())
            .append("serId", getSerId())
            .append("portId", getPortId())
            .append("createId", getCreateId())
            .toString();
    }
}
