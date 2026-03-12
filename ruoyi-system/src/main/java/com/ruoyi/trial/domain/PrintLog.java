package com.ruoyi.trial.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 *  打印记录对象 print_log
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public class PrintLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 打印记录 */
    private Long id;

    /**  打印单据编号 */
    @Excel(name = " 打印单据编号")
    private Long taskId;

    /**  打印内容（盘点单不保存） */
    @Excel(name = " 打印内容", readConverterExp = "盘=点单不保存")
    private String content;

    /**  打印卡片类型 */
    @Excel(name = " 打印卡片类型")
    private Long cardtype;

    /** 机构编号 */
    @Excel(name = "机构编号")
    private Long orgnno;

    /** 打印类型 */
    @Excel(name = "打印类型")
    private Long printtype;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setCardtype(Long cardtype) 
    {
        this.cardtype = cardtype;
    }

    public Long getCardtype() 
    {
        return cardtype;
    }
    public void setOrgnno(Long orgnno) 
    {
        this.orgnno = orgnno;
    }

    public Long getOrgnno() 
    {
        return orgnno;
    }
    public void setPrinttype(Long printtype) 
    {
        this.printtype = printtype;
    }

    public Long getPrinttype() 
    {
        return printtype;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("content", getContent())
            .append("cardtype", getCardtype())
            .append("orgnno", getOrgnno())
            .append("printtype", getPrinttype())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
