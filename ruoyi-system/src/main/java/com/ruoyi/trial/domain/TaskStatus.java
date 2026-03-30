package com.ruoyi.trial.domain;

/**
 * 试制任务状态枚举
 */
public enum TaskStatus {
    NORMAL("0", "正常"),
    DISABLED("1", "停用"),
    FINISHED("2", "已结束");

    private final String code;
    private final String description;

    TaskStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TaskStatus fromCode(String code) {
        for (TaskStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown task status code: " + code);
    }
}
