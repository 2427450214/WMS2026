package org.example.wms_backend.service.impl;

import org.example.wms_backend.dto.StockChangeLogDTO;
import org.example.wms_backend.entity.StockChangeLog;
import org.example.wms_backend.mapper.StockChangeLogMapper;
import org.example.wms_backend.service.StockChangeLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 库存变动日志表服务实现类
 */
@Service
public class StockChangeLogServiceImpl implements StockChangeLogService {

    @Autowired
    private StockChangeLogMapper stockChangeLogMapper;

    @Override
    public List<StockChangeLogDTO> findAll() {
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectAll();
        return convertToDTOList(stockChangeLogs);
    }

    @Override
    public List<StockChangeLogDTO> findByOperationType(String operationType) {
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByOperationType(operationType);
        return convertToDTOList(stockChangeLogs);
    }

    @Override
    public List<StockChangeLogDTO> findByWarehouseCode(String warehouseCode) {
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByWarehouseCode(warehouseCode);
        return convertToDTOList(stockChangeLogs);
    }

    @Override
    public List<StockChangeLogDTO> findByUserName(String userName) {
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByUserName(userName);
        return convertToDTOList(stockChangeLogs);
    }

    @Override
    public List<StockChangeLogDTO> findByGoodsCode(String goodsCode) {
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByGoodsCode(goodsCode);
        return convertToDTOList(stockChangeLogs);
    }

    @Override
    public List<StockChangeLogDTO> findByAccount(String account) {
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByAccount(account);
        return convertToDTOList(stockChangeLogs);
    }

    @Override
    public List<StockChangeLogDTO> findByInOutDetailCode(String inOutDetailCode) {
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByInOutDetailCode(inOutDetailCode);
        return convertToDTOList(stockChangeLogs);
    }

    @Override
    public StockChangeLogDTO findById(Integer id) {
        StockChangeLog stockChangeLog = stockChangeLogMapper.selectByPrimaryKey(id);
        if (stockChangeLog != null) {
            return convertToDTO(stockChangeLog);
        }
        return null;
    }

    @Override
    public String deleteById(Integer id) {
        // 检查库存变动日志是否存在
        StockChangeLog stockChangeLog = stockChangeLogMapper.selectByPrimaryKey(id);
        if (stockChangeLog == null) {
            return "删除失败：库存变动日志不存在";
        }
        
        // 执行逻辑删除
        int result = stockChangeLogMapper.updateIsDeletedById(id);
        if (result > 0) {
            return "删除库存变动日志成功";
        } else {
            return "删除库存变动日志失败";
        }
    }

    /**
     * 将StockChangeLog实体转换为StockChangeLogDTO
     * @param stockChangeLog StockChangeLog实体
     * @return StockChangeLogDTO
     */
    private StockChangeLogDTO convertToDTO(StockChangeLog stockChangeLog) {
        StockChangeLogDTO dto = new StockChangeLogDTO();
        // 先复制基本属性
        dto.setId(stockChangeLog.getId() != null ? stockChangeLog.getId().longValue() : null);
        dto.setGoodsCode(stockChangeLog.getGoodsCode());
        dto.setWarehouseCode(stockChangeLog.getWarehouseCode());
        dto.setBeforeQuantity(stockChangeLog.getBeforeQuantity());
        dto.setChangeQuantity(stockChangeLog.getChangeQuantity());
        dto.setAfterQuantity(stockChangeLog.getAfterQuantity());
        dto.setOperationType(stockChangeLog.getOperationType());
        dto.setInOutDetailCode(stockChangeLog.getInOutDetailCode());
        dto.setAccount(stockChangeLog.getAccount());
        dto.setUserName(stockChangeLog.getUserName());
        
        // 手动处理日期转换
        if (stockChangeLog.getOperationTime() != null) {
            dto.setOperationTime(convertToLocalDateTime(stockChangeLog.getOperationTime()));
        }
        
        return dto;
    }

    /**
     * 将StockChangeLog实体列表转换为StockChangeLogDTO列表
     * @param stockChangeLogs StockChangeLog实体列表
     * @return StockChangeLogDTO列表
     */
    private List<StockChangeLogDTO> convertToDTOList(List<StockChangeLog> stockChangeLogs) {
        List<StockChangeLogDTO> dtoList = new ArrayList<>();
        if (stockChangeLogs != null && !stockChangeLogs.isEmpty()) {
            for (StockChangeLog stockChangeLog : stockChangeLogs) {
                dtoList.add(convertToDTO(stockChangeLog));
            }
        }
        return dtoList;
    }

    /**
     * 将Date转换为LocalDateTime
     * @param date Date对象
     * @return LocalDateTime对象
     */
    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByPage(offset, pageSize);
        int total = stockChangeLogMapper.selectCount();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(stockChangeLogs), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByGoodsCodePage(String goodsCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByGoodsCodeLikePage(goodsCode, offset, pageSize);
        int total = stockChangeLogMapper.selectCountByGoodsCodeLike(goodsCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(stockChangeLogs), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByOperationTypePage(String operationType, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByOperationTypeLikePage(operationType, offset, pageSize);
        int total = stockChangeLogMapper.selectCountByOperationTypeLike(operationType);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(stockChangeLogs), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByWarehouseCodeLikePage(warehouseCode, offset, pageSize);
        int total = stockChangeLogMapper.selectCountByWarehouseCodeLike(warehouseCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(stockChangeLogs), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByUserNamePage(String userName, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByUserNameLikePage(userName, offset, pageSize);
        int total = stockChangeLogMapper.selectCountByUserNameLike(userName);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(stockChangeLogs), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByAccountPage(String account, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByAccountLikePage(account, offset, pageSize);
        int total = stockChangeLogMapper.selectCountByAccountLike(account);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(stockChangeLogs), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<StockChangeLogDTO> findByInOutDetailCodePage(String inOutDetailCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<StockChangeLog> stockChangeLogs = stockChangeLogMapper.selectByInOutDetailCodeLikePage(inOutDetailCode, offset, pageSize);
        int total = stockChangeLogMapper.selectCountByInOutDetailCodeLike(inOutDetailCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(stockChangeLogs), total, page, pageSize);
    }
}