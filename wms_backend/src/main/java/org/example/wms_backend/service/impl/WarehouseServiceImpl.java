package org.example.wms_backend.service.impl;

import org.example.wms_backend.common.StringUtil;
import org.example.wms_backend.dto.WarehouseDTO;
import org.example.wms_backend.entity.Warehouse;
import org.example.wms_backend.mapper.WarehouseMapper;
import org.example.wms_backend.service.WarehouseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 仓库表服务实现类
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public String save(WarehouseDTO warehouseDTO) {
        // 自动生成仓库编码
        String warehouseCode;
        do {
            warehouseCode = "WH" + StringUtil.generateRandomString(8);
        } while (warehouseMapper.selectByWarehouseCode(warehouseCode) != null);

        // 将DTO转换为实体类
        Warehouse warehouse = new Warehouse();
        BeanUtils.copyProperties(warehouseDTO, warehouse);
        warehouse.setWarehouseCode(warehouseCode);

        // 设置默认值
        Date now = new Date();
        warehouse.setCreateTime(now);
        warehouse.setUpdateTime(now);
        warehouse.setIsDeleted(0);

        // 保存数据
        warehouseMapper.insert(warehouse);

        return "添加仓库成功";
    }

    @Override
    public List<WarehouseDTO> findAll() {
        // 查询所有仓库
        List<Warehouse> warehouseList = warehouseMapper.selectAll();
        
        // 将实体类转换为DTO
        return convertToDTOList(warehouseList);
    }

    @Override
    public WarehouseDTO findByWarehouseCode(String warehouseCode) {
        // 根据仓库编码查询仓库
        Warehouse warehouse = warehouseMapper.selectByWarehouseCode(warehouseCode);
        
        // 将实体类转换为DTO
        if (warehouse != null) {
            return convertToDTO(warehouse);
        }
        return null;
    }

    @Override
    public List<WarehouseDTO> findByStatus(String status) {
        // 根据仓库状态查询仓库
        List<Warehouse> warehouseList = warehouseMapper.selectByStatus(status);
        
        // 将实体类转换为DTO
        return convertToDTOList(warehouseList);
    }

    @Override
    public List<WarehouseDTO> findByWarehouseType(String warehouseType) {
        // 根据仓库类型查询仓库
        List<Warehouse> warehouseList = warehouseMapper.selectByWarehouseType(warehouseType);
        
        // 将实体类转换为DTO
        return convertToDTOList(warehouseList);
    }

    @Override
    public List<WarehouseDTO> findByWarehouseNameLike(String warehouseName) {
        // 根据仓库名称模糊查询仓库
        List<Warehouse> warehouseList = warehouseMapper.selectByWarehouseNameLike(warehouseName);
        
        // 将实体类转换为DTO
        return convertToDTOList(warehouseList);
    }

    @Override
    public String updateWarehouse(WarehouseDTO warehouseDTO) {
        // 检查仓库是否存在
        if (warehouseDTO.getWarehouseCode() == null) {
            throw new org.example.wms_backend.exception.BusinessException("修改仓库失败：仓库编码不能为空");
        }
        
        Warehouse existingWarehouse = warehouseMapper.selectByWarehouseCode(warehouseDTO.getWarehouseCode());
        if (existingWarehouse == null) {
            throw new org.example.wms_backend.exception.BusinessException("修改仓库失败：仓库不存在");
        }

        // 将DTO转换为实体类
        Warehouse warehouse = new Warehouse();
        BeanUtils.copyProperties(warehouseDTO, warehouse);

        // 更新数据
        warehouse.setUpdateTime(new Date());
        warehouseMapper.updateByWarehouseCode(warehouse);

        return "修改仓库成功";
    }

    @Override
    public String updateIsDeleted(String warehouseCode, Integer isDeleted) {
        // 检查仓库是否存在
        Warehouse existingWarehouse = warehouseMapper.selectByWarehouseCode(warehouseCode);
        if (existingWarehouse == null) {
            throw new org.example.wms_backend.exception.BusinessException("修改仓库状态失败：仓库不存在");
        }

        // 更新逻辑删除状态
        warehouseMapper.updateIsDeletedByWarehouseCode(warehouseCode, isDeleted);

        return "修改仓库状态成功";
    }

    @Override
    public List<String> findCodeByWarehouseName(String warehouseName) {
        List<Warehouse> warehouseList = warehouseMapper.selectByWarehouseNameLike(warehouseName);
        List<String> warehouseCodeList = new ArrayList<>();
        if (warehouseList != null && !warehouseList.isEmpty()) {
            for (Warehouse warehouse : warehouseList) {
                warehouseCodeList.add(warehouse.getWarehouseCode());
            }
        }
        return warehouseCodeList;
    }

    /**
     * 将Warehouse实体类转换为WarehouseDTO
     */
    private WarehouseDTO convertToDTO(Warehouse warehouse) {
        WarehouseDTO warehouseDTO = new WarehouseDTO();
        BeanUtils.copyProperties(warehouse, warehouseDTO);
        return warehouseDTO;
    }

    /**
     * 将Warehouse实体类列表转换为WarehouseDTO列表
     */
    private List<WarehouseDTO> convertToDTOList(List<Warehouse> warehouseList) {
        List<WarehouseDTO> warehouseDTOList = new ArrayList<>();
        if (warehouseList != null && !warehouseList.isEmpty()) {
            for (Warehouse warehouse : warehouseList) {
                warehouseDTOList.add(convertToDTO(warehouse));
            }
        }
        return warehouseDTOList;
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<WarehouseDTO> findByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Warehouse> warehouseList = warehouseMapper.selectByPage(offset, pageSize);
        int total = warehouseMapper.selectCount();
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(warehouseList), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<WarehouseDTO> findByWarehouseNamePage(String warehouseName, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Warehouse> warehouseList = warehouseMapper.selectByWarehouseNameLikePage(warehouseName, offset, pageSize);
        int total = warehouseMapper.selectCountByWarehouseNameLike(warehouseName);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(warehouseList), total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<WarehouseDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Warehouse> warehouseList = warehouseMapper.selectByWarehouseCodePage(warehouseCode, offset, pageSize);
        int total = warehouseMapper.selectCountByWarehouseCode(warehouseCode);
        return new org.example.wms_backend.dto.PageResponseDTO<>(convertToDTOList(warehouseList), total, page, pageSize);
    }
}