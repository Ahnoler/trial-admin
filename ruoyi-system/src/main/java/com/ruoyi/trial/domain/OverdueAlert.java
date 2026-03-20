package com.ruoyi.trial.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OverdueAlert extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long alertId;

    @Excel(name = "预警类型")
    private String alertType;

    @Excel(name = "关联ID")
    private Long targetId;

    @Excel(name = "目标名称")
    private String targetName;

    @Excel(name = "项目ID")
    private Long projectId;

    @Excel(name = "项目名称")
    private String projectName;

    @Excel(name = "预警级别")
    private String alertLevel;

    @Excel(name = "预警状态")
    private String alertStatus;

    @Excel(name = "超期天数")
    private Integer overdueDays;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预期完成日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectedDate;

    @Excel(name = "预警内容")
    private String alertContent;

    @Excel(name = "处理人")
    private String handler;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    @Excel(name = "处理备注")
    private String handleRemark;

    public Long getAlertId()
    {
        return alertId;
    }

    public void setAlertId(Long alertId)
    {
        this.alertId = alertId;
    }

    public String getAlertType()
    {
        return alertType;
    }

    public void setAlertType(String alertType)
    {
        this.alertType = alertType;
    }

    public Long getTargetId()
    {
        return targetId;
    }

    public void setTargetId(Long targetId)
    {
        this.targetId = targetId;
    }

    public String getTargetName()
    {
        return targetName;
    }

    public void setTargetName(String targetName)
    {
        this.targetName = targetName;
    }

    public Long getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String getAlertLevel()
    {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel)
    {
        this.alertLevel = alertLevel;
    }

    public String getAlertStatus()
    {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus)
    {
        this.alertStatus = alertStatus;
    }

    public Integer getOverdueDays()
    {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays)
    {
        this.overdueDays = overdueDays;
    }

    public Date getExpectedDate()
    {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate)
    {
        this.expectedDate = expectedDate;
    }

    public String getAlertContent()
    {
        return alertContent;
    }

    public void setAlertContent(String alertContent)
    {
        this.alertContent = alertContent;
    }

    public String getHandler()
    {
        return handler;
    }

    public void setHandler(String handler)
    {
        this.handler = handler;
    }

    public Date getHandleTime()
    {
        return handleTime;
    }

    public void setHandleTime(Date handleTime)
    {
        this.handleTime = handleTime;
    }

    public String getHandleRemark()
    {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark)
    {
        this.handleRemark = handleRemark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("alertId", getAlertId())
            .append("alertType", getAlertType())
            .append("targetId", getTargetId())
            .append("targetName", getTargetName())
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("alertLevel", getAlertLevel())
            .append("alertStatus", getAlertStatus())
            .append("overdueDays", getOverdueDays())
            .append("expectedDate", getExpectedDate())
            .append("alertContent", getAlertContent())
            .append("handler", getHandler())
            .append("handleTime", getHandleTime())
            .append("handleRemark", getHandleRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
