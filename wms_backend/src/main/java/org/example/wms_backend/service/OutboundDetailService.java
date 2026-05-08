package org.example.wms_backend.service;

import org.example.wms_backend.dto.OutboundDetailDTO;

import java.util.List;

/**
 * 出库详情表服务接口
 */
public interface OutboundDetailService {
    /**
     * 新增出库明细
     * @param outboundDetailDTO 出库明细DTO
     * @return 操作结果
     */
    String save(OutboundDetailDTO outboundDetailDTO);
    
    /**
     * 逻辑删除出库明细
     * @param outboundId 出库单ID
     * @return 操作结果
     */
    String deleteByOutboundId(String outboundId);
    
    /**
     * 查询所有出库明细
     * @return 出库明细列表
     */
    List<OutboundDetailDTO> findAll();
    
    /**
     * 根据商品编码查询出库明细
     * @param goodsCode 商品编码
     * @return 出库明细列表
     */
    List<OutboundDetailDTO> findByGoodsCode(String goodsCode);
    
    /**
     * 根据仓库编码查询出库明细
     * @param warehouseCode 仓库编码
     * @return 出库明细列表
     */
    List<OutboundDetailDTO> findByWarehouseCode(String warehouseCode);
    
    /**
     * 根据批次号查询出库明细
     * @param batchNo 批次号
     * @return 出库明细列表
     */
    List<OutboundDetailDTO> findByBatchNo(String batchNo);
    
    /**
     * 根据出库单ID查询出库明细
     * @param outboundId 出库单ID
     * @return 出库明细列表
     */
    List<OutboundDetailDTO> findByOutboundId(String outboundId);

    /**
     * 统计近一周内每个商品出库总数量
     * @return 商品出库统计列表
     */
    List<org.example.wms_backend.entity.GoodsOutboundStatistic> getWeeklyOutboundStatistic();

    /**
     * 统计近7天每天出库总数量
     * @return 每天出库统计列表
     */
    List<org.example.wms_backend.entity.DailyOutboundStatistic> getDailyOutboundStatistic();

    /**
     * 分页查询出库明细
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByPage(int page, int pageSize);

    /**
     * 根据商品编码模糊查询分页
     * @param goodsCode 商品编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByGoodsCodePage(String goodsCode, int page, int pageSize);

    /**
     * 根据仓库编码分页查询
     * @param warehouseCode 仓库编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize);

    /**
     * 根据批次号分页查询
     * @param batchNo 批次号
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByBatchNoPage(String batchNo, int page, int pageSize);

    /**
     * 根据出库单ID分页查询
     * @param outboundId 出库单ID
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<OutboundDetailDTO> findByOutboundIdPage(String outboundId, int page, int pageSize);
}

