package org.example.wms_backend.service;

import org.example.wms_backend.dto.InboundDetailDTO;

import java.util.List;

/**
 * 入库详情表服务接口
 */
public interface InboundDetailService {
    /**
     * 新增入库明细
     * @param inboundDetailDTO 入库明细DTO
     * @return 操作结果，包含批次号
     */
    String save(InboundDetailDTO inboundDetailDTO);

    /**
     * 根据仓库编码查询入库明细
     * @param warehouseCode 仓库编码
     * @return 入库明细列表
     */
    List<InboundDetailDTO> findByWarehouseCode(String warehouseCode);

    /**
     * 根据商品编码查询入库明细
     * @param goodsCode 商品编码
     * @return 入库明细列表
     */
    List<InboundDetailDTO> findByGoodsCode(String goodsCode);

    /**
     * 根据批次号查询入库明细
     * @param batchNo 批次号
     * @return 入库明细列表
     */
    List<InboundDetailDTO> findByBatchNo(String batchNo);

    /**
     * 根据入库单ID查询入库明细
     * @param inboundId 入库单ID
     * @return 入库明细列表
     */
    List<InboundDetailDTO> findByInboundId(String inboundId);

    /**
     * 逻辑删除入库明细
     * @param inboundId 入库单ID
     * @return 操作结果
     */
    String deleteByInboundId(String inboundId);
    
    /**
     * 查询所有入库明细
     * @return 入库明细列表
     */
    List<InboundDetailDTO> findAll();

    /**
     * 统计近一周内每个商品入库总数量
     * @return 商品入库统计列表
     */
    List<org.example.wms_backend.entity.GoodsInboundStatistic> getWeeklyInboundStatistic();

    /**
     * 统计近7天每天入库总数量
     * @return 每天入库统计列表
     */
    List<org.example.wms_backend.entity.DailyInboundStatistic> getDailyInboundStatistic();

    /**
     * 分页查询入库明细
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByPage(int page, int pageSize);

    /**
     * 根据商品编码模糊查询分页
     * @param goodsCode 商品编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByGoodsCodePage(String goodsCode, int page, int pageSize);

    /**
     * 根据仓库编码分页查询
     * @param warehouseCode 仓库编码
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByWarehouseCodePage(String warehouseCode, int page, int pageSize);

    /**
     * 根据批次号分页查询
     * @param batchNo 批次号
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByBatchNoPage(String batchNo, int page, int pageSize);

    /**
     * 根据入库单ID分页查询
     * @param inboundId 入库单ID
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<InboundDetailDTO> findByInboundIdPage(String inboundId, int page, int pageSize);
}

