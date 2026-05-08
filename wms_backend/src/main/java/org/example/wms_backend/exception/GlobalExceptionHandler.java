package org.example.wms_backend.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@Order(1)
public class GlobalExceptionHandler {
    /**
     * 是否为开发环境
     */
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * 处理权限访问异常
     * @param e 权限访问异常
     * @return 错误信息
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @Order(1)
    public Map<String, Object> handleAccessDeniedException(AccessDeniedException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 403);
        result.put("message", "没有权限访问该资源");
        return result;
    }

    /**
     * 处理验证异常（@Valid 验证失败）
     * @param e 验证异常
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @Order(2)
    public Map<String, Object> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        
        // 获取第一个验证错误的消息，只返回友好的提示信息
        StringBuilder errorMessage = new StringBuilder();
        if (e.getBindingResult().hasErrors()) {
            FieldError firstError = e.getBindingResult().getFieldErrors().get(0);
            errorMessage.append(firstError.getDefaultMessage());
        }
        
        // 如果没有自定义消息，返回通用提示
        if (errorMessage.length() == 0) {
            errorMessage.append("参数验证失败");
        }
        
        result.put("message", errorMessage.toString());
        
        // 在开发环境下返回详细错误信息
        if ("dev".equals(activeProfile)) {
            result.put("error", e.getMessage());
            result.put("stackTrace", getStackTrace(e));
        }
        
        return result;
    }

    /**
     * 处理绑定异常（表单绑定验证失败）
     * @param e 绑定异常
     * @return 错误信息
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    @Order(3)
    public Map<String, Object> handleBindException(BindException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        
        // 获取第一个验证错误的消息，只返回友好的提示信息
        StringBuilder errorMessage = new StringBuilder();
        if (e.getBindingResult().hasErrors()) {
            FieldError firstError = e.getBindingResult().getFieldErrors().get(0);
            errorMessage.append(firstError.getDefaultMessage());
        }
        
        // 如果没有自定义消息，返回通用提示
        if (errorMessage.length() == 0) {
            errorMessage.append("参数验证失败");
        }
        
        result.put("message", errorMessage.toString());
        
        // 在开发环境下返回详细错误信息
        if ("dev".equals(activeProfile)) {
            result.put("error", e.getMessage());
            result.put("stackTrace", getStackTrace(e));
        }
        
        return result;
    }

    /**
     * 处理业务异常
     * @param e 业务异常
     * @return 错误信息
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Map<String, Object> handleBusinessException(BusinessException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", e.getCode() != null ? e.getCode() : 500);
        result.put("message", e.getMessage());
        
        // 在开发环境下返回详细错误信息
        if ("dev".equals(activeProfile)) {
            result.put("error", e.getMessage());
            result.put("stackTrace", getStackTrace(e));
        }
        
        return result;
    }

    /**
     * 处理系统异常
     * @param e 系统异常
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> handleException(Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "系统内部错误：" + e.getMessage());
        
        // 在开发环境下返回详细错误信息
        if ("dev".equals(activeProfile)) {
            result.put("error", e.getMessage());
            result.put("stackTrace", getStackTrace(e));
        }
        
        return result;
    }

    /**
     * 获取异常堆栈信息
     * @param e 异常
     * @return 堆栈信息字符串
     */
    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
