package org.example.wms_backend.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应结果工具类
 */
public class ResponseResult {
    /**
     * 成功状态码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败状态码
     */
    public static final int ERROR_CODE = 500;

    /**
     * 参数错误状态码
     */
    public static final int PARAM_ERROR_CODE = 400;

    /**
     * 未授权状态码
     */
    public static final int UNAUTHORIZED_CODE = 401;

    /**
     * 禁止访问状态码
     */
    public static final int FORBIDDEN_CODE = 403;

    /**
     * 资源不存在状态码
     */
    public static final int NOT_FOUND_CODE = 404;

    /**
     * 成功消息
     */
    public static final String SUCCESS_MESSAGE = "操作成功";

    /**
     * 失败消息
     */
    public static final String ERROR_MESSAGE = "操作失败";

    /**
     * 参数错误消息
     */
    public static final String PARAM_ERROR_MESSAGE = "参数错误";

    /**
     * 未授权消息
     */
    public static final String UNAUTHORIZED_MESSAGE = "未授权";

    /**
     * 禁止访问消息
     */
    public static final String FORBIDDEN_MESSAGE = "禁止访问";

    /**
     * 资源不存在消息
     */
    public static final String NOT_FOUND_MESSAGE = "资源不存在";

    /**
     * 构建成功响应
     * @param data 响应数据
     * @return 响应结果
     */
    public static Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", SUCCESS_CODE);
        result.put("message", SUCCESS_MESSAGE);
        result.put("data", data);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 构建成功响应
     * @return 响应结果
     */
    public static Map<String, Object> success() {
        return success(null);
    }

    /**
     * 构建成功响应
     * @param data 响应数据
     * @param message 响应消息
     * @return 响应结果
     */
    public static Map<String, Object> success(Object data, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", SUCCESS_CODE);
        result.put("message", message);
        result.put("data", data);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 构建失败响应
     * @param message 错误信息
     * @return 响应结果
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", ERROR_CODE);
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 构建失败响应
     * @param code 错误码
     * @param message 错误信息
     * @return 响应结果
     */
    public static Map<String, Object> error(int code, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 构建参数错误响应
     * @param message 错误信息
     * @return 响应结果
     */
    public static Map<String, Object> paramError(String message) {
        return error(PARAM_ERROR_CODE, message != null ? message : PARAM_ERROR_MESSAGE);
    }

    /**
     * 构建未授权响应
     * @param message 错误信息
     * @return 响应结果
     */
    public static Map<String, Object> unauthorized(String message) {
        return error(UNAUTHORIZED_CODE, message != null ? message : UNAUTHORIZED_MESSAGE);
    }

    /**
     * 构建禁止访问响应
     * @param message 错误信息
     * @return 响应结果
     */
    public static Map<String, Object> forbidden(String message) {
        return error(FORBIDDEN_CODE, message != null ? message : FORBIDDEN_MESSAGE);
    }

    /**
     * 构建资源不存在响应
     * @param message 错误信息
     * @return 响应结果
     */
    public static Map<String, Object> notFound(String message) {
        return error(NOT_FOUND_CODE, message != null ? message : NOT_FOUND_MESSAGE);
    }

    /**
     * 构建分页响应
     * @param data 数据列表
     * @param total 总记录数
     * @param page 页码
     * @param size 每页大小
     * @return 响应结果
     */
    public static Map<String, Object> page(Object data, long total, int page, int size) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", SUCCESS_CODE);
        result.put("message", SUCCESS_MESSAGE);
        result.put("data", data);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
}
