package com.ruoyi.project.device.devList.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.project.device.devIo.domain.DevIo;
import com.ruoyi.project.device.devModel.domain.DevModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * 硬件表 dev_list
 * 
 * @author ruoyi
 * @date 2019-04-09
 */
public class DevList extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 编号  */
	@Excel(name = "编号")
	private String deviceId;

	/** 型号*/
	@Excel(name = "型号")
	private String devModel;
	/**  */
	private Integer deviceStatus;

	private Integer configStatus;//硬件编码与硬件进行配置状态 0、未配置 1、已配置
	/** 名称  */
	@Excel(name = "硬件名称")
	private String deviceName;
	/**  */
	private Integer devModelId;
	/**  */
	private Integer deviceUploadTime;
	/**  */
	private Integer companyId;
	@Excel(name = "所属公司")
	private String comName;
	/** 备注信息 */
	@Excel(name = "备注信息")
	private String remark;
	/**  */
	private Integer defId;
	/** 配置时间 */
	@Excel(name = "配置时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
	private Date configDate;
	/** 创建时间 */
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
	private Date createDate;

	private Integer scan;

	private List<DevIo> devIos;


	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setDeviceId(String deviceId) 
	{
		this.deviceId = deviceId;
	}

	public String getDeviceId() 
	{
		return deviceId;
	}
	public void setDeviceStatus(Integer deviceStatus) 
	{
		this.deviceStatus = deviceStatus;
	}

	public Integer getDeviceStatus() 
	{
		return deviceStatus;
	}
	public void setDeviceName(String deviceName) 
	{
		this.deviceName = deviceName;
	}

	public String getDeviceName() 
	{
		return deviceName;
	}
	public void setDevModelId(Integer devModelId) 
	{
		this.devModelId = devModelId;
	}

	public Integer getDevModelId() 
	{
		return devModelId;
	}
	public void setDeviceUploadTime(Integer deviceUploadTime) 
	{
		this.deviceUploadTime = deviceUploadTime;
	}

	public Integer getDeviceUploadTime() 
	{
		return deviceUploadTime;
	}
	public void setCompanyId(Integer companyId) 
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId() 
	{
		return companyId;
	}
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}

	public String getRemark() 
	{
		return remark;
	}
	public void setDefId(Integer defId) 
	{
		this.defId = defId;
	}

	public Integer getDefId() 
	{
		return defId;
	}
	public void setConfigDate(Date configDate) 
	{
		this.configDate = configDate;
	}

	public Date getConfigDate() 
	{
		return configDate;
	}
	public void setCreateDate(Date createDate) 
	{
		this.createDate = createDate;
	}

	public Date getCreateDate() 
	{
		return createDate;
	}

	public String getDevModel() {
		return devModel;
	}

	public void setDevModel(String devModel) {
		this.devModel = devModel;
	}

	public List<DevIo> getDevIos() {
		return devIos;
	}

	public void setDevIos(List<DevIo> devIos) {
		this.devIos = devIos;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public Integer getConfigStatus() {
		return configStatus;
	}

	public void setConfigStatus(Integer configStatus) {
		this.configStatus = configStatus;
	}

	public Integer getScan() {
		return scan;
	}

	public void setScan(Integer scan) {
		this.scan = scan;
	}

	@Override
	public String toString() {
		return "DevList{" +
				"id=" + id +
				", deviceId='" + deviceId + '\'' +
				", devModel='" + devModel + '\'' +
				", deviceStatus=" + deviceStatus +
				", configStatus=" + configStatus +
				", deviceName='" + deviceName + '\'' +
				", devModelId=" + devModelId +
				", deviceUploadTime=" + deviceUploadTime +
				", companyId=" + companyId +
				", comName='" + comName + '\'' +
				", remark='" + remark + '\'' +
				", defId=" + defId +
				", configDate=" + configDate +
				", createDate=" + createDate +
				", devIos=" + devIos +
				'}';
	}
}
