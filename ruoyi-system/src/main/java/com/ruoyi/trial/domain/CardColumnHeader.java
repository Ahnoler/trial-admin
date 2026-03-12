package com.ruoyi.trial.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 卡片列头模版对象 card_column_header
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public class CardColumnHeader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 卡片列头编码 */
    private Long columnCode;

    /** 父级卡片列头编码 */
    @Excel(name = "父级卡片列头编码")
    private Long parentCode;

    /** 卡片列头排序 */
    @Excel(name = "卡片列头排序")
    private Integer columnSort;

    /** 卡片列头标签 */
    @Excel(name = "卡片列头标签")
    private String columnLabel;

    /** 卡片列头键值 */
    @Excel(name = "卡片列头键值")
    private String columnValue;

    /** 卡片行数最小数量 */
    @Excel(name = "卡片行数最小数量")
    private Integer columnMin;

    /** 卡片行数最大数量 */
    @Excel(name = "卡片行数最大数量")
    private Integer columnMax;

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

    public void setColumnCode(Long columnCode) 
    {
        this.columnCode = columnCode;
    }

    public Long getColumnCode() 
    {
        return columnCode;
    }
    public void setParentCode(Long parentCode) 
    {
        this.parentCode = parentCode;
    }

    public Long getParentCode() 
    {
        return parentCode;
    }
    public void setColumnSort(Integer columnSort) 
    {
        this.columnSort = columnSort;
    }

    public Integer getColumnSort() 
    {
        return columnSort;
    }
    public void setColumnLabel(String columnLabel) 
    {
        this.columnLabel = columnLabel;
    }

    public String getColumnLabel() 
    {
        return columnLabel;
    }
    public void setColumnValue(String columnValue) 
    {
        this.columnValue = columnValue;
    }

    public String getColumnValue() 
    {
        return columnValue;
    }
    public void setColumnMin(Integer columnMin) 
    {
        this.columnMin = columnMin;
    }

    public Integer getColumnMin() 
    {
        return columnMin;
    }
    public void setColumnMax(Integer columnMax) 
    {
        this.columnMax = columnMax;
    }

    public Integer getColumnMax() 
    {
        return columnMax;
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
            .append("columnCode", getColumnCode())
            .append("parentCode", getParentCode())
            .append("columnSort", getColumnSort())
            .append("columnLabel", getColumnLabel())
            .append("columnValue", getColumnValue())
            .append("columnMin", getColumnMin())
            .append("columnMax", getColumnMax())
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
