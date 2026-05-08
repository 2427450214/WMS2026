package org.example.wms_backend.service;

import org.example.wms_backend.dto.BatchDTO;

import java.util.List;

/**
 * 批次表服务接口
 */
public interface BatchService {
    /**
     * 新增批次
     * @param batchDTO 批次DTO
     * @return 操作结果
     */
    String save(BatchDTO batchDTO);

    /**
     * 查询所有批次
     * @return 批次DTO列表
     */
    List<BatchDTO> findAll();

    /**
     * 根据批次号查询批次
     * @param batchNumber 批次号
     * @return 批次DTO
     */
    BatchDTO findByBatchNumber(String batchNumber);

    /**
     * 根据商品编码查询批次
     * @param goodsCode 商品编码
     * @return 批次DTO列表
     */
    List<BatchDTO> findByGoodsCode(String goodsCode);

    /**
     * 根据仓库编码查询批次
     * @param warehouseCode 仓库编码
     * @return 批次DTO列表
     */
    List<BatchDTO> findByWarehouseCode(String warehouseCode);

    /**
     * 根据商品编码和仓库编码查询批次
     * @param goodsCode 商品编码
     * @param warehouseCode 仓库编码
     * @return 批次DTO列表
     */
    List<BatchDTO> findByGoodsCodeAndWarehouseCode(String goodsCode, String warehouseCode);

    /**
     * 根据状态查询批次
     * @param status 状态
     * @return 批次DTO列表
     */
    List<BatchDTO> findByStatus(String status);
    
    /**
     * 查询到期日期接近的批次
     * @return 批次DTO列表
     */
    List<BatchDTO> findNearExpiryBatches();

    /**
     * 查询到期日期接近一个月的批次
     * @return 批次DTO列表
     */
    List<BatchDTO> findNearExpiryBatchesOneMonth();

    /**
     * 更新已过期的批次状态
     * @return 更新数量
     */
    int updateExpiredBatchStatus();

    /**
     * 查询已过期的批次
     * @return 已过期批次DTO列表
     */
    List<org.example.wms_backend.dto.BatchDTO> findExpiredBatches();

    /**
     * 分页查询批次
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByPage(int page, int pageSize);

    /**
     * 根据批次号模糊查询分页
     * @param batchNo 批次号
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByBatchNoPage(String batchNo, int page, int pageSize);

    /**
     * 根据仓库编码分页查询
     * @param warehouseCode 仓库编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize);

    /**
     * 根据商品编码分页查询
     * @param goodsCode 商品编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByGoodsCodePage(String goodsCode, int page, int pageSize);

    /**
     * 根据状态分页查询
     * @param status 状态
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findByStatusPage(String status, int page, int pageSize);

    /**
     * 分页查询接近过期一个月的批次
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findNearExpiryBatchesOneMonthPage(int page, int pageSize);

    /**
     * 分页查询已过期的批次
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.BatchDTO> findExpiredBatchesPage(int page, int pageSize);
}

