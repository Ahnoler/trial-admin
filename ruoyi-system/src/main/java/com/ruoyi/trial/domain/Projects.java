package com.ruoyi.trial.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 项目管理对象 projects
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public class Projects extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目主键 */
    private Long id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目类型 */
    @Excel(name = "项目类型")
    private String projectType;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private Long status;

    /** 项目管理员id */
    @Excel(name = "项目管理员id")
    private String pm;

    /** 项目管理员 */
    @Excel(name = "项目管理员")
    private String pmName;

    /** 总成名称 */
    @Excel(name = "总成名称")
    private String assemblyName;

    /** 总成图号 */
    @Excel(name = "总成图号")
    private String assemblyDrawingNumber;

    /** 项目描述 */
    @Excel(name = "项目描述")
    private String projectDescription;

    /** 项目开始时间 */
    @Excel(name = "项目开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 项目结束时间 */
    @Excel(name = "项目结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 项目状态 */
    @Excel(name = "项目状态")
    private String projectStatus;

    /** 项目预算 */
    @Excel(name = "项目预算")
    private Double budget;

    /** 项目实际成本 */
    @Excel(name = "项目实际成本")
    private Double actualCost;

    /** 项目优先级 */
    @Excel(name = "项目优先级")
    private String priority;

    /** 项目负责人 */
    @Excel(name = "项目负责人")
    private String projectLeader;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String contactInfo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }
    public void setProjectType(String projectType) 
    {
        this.projectType = projectType;
    }

    public String getProjectType() 
    {
        return projectType;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setPm(String pm) 
    {
        this.pm = pm;
    }

    public String getPm() 
    {
        return pm;
    }
    public void setPmName(String pmName) 
    {
        this.pmName = pmName;
    }

    public String getPmName() 
    {
        return pmName;
    }
    public void setAssemblyName(String assemblyName) 
    {
        this.assemblyName = assemblyName;
    }

    public String getAssemblyName() 
    {
        return assemblyName;
    }
    public void setAssemblyDrawingNumber(String assemblyDrawingNumber) 
    {
        this.assemblyDrawingNumber = assemblyDrawingNumber;
    }

    public String getAssemblyDrawingNumber() 
    {
        return assemblyDrawingNumber;
    }
    public void setProjectDescription(String projectDescription) 
    {
        this.projectDescription = projectDescription;
    }

    public String getProjectDescription() 
    {
        return projectDescription;
    }
    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }
    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }
    public void setProjectStatus(String projectStatus) 
    {
        this.projectStatus = projectStatus;
    }

    public String getProjectStatus() 
    {
        return projectStatus;
    }
    public void setBudget(Double budget) 
    {
        this.budget = budget;
    }

    public Double getBudget() 
    {
        return budget;
    }
    public void setActualCost(Double actualCost) 
    {
        this.actualCost = actualCost;
    }

    public Double getActualCost() 
    {
        return actualCost;
    }
    public void setPriority(String priority) 
    {
        this.priority = priority;
    }

    public String getPriority() 
    {
        return priority;
    }
    public void setProjectLeader(String projectLeader) 
    {
        this.projectLeader = projectLeader;
    }

    public String getProjectLeader() 
    {
        return projectLeader;
    }
    public void setContactInfo(String contactInfo) 
    {
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() 
    {
        return contactInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectName", getProjectName())
            .append("projectType", getProjectType())
            .append("status", getStatus())
            .append("pm", getPm())
            .append("pmName", getPmName())
            .append("assemblyName", getAssemblyName())
            .append("assemblyDrawingNumber", getAssemblyDrawingNumber())
            .append("projectDescription", getProjectDescription())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("projectStatus", getProjectStatus())
            .append("budget", getBudget())
            .append("actualCost", getActualCost())
            .append("priority", getPriority())
            .append("projectLeader", getProjectLeader())
            .append("contactInfo", getContactInfo())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
