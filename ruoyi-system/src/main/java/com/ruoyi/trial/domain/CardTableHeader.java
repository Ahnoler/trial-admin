package com.ruoyi.trial.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 卡片头模版对象 card_table_header
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public class CardTableHeader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 卡片表头编码 */
    private Long headerCode;

    /** 父级卡片表头编码 */
    @Excel(name = "父级卡片表头编码")
    private Long parentCode;

    /** 卡片表头排序 */
    @Excel(name = "卡片表头排序")
    private Integer headerSort;

    /** 卡片表头标签 */
    @Excel(name = "卡片表头标签")
    private String headerLabel;

    /** 卡片表头键值 */
    @Excel(name = "卡片表头键值")
    private String headerValue;

    /** 卡片类型 */
    @Excel(name = "卡片类型")
    private String cardType;

    /** 样式属性（其他样式扩展） */
    @Excel(name = "样式属性", readConverterExp = "其=他样式扩展")
    private String cssClass;

    /** 表格回显样式 */
    @Excel(name = "表格回显样式")
    private String listClass;

    /** 是否默认（Y是 N否） */
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    private String isDefault;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setHeaderCode(Long headerCode) 
    {
        this.headerCode = headerCode;
    }

    public Long getHeaderCode() 
    {
        return headerCode;
    }
    public void setParentCode(Long parentCode) 
    {
        this.parentCode = parentCode;
    }

    public Long getParentCode() 
    {
        return parentCode;
    }
    public void setHeaderSort(Integer headerSort) 
    {
        this.headerSort = headerSort;
    }

    public Integer getHeaderSort() 
    {
        return headerSort;
    }
    public void setHeaderLabel(String headerLabel) 
    {
        this.headerLabel = headerLabel;
    }

    public String getHeaderLabel() 
    {
        return headerLabel;
    }
    public void setHeaderValue(String headerValue) 
    {
        this.headerValue = headerValue;
    }

    public String getHeaderValue() 
    {
        return headerValue;
    }
    public void setCardType(String cardType) 
    {
        this.cardType = cardType;
    }

    public String getCardType() 
    {
        return cardType;
    }
    public void setCssClass(String cssClass) 
    {
        this.cssClass = cssClass;
    }

    public String getCssClass() 
    {
        return cssClass;
    }
    public void setListClass(String listClass) 
    {
        this.listClass = listClass;
    }

    public String getListClass() 
    {
        return listClass;
    }
    public void setIsDefault(String isDefault) 
    {
        this.isDefault = isDefault;
    }

    public String getIsDefault() 
    {
        return isDefault;
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
            .append("headerCode", getHeaderCode())
            .append("parentCode", getParentCode())
            .append("headerSort", getHeaderSort())
            .append("headerLabel", getHeaderLabel())
            .append("headerValue", getHeaderValue())
            .append("cardType", getCardType())
            .append("cssClass", getCssClass())
            .append("listClass", getListClass())
            .append("isDefault", getIsDefault())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
