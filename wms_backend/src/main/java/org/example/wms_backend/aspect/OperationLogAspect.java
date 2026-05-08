package org.example.wms_backend.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.wms_backend.entity.OperationLog;
import org.example.wms_backend.mapper.OperationLogMapper;
import org.example.wms_backend.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 定义切点，拦截所有Controller方法
     */
    @Pointcut("execution(* org.example.wms_backend.controller.*Controller.*(..))")
    public void operationLogPointcut() {
    }

    /**
     * 方法执行后记录操作日志
     * @param joinPoint 连接点
     * @param result 返回结果
     */
    @AfterReturning(pointcut = "operationLogPointcut()", returning = "result")
    public void recordOperationLog(JoinPoint joinPoint, Object result) {
        try {
            // 获取请求信息
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String requestMethod = request.getMethod();
            String requestUri = request.getRequestURI();

            // 过滤掉查询操作，只记录增删改操作
            if ("GET".equals(requestMethod)) {
                return;
            }

            // 从SecurityContext中获取用户信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.error("用户未登录，无法记录操作日志");
                return;
            }

            // 获取用户名（账号）
            String account = authentication.getName();
            
            // 如果是匿名用户，不记录日志
            if ("anonymousUser".equals(account)) {
                return;
            }

            // 解析操作类型和操作详情
            String operationType = parseOperationType(requestMethod, requestUri);
            String operationDetail = parseOperationDetail(requestUri, joinPoint.getArgs());

            // 创建操作日志
    OperationLog operationLog = new OperationLog();
    
    // 设置用户账号
    operationLog.setAccount(account);
            operationLog.setOperationType(operationType);
            operationLog.setOperationDetail(operationDetail);
            operationLog.setCreateTime(new Date());
            operationLog.setUpdateTime(new Date());
            operationLog.setIsDeleted(0);

            // 保存操作日志
            operationLogMapper.insert(operationLog);
            logger.info("操作日志记录成功: {}", operationLog);
        } catch (Exception e) {
            logger.error("记录操作日志失败: {}", e.getMessage());
        }
    }

    /**
     * 解析操作类型
     * @param requestMethod 请求方法
     * @param requestUri 请求路径
     * @return 操作类型
     */
    private String parseOperationType(String requestMethod, String requestUri) {
        if ("POST".equals(requestMethod)) {
            if (requestUri.contains("/add")) {
                return "新增";
            }
        } else if ("PUT".equals(requestMethod)) {
            if (requestUri.contains("/update")) {
                return "修改";
            }
        } else if ("DELETE".equals(requestMethod)) {
            if (requestUri.contains("/delete")) {
                return "删除";
            }
        }
        return "其他";
    }

    /**
     * 解析操作详情
     * @param requestUri 请求路径
     * @param args 方法参数
     * @return 操作详情
     */
    private String parseOperationDetail(String requestUri, Object[] args) {
        StringBuilder detail = new StringBuilder();
        detail.append("接口: ").append(requestUri);
        if (args != null && args.length > 0) {
            detail.append("，参数: ");
            for (Object arg : args) {
                if (arg != null) {
                    detail.append(arg.toString()).append(" ");
                }
            }
        }
        return detail.toString();
    }
}