package com.ruoyi.trial.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 卡片信息模版对象 card_model
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public class CardModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 卡片编码 */
    private Long cardCode;

    /** 卡片排序 */
    @Excel(name = "卡片排序")
    private Integer cardSort;

    /** 卡片标签 */
    @Excel(name = "卡片标签")
    private String cardLabel;

    /** 卡片键值 */
    @Excel(name = "卡片键值")
    private String cardValue;

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

    public void setCardCode(Long cardCode) 
    {
        this.cardCode = cardCode;
    }

    public Long getCardCode() 
    {
        return cardCode;
    }
    public void setCardSort(Integer cardSort) 
    {
        this.cardSort = cardSort;
    }

    public Integer getCardSort() 
    {
        return cardSort;
    }
    public void setCardLabel(String cardLabel) 
    {
        this.cardLabel = cardLabel;
    }

    public String getCardLabel() 
    {
        return cardLabel;
    }
    public void setCardValue(String cardValue) 
    {
        this.cardValue = cardValue;
    }

    public String getCardValue() 
    {
        return cardValue;
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
            .append("cardCode", getCardCode())
            .append("cardSort", getCardSort())
            .append("cardLabel", getCardLabel())
            .append("cardValue", getCardValue())
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
