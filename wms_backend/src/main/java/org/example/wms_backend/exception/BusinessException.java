package org.example.wms_backend.exception;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 构造方法
     * @param message 错误信息
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 构造方法
     * @param code 错误码
     * @param message 错误信息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
