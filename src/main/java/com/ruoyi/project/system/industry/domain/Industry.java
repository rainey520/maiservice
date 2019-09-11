package com.ruoyi.project.system.industry.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 行业管理表 tab_industry
 * 
 * @author zqm
 * @date 2019-07-10
 */
public class Industry extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 行业名称 */
	private String name;
	/** 备注信息 */
	private String remork;
	/** 创建时间 */
	private Date cTime;
	/** 状态 0、可用 1、禁用 */
	private Integer inStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemork() {
		return remork;
	}

	public void setRemork(String remork) {
		this.remork = remork;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public Integer getInStatus() {
		return inStatus;
	}

	public void setInStatus(Integer inStatus) {
		this.inStatus = inStatus;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("remork", getRemork())
            .append("cTime", getcTime())
            .append("inStatus", getInStatus())
            .toString();
    }
}
