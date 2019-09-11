package com.ruoyi.project.system.serPort.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 服务器端口配置表 tab_ser_port
 * 
 * @author zqm
 * @date 2019-06-10
 */
public class SerPort extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Integer id;
	/** 服务器id */
	private Integer sid;
	/** 端口信息 */
	private Integer port;
	/** 公司配置id */
	private Integer companyId;
	/** 修改时间,即配置时间 */
	private Date updateTime;
	/**  */
	private Date createTime;

	private String remark;//备注信息

	private String sname;//服务器名称

	private String cname;//公司名称

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setSid(Integer sid) 
	{
		this.sid = sid;
	}

	public Integer getSid() 
	{
		return sid;
	}
	public void setPort(Integer port) 
	{
		this.port = port;
	}

	public Integer getPort() 
	{
		return port;
	}
	public void setCompanyId(Integer companyId) 
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId() 
	{
		return companyId;
	}
	public void setUpdateTime(Date updateTime) 
	{
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() 
	{
		return updateTime;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}

	@Override
	public String getRemark() {
		return remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sid", getSid())
            .append("port", getPort())
            .append("companyId", getCompanyId())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
