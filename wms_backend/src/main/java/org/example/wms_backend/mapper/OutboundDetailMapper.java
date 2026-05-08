package org.example.wms_backend.mapper;

import org.example.wms_backend.entity.OutboundDetail;
import org.example.wms_backend.entity.GoodsOutboundStatistic;
import org.example.wms_backend.entity.DailyOutboundStatistic;
import org.example.wms_backend.entity.CategoryInOutStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库明细表Mapper接口
 */
@Mapper
public interface OutboundDetailMapper {
    // 基本CRUD方法
    int insert(OutboundDetail outboundDetail);
    int updateByPrimaryKey(OutboundDetail outboundDetail);
    int deleteByPrimaryKey(Integer id);
    OutboundDetail selectByPrimaryKey(Integer id);
    
    // 逻辑删除
    int updateIsDeletedByOutboundId(@Param("outboundId") String outboundId, @Param("isDeleted") Integer isDeleted);
    
    // 根据商品编码查询
    List<OutboundDetail> selectByGoodsCode(@Param("goodsCode") String goodsCode);
    
    // 根据仓库编码查询
    List<OutboundDetail> selectByWarehouseCode(@Param("warehouseCode") String warehouseCode);
    
    // 根据批次号查询
    List<OutboundDetail> selectByBatchNo(@Param("batchNo") String batchNo);
    
    // 根据出库单ID查询
    List<OutboundDetail> selectByOutboundId(@Param("outboundId") String outboundId);
    
    // 查询所有出库明细
    List<OutboundDetail> selectAll();

    // 统计近一周内每个商品出库总数量
    List<GoodsOutboundStatistic> selectWeeklyOutboundStatistic();

    // 统计近7天每天出库总数量
    List<DailyOutboundStatistic> selectDailyOutboundStatistic();

    // 按分类统计出库
    List<CategoryInOutStatistic> selectCategoryOutboundStatistics(@Param("days") int days);

    // 分页查询出库明细
    List<OutboundDetail> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询出库明细总数
    int selectCount();

    // 根据商品编码模糊查询分页
    List<OutboundDetail> selectByGoodsCodeLikePage(@Param("goodsCode") String goodsCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据商品编码模糊查询总数
    int selectCountByGoodsCodeLike(@Param("goodsCode") String goodsCode);

    // 根据仓库编码分页查询
    List<OutboundDetail> selectByWarehouseCodePage(@Param("warehouseCode") String warehouseCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据仓库编码查询总数
    int selectCountByWarehouseCode(@Param("warehouseCode") String warehouseCode);

    // 根据批次号分页查询
    List<OutboundDetail> selectByBatchNoPage(@Param("batchNo") String batchNo, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据批次号查询总数
    int selectCountByBatchNo(@Param("batchNo") String batchNo);

    // 根据出库单ID分页查询
    List<OutboundDetail> selectByOutboundIdPage(@Param("outboundId") String outboundId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据出库单ID查询总数
    int selectCountByOutboundId(@Param("outboundId") String outboundId);
}
