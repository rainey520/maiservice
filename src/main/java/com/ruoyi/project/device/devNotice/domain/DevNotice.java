package com.ruoyi.project.device.devNotice.domain;

import com.ruoyi.project.system.user.domain.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 公司消息通知表 dev_notice
 *
 * @author zqm
 * @date 2019-04-18
 */
public class DevNotice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 消息主键ID
     */
    private Integer id;
    /**
     * 通知内容
     */
    private String noticeContent;
    /**
     * 创建者id
     */
    private Integer createId;
    /**
     * 创建者的用户对象
     */
    private User createUser;
    /**
     * 消息通知者id
     */
    private Integer receiveBy;
    /**
     * 消息通知者的用户对象
     */
    private User receiveUser;
    /**
     * 消息状态:1,未发布 2,已发布 3,已下线
     */
    private Integer noticeStatus;
    /**
     * 公司主键ID
     */
    private Integer companyId;
    /**
     * 消息创建时间
     */
    private Date createTime;

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public User getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Integer getReceiveBy() {
        return receiveBy;
    }

    public void setReceiveBy(Integer receiveBy) {
        this.receiveBy = receiveBy;
    }

    public Integer getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(Integer noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("noticeContent", getNoticeContent())
                .append("createBy", getCreateId())
                .append("receiveBy", getReceiveBy())
                .append("noticeStatus", getNoticeStatus())
                .append("companyId", getCompanyId())
                .append("createTime", getCreateTime())
                .toString();
    }
}
