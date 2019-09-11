package com.ruoyi.project.device.devDeviceCounts.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 数据上报日志表 dev_data_log
 * 
 * @author zqm
 * @date 2019-04-12
 */
public class DevDataLog extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/**  */
	private Integer companyId;
	/**  */
	private Integer lineId;
	/**  */
	private Integer workId;
	/**  */
	private Integer devId;
	/**  */
	private Integer ioId;
	/**  */
	private Integer ioOrder;
	/**  */
	private Integer dataTotal;
	/**  */
	private Date createDate;
	/**  */
	private Date createTime;

	private Integer delData;//每次上传前后数据差

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setCompanyId(Integer companyId) 
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId() 
	{
		return companyId;
	}
	public void setLineId(Integer lineId) 
	{
		this.lineId = lineId;
	}

	public Integer getLineId() 
	{
		return lineId;
	}
	public void setWorkId(Integer workId) 
	{
		this.workId = workId;
	}

	public Integer getWorkId() 
	{
		return workId;
	}
	public void setDevId(Integer devId) 
	{
		this.devId = devId;
	}

	public Integer getDevId() 
	{
		return devId;
	}
	public void setIoId(Integer ioId) 
	{
		this.ioId = ioId;
	}

	public Integer getIoId() 
	{
		return ioId;
	}
	public void setIoOrder(Integer ioOrder) 
	{
		this.ioOrder = ioOrder;
	}

	public Integer getIoOrder() 
	{
		return ioOrder;
	}
	public void setDataTotal(Integer dataTotal) 
	{
		this.dataTotal = dataTotal;
	}

	public Integer getDataTotal() 
	{
		return dataTotal;
	}
	public void setCreateDate(Date createDate) 
	{
		this.createDate = createDate;
	}

	public Date getCreateDate() 
	{
		return createDate;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}

	public Integer getDelData() {
		return delData;
	}

	public void setDelData(Integer delData) {
		this.delData = delData;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("lineId", getLineId())
            .append("workId", getWorkId())
            .append("devId", getDevId())
            .append("ioId", getIoId())
            .append("ioOrder", getIoOrder())
            .append("dataTotal", getDataTotal())
            .append("createDate", getCreateDate())
            .append("createTime", getCreateTime())
            .toString();
    }
}
