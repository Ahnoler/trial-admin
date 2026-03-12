package com.ruoyi.trial.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 试制任务信息对象 trial_task_prod
 *
 * @author ruoyi
 * @date 2023-07-17
 */
@Data
public class TrialTaskProd extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 任务主键
     */
    private Long taskId;

    /**
     * 项目编号
     */
    @Excel(name = "项目编号")
    private Long projectId;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    private String projectName;

    /**
     * 卡片类型
     */
    @Excel(name = "卡片类型")
    private String cardType;

    /**
     * 卡片名称
     */
    @Excel(name = "卡片名称")
    private String title;

    /**
     * 车型
     */
    @Excel(name = "车型")
    private String carType;

    /**
     * 总成名称
     */
    @Excel(name = "总成名称")
    private String assemblyName;

    /**
     * 总成图号
     */
    @Excel(name = "总成图号")
    private String assemblyFigure;

    /**
     * 试制管理员/电话
     */
    @Excel(name = "试制管理员/电话")
    private String pm;

    /**
     * PE姓名/电话
     */
    @Excel(name = "PE姓名/电话")
    private String pe;

    /**
     * 当前程序
     */
    @Excel(name = "当前程序")
    private Integer currentSerialNo;

    /**
     * 当前程序名称
     */
    @Excel(name = "当前程序名称")
    private String currentSerialName;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
    
    /**
     * 关联任务ID（用于分流卡片）
     */
    private Long relatedTaskId;
    
    //    卡片详情
    private String listStr;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskId", getTaskId())
                .append("projectId", getProjectId())
                .append("projectName", getProjectName())
                .append("cardType", getCardType())
                .append("title", getTitle())
                .append("carType", getCarType())
                .append("assemblyName", getAssemblyName())
                .append("assemblyFigure", getAssemblyFigure())
                .append("pm", getPm())
                .append("pe", getPe())
                .append("currentSerialNo", getCurrentSerialNo())
                .append("currentSerialName", getCurrentSerialName())
                .append("status", getStatus())
                .append("relatedTaskId", getRelatedTaskId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
