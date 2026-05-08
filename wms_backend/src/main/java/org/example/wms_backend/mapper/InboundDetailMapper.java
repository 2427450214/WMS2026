package org.example.wms_backend.mapper;

import org.example.wms_backend.entity.InboundDetail;
import org.example.wms_backend.entity.GoodsInboundStatistic;
import org.example.wms_backend.entity.DailyInboundStatistic;
import org.example.wms_backend.entity.CategoryInOutStatistic;
import org.example.wms_backend.entity.DailyInOutStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库明细表Mapper接口
 */
@Mapper
public interface InboundDetailMapper {
    // 新增入库明细
    int insert(InboundDetail inboundDetail);
    
    // 根据主键查询入库明细
    InboundDetail selectByPrimaryKey(Integer id);
    
    // 根据仓库编码查询入库明细
    List<InboundDetail> selectByWarehouseCode(@Param("warehouseCode") String warehouseCode);
    
    // 根据商品编码查询入库明细
    List<InboundDetail> selectByGoodsCode(@Param("goodsCode") String goodsCode);
    
    // 根据批次号查询入库明细
    List<InboundDetail> selectByBatchNo(@Param("batchNo") String batchNo);
    
    // 根据入库单ID查询入库明细
    List<InboundDetail> selectByInboundId(@Param("inboundId") String inboundId);
    
    // 逻辑删除入库明细
    int updateIsDeletedByInboundId(@Param("inboundId") String inboundId, @Param("isDeleted") Integer isDeleted);
    
    // 查询所有入库明细
    List<InboundDetail> selectAll();

    // 统计近一周内每个商品入库总数量
    List<GoodsInboundStatistic> selectWeeklyInboundStatistic();

    // 统计近7天每天入库总数量
    List<DailyInboundStatistic> selectDailyInboundStatistic();

    // 按分类统计入库
    List<CategoryInOutStatistic> selectCategoryInboundStatistics(@Param("days") int days);

    // 每日出入库统计
    List<DailyInOutStatistic> selectDailyInboundOutboundStatistics(@Param("days") int days);

    // 分页查询入库明细
    List<InboundDetail> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询入库明细总数
    int selectCount();

    // 根据商品编码模糊查询分页
    List<InboundDetail> selectByGoodsCodeLikePage(@Param("goodsCode") String goodsCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据商品编码模糊查询总数
    int selectCountByGoodsCodeLike(@Param("goodsCode") String goodsCode);

    // 根据仓库编码分页查询
    List<InboundDetail> selectByWarehouseCodePage(@Param("warehouseCode") String warehouseCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据仓库编码查询总数
    int selectCountByWarehouseCode(@Param("warehouseCode") String warehouseCode);

    // 根据批次号分页查询
    List<InboundDetail> selectByBatchNoPage(@Param("batchNo") String batchNo, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据批次号查询总数
    int selectCountByBatchNo(@Param("batchNo") String batchNo);

    // 根据入库单ID分页查询
    List<InboundDetail> selectByInboundIdPage(@Param("inboundId") String inboundId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据入库单ID查询总数
    int selectCountByInboundId(@Param("inboundId") String inboundId);
}
