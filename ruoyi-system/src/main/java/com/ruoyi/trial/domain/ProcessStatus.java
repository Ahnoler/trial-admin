package com.ruoyi.trial.domain;

/**
 * 流转程序状态枚举
 */
public enum ProcessStatus {
    NOT_FILLED("0", "未填报"),
    FILLING("1", "正在填报"),
    SUBMITTED("2", "申请审核"),
    APPROVED("3", "已审核");

    private final String code;
    private final String description;

    ProcessStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProcessStatus fromCode(String code) {
        for (ProcessStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown process status code: " + code);
    }

    // 判断是否可以转换到目标状态
    public boolean canTransitionTo(ProcessStatus target) {
        switch (this) {
            case NOT_FILLED:
                return target == FILLING;
            case FILLING:
                return target == SUBMITTED;
            case SUBMITTED:
                return target == APPROVED;
            case APPROVED:
                return false; // 已审核状态不能再转换
            default:
                return false;
        }
    }

    // 判断是否可以编辑
    public boolean isEditable() {
        return this == NOT_FILLED || this == FILLING;
    }
}
