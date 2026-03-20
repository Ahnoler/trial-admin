package com.ruoyi.trial.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 试制任务程序对象 trial_task_detail_prod
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@Data
public class TrialTaskDetailProd extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 任务编号
     */
    @Excel(name = "任务编号")
    private Long taskId;

    /**
     * 卡片类型
     */
    @Excel(name = "卡片类型")
    private String cardType;

    /**
     * 卡片列头编码
     */
    @Excel(name = "卡片列头编码")
    private Long columnCode;

    /**
     * 序号
     */
    @Excel(name = "序号")
    private Integer serialNo;

    /**
     * 流转程序
     */
    @Excel(name = "流转程序")
    private String program;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 图号
     */
    @Excel(name = "图号")
    private String figure;

    /**
     * 试制数量
     */
    @Excel(name = "试制数量")
    private String trialQuantity;

    /**
     * 送检数量
     */
    @Excel(name = "送检数量")
    private String inspectionQuantity;

    /**
     * 质量状态
     */
    @Excel(name = "质量状态")
    private String manufacturingQualityStatus;

    /**
     * 制造区域
     */
    @Excel(name = "制造区域")
    private String manufacturingArea;

    /**
     * 负责人
     */
    @Excel(name = "负责人")
    private String director;

    /**
     * 电话
     */
    @Excel(name = "电话")
    private String directorTel;

    /**
     * 质量状态
     */
    @Excel(name = "质量状态")
    private String processQualityStatus;

    /**
     * 责任ME
     */
    @Excel(name = "责任ME")
    private String meDirector;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String meDirectorTel;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String notes;

    /**
     * 附件图片
     */
    private String images;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 乐观锁版本号
     */
    private Integer version;

    private String urlType;

    private String shuntQty;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setColumnCode(Long columnCode) {
        this.columnCode = columnCode;
    }

    public Long getColumnCode() {
        return columnCode;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProgram() {
        return program;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getFigure() {
        return figure;
    }

    public void setTrialQuantity(String trialQuantity) {
        this.trialQuantity = trialQuantity;
    }

    public String getTrialQuantity() {
        return trialQuantity;
    }

    public void setInspectionQuantity(String inspectionQuantity) {
        this.inspectionQuantity = inspectionQuantity;
    }

    public String getInspectionQuantity() {
        return inspectionQuantity;
    }

    public void setManufacturingQualityStatus(String manufacturingQualityStatus) {
        this.manufacturingQualityStatus = manufacturingQualityStatus;
    }

    public String getManufacturingQualityStatus() {
        return manufacturingQualityStatus;
    }

    public void setManufacturingArea(String manufacturingArea) {
        this.manufacturingArea = manufacturingArea;
    }

    public String getManufacturingArea() {
        return manufacturingArea;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public void setDirectorTel(String directorTel) {
        this.directorTel = directorTel;
    }

    public String getDirectorTel() {
        return directorTel;
    }

    public void setProcessQualityStatus(String processQualityStatus) {
        this.processQualityStatus = processQualityStatus;
    }

    public String getProcessQualityStatus() {
        return processQualityStatus;
    }

    public void setMeDirector(String meDirector) {
        this.meDirector = meDirector;
    }

    public String getMeDirector() {
        return meDirector;
    }

    public void setMeDirectorTel(String meDirectorTel) {
        this.meDirectorTel = meDirectorTel;
    }

    public String getMeDirectorTel() {
        return meDirectorTel;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskId", getTaskId())
                .append("cardType", getCardType())
                .append("columnCode", getColumnCode())
                .append("serialNo", getSerialNo())
                .append("program", getProgram())
                .append("name", getName())
                .append("figure", getFigure())
                .append("trialQuantity", getTrialQuantity())
                .append("inspectionQuantity", getInspectionQuantity())
                .append("manufacturingQualityStatus", getManufacturingQualityStatus())
                .append("manufacturingArea", getManufacturingArea())
                .append("director", getDirector())
                .append("directorTel", getDirectorTel())
                .append("processQualityStatus", getProcessQualityStatus())
                .append("meDirector", getMeDirector())
                .append("meDirectorTel", getMeDirectorTel())
                .append("notes", getNotes())
                .append("status", getStatus())
                .append("version", getVersion())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
