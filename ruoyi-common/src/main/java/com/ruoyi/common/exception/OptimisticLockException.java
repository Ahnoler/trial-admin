package com.ruoyi.common.exception;

public class OptimisticLockException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public OptimisticLockException() {
        super("数据已被其他用户修改，请刷新后重试");
    }

    public OptimisticLockException(String message) {
        super(message);
    }

    public OptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
