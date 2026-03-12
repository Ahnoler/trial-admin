package com.ruoyi.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 计划任务操作日志(ActionLog)实体类
 *
 * @author makejava
 * @since 2023-07-26 20:03:35
 */
@Data
public class ActionLog implements Serializable {
    private static final long serialVersionUID = -79884792071288202L;
    /**
    * 主键
    */
    private Integer id;
    /**
    * 任务编号
    */
    private Long taskId;
    /**
    * 操作类型 1、变更 2、分流
    */
    private Long actionType;
    /**
    * 创建人员
    */
    private String createBy;
    /**
    * 创建时间
    */
    private String createTime;
    
    private String otherInfo01;
    
    private String otherInfo02;
    
    private String otherInfo03;
    
    private String otherInfo04;
    
    private String otherInfo05;
    
    private String otherInfo06;
    
    private String otherInfo07;

    private String userName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getActionType() {
        return actionType;
    }

    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOtherInfo01() {
        return otherInfo01;
    }

    public void setOtherInfo01(String otherInfo01) {
        this.otherInfo01 = otherInfo01;
    }

    public String getOtherInfo02() {
        return otherInfo02;
    }

    public void setOtherInfo02(String otherInfo02) {
        this.otherInfo02 = otherInfo02;
    }

    public String getOtherInfo03() {
        return otherInfo03;
    }

    public void setOtherInfo03(String otherInfo03) {
        this.otherInfo03 = otherInfo03;
    }

    public String getOtherInfo04() {
        return otherInfo04;
    }

    public void setOtherInfo04(String otherInfo04) {
        this.otherInfo04 = otherInfo04;
    }

    public String getOtherInfo05() {
        return otherInfo05;
    }

    public void setOtherInfo05(String otherInfo05) {
        this.otherInfo05 = otherInfo05;
    }

    public String getOtherInfo06() {
        return otherInfo06;
    }

    public void setOtherInfo06(String otherInfo06) {
        this.otherInfo06 = otherInfo06;
    }

    public String getOtherInfo07() {
        return otherInfo07;
    }

    public void setOtherInfo07(String otherInfo07) {
        this.otherInfo07 = otherInfo07;
    }

}