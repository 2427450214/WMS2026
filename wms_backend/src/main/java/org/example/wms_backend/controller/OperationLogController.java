package org.example.wms_backend.controller;

import org.example.wms_backend.dto.OperationLogDTO;
import org.example.wms_backend.service.OperationLogService;
import org.example.wms_backend.common.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 操作日志表控制器
 */
@RestController
@RequestMapping("/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询所有操作日志
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> findAll() {
        List<OperationLogDTO> operationLogDTOList = operationLogService.findAll();
        if (operationLogDTOList != null && !operationLogDTOList.isEmpty()) {
            return ResponseResult.success(operationLogDTOList);
        } else {
            return ResponseResult.error("未找到对应操作日志");
        }
    }

    /**
     * 根据操作类型查询操作日志
     */
    @GetMapping("/findByOperationType")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> findByOperationType(@RequestParam(required = false) String operationType) {
        if (operationType == null || operationType.trim().isEmpty()) {
            return ResponseResult.error("操作类型不能为空");
        }
        List<OperationLogDTO> operationLogDTOList = operationLogService.findByOperationType(operationType);
        if (operationLogDTOList != null && !operationLogDTOList.isEmpty()) {
            return ResponseResult.success(operationLogDTOList);
        } else {
            return ResponseResult.error("未找到对应操作日志");
        }
    }

    /**
     * 根据用户账号查询操作日志
     */
    @GetMapping("/findByAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> findByAccount(@RequestParam(required = false) String account) {
        if (account == null || account.trim().isEmpty()) {
            return ResponseResult.error("用户账号不能为空");
        }
        List<OperationLogDTO> operationLogDTOList = operationLogService.findByAccount(account);
        if (operationLogDTOList != null && !operationLogDTOList.isEmpty()) {
            return ResponseResult.success(operationLogDTOList);
        } else {
            return ResponseResult.error("未找到对应操作日志");
        }
    }

    /**
     * 分页查询操作日志
     */
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getOperationLogByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<OperationLogDTO> pageResponse = operationLogService.findByPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据操作类型模糊查询分页
     */
    @GetMapping("/page/operationType")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getOperationLogByOperationTypePage(@RequestParam String operationType, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<OperationLogDTO> pageResponse = operationLogService.findByOperationTypePage(operationType, page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 根据用户账号模糊查询分页
     */
    @GetMapping("/page/account")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getOperationLogByAccountPage(@RequestParam String account, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<OperationLogDTO> pageResponse = operationLogService.findByAccountPage(account, page, pageSize);
        return ResponseResult.success(pageResponse);
    }
}
