package org.example.wms_backend.service.impl;

import org.example.wms_backend.dto.OperationLogDTO;
import org.example.wms_backend.entity.OperationLog;
import org.example.wms_backend.mapper.OperationLogMapper;
import org.example.wms_backend.service.OperationLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志表服务实现类
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public List<OperationLogDTO> findAll() {
        List<OperationLog> operationLogList = operationLogMapper.selectAll();
        return convertToDTOList(operationLogList);
    }

    @Override
    public List<OperationLogDTO> findByOperationType(String operationType) {
        List<OperationLog> operationLogList = operationLogMapper.selectByOperationType(operationType);
        return convertToDTOList(operationLogList);
    }

    @Override
    public List<OperationLogDTO> findByAccount(String account) {
        List<OperationLog> operationLogList = operationLogMapper.selectByAccount(account);
        return convertToDTOList(operationLogList);
    }

    /**
     * 将实体类列表转换为DTO列表
     */
    private List<OperationLogDTO> convertToDTOList(List<OperationLog> operationLogList) {
        List<OperationLogDTO> operationLogDTOList = new ArrayList<>();
        if (operationLogList != null && !operationLogList.isEmpty()) {
            for (OperationLog operationLog : operationLogList) {
                OperationLogDTO operationLogDTO = new OperationLogDTO();
                BeanUtils.copyProperties(operationLog, operationLogDTO);
                operationLogDTOList.add(operationLogDTO);
            }
        }
        return operationLogDTOList;
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<OperationLogDTO> findByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OperationLog> operationLogList = operationLogMapper.selectByPage(offset, pageSize);
        int total = operationLogMapper.selectCount();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(operationLogList), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<OperationLogDTO> findByOperationTypePage(String operationType, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OperationLog> operationLogList = operationLogMapper.selectByOperationTypeLikePage(operationType, offset, pageSize);
        int total = operationLogMapper.selectCountByOperationTypeLike(operationType);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(operationLogList), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<OperationLogDTO> findByAccountPage(String account, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OperationLog> operationLogList = operationLogMapper.selectByAccountLikePage(account, offset, pageSize);
        int total = operationLogMapper.selectCountByAccountLike(account);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(operationLogList), total, page, pageSize);
    }
}