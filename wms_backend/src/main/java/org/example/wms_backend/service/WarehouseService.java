package org.example.wms_backend.service;

import org.example.wms_backend.dto.WarehouseDTO;

import java.util.List;

/**
 * 仓库表服务接口
 */
public interface WarehouseService {
    /**
     * 新增仓库
     * @param warehouseDTO 仓库信息
     * @return 操作结果
     */
    String save(WarehouseDTO warehouseDTO);
    
    /**
     * 查询所有仓库
     * @return 仓库列表
     */
    List<WarehouseDTO> findAll();
    
    /**
     * 根据仓库编码查询仓库
     * @param warehouseCode 仓库编码
     * @return 仓库信息
     */
    WarehouseDTO findByWarehouseCode(String warehouseCode);
    
    /**
     * 根据仓库状态查询仓库
     * @param status 仓库状态
     * @return 仓库列表
     */
    List<WarehouseDTO> findByStatus(String status);
    
    /**
     * 根据仓库类型查询仓库
     * @param warehouseType 仓库类型
     * @return 仓库列表
     */
    List<WarehouseDTO> findByWarehouseType(String warehouseType);
    
    /**
     * 根据仓库名称模糊查询仓库
     * @param warehouseName 仓库名称
     * @return 仓库列表
     */
    List<WarehouseDTO> findByWarehouseNameLike(String warehouseName);
    
    /**
     * 修改仓库（不能修改仓库编码）
     * @param warehouseDTO 仓库信息
     * @return 操作结果
     */
    String updateWarehouse(WarehouseDTO warehouseDTO);
    
    /**
     * 逻辑删除功能
     * @param warehouseCode 仓库编码
     * @param isDeleted 逻辑删除状态
     * @return 操作结果
     */
    String updateIsDeleted(String warehouseCode, Integer isDeleted);

    /**
     * 根据仓库名称查找仓库编码（模糊查询）
     * @param warehouseName 仓库名称
     * @return 仓库编码列表
     */
    List<String> findCodeByWarehouseName(String warehouseName);

    /**
     * 分页查询仓库
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<WarehouseDTO> findByPage(int page, int pageSize);

    /**
     * 根据仓库名称模糊查询分页
     * @param warehouseName 仓库名称
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<WarehouseDTO> findByWarehouseNamePage(String warehouseName, int page, int pageSize);

    /**
     * 根据仓库编码分页查询
     * @param warehouseCode 仓库编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<WarehouseDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize);
}

