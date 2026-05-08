package org.example.wms_backend.service;

import org.example.wms_backend.dto.OperationLogDTO;

import java.util.List;

/**
 * 操作日志表服务接口
 */
public interface OperationLogService {
    /**
     * 查询所有操作日志
     */
    List<OperationLogDTO> findAll();

    /**
     * 根据操作类型查询操作日志
     */
    List<OperationLogDTO> findByOperationType(String operationType);

    /**
     * 根据用户账号查询操作日志
     */
    List<OperationLogDTO> findByAccount(String account);

    /**
     * 分页查询操作日志
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<OperationLogDTO> findByPage(int page, int pageSize);

    /**
     * 根据操作类型模糊查询分页
     * @param operationType 操作类型
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<OperationLogDTO> findByOperationTypePage(String operationType, int page, int pageSize);

    /**
     * 根据用户账号模糊查询分页
     * @param account 用户账号
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<OperationLogDTO> findByAccountPage(String account, int page, int pageSize);
}

