package org.example.wms_backend.mapper;

import org.example.wms_backend.entity.Batch;
import org.example.wms_backend.entity.BatchRemainingStatistic;
import org.example.wms_backend.entity.BatchExpiryStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

/**
 * 批次表Mapper接口
 */
@Mapper
public interface BatchMapper {
    // 新增批次
    int insert(Batch batch);
    
    // 根据主键更新批次
    int updateByPrimaryKey(Batch batch);
    
    // 根据主键查询批次
    Batch selectByPrimaryKey(Integer id);
    
    // 根据批次号查询批次
    Batch selectByBatchCode(@Param("batchCode") String batchCode);
    
    // 根据商品编码查询批次
    List<Batch> selectByGoodsCode(@Param("goodsCode") String goodsCode);
    
    // 根据仓库编码查询批次
    List<Batch> selectByWarehouseCode(@Param("warehouseCode") String warehouseCode);

    // 根据商品编码和仓库编码查询批次
    List<Batch> selectByGoodsCodeAndWarehouseCode(@Param("goodsCode") String goodsCode, @Param("warehouseCode") String warehouseCode);
    
    // 根据状态查询批次
    List<Batch> selectByStatus(@Param("status") String status);
    
    // 根据创建时间查询批次
    List<Batch> selectByCreateTime(@Param("createTime") Date createTime);
    
    // 逻辑删除批次
    int updateIsDeletedByBatchCode(@Param("batchCode") String batchCode);
    
    // 查询所有批次
    List<Batch> selectAll();
    
    // 查询到期日期接近的批次（到期时间距离现在时间少于30天）
    List<Batch> selectNearExpiryBatches();

    // 查询到期日期接近一个月的批次（到期时间距离现在时间少于30天）
    List<Batch> selectNearExpiryBatchesOneMonth();

    // 更新已过期的批次状态（状态为正常但过期日期已过的批次改为过期）
    int updateExpiredBatchStatus();

    // 查询已过期的批次
    List<Batch> selectExpiredBatches();

    // 批次剩余库存统计
    List<BatchRemainingStatistic> selectBatchRemainingStatistics();

    // 批次过期预警统计
    List<BatchExpiryStatistic> selectBatchExpiryStatistics();

    // 分页查询批次
    List<Batch> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询批次总数
    int selectCount();

    // 根据批次号模糊查询分页
    List<Batch> selectByBatchNoLikePage(@Param("batchNo") String batchNo, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据批次号模糊查询总数
    int selectCountByBatchNoLike(@Param("batchNo") String batchNo);

    // 根据仓库编码分页查询
    List<Batch> selectByWarehouseCodePage(@Param("warehouseCode") String warehouseCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据仓库编码查询总数
    int selectCountByWarehouseCode(@Param("warehouseCode") String warehouseCode);

    // 根据商品编码分页查询
    List<Batch> selectByGoodsCodePage(@Param("goodsCode") String goodsCode, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据商品编码查询总数
    int selectCountByGoodsCode(@Param("goodsCode") String goodsCode);

    // 根据状态分页查询
    List<Batch> selectByStatusPage(@Param("status") String status, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据状态查询总数
    int selectCountByStatus(@Param("status") String status);

    // 分页查询接近过期一个月的批次
    List<Batch> selectNearExpiryBatchesOneMonthPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询接近过期一个月的批次总数
    int selectCountNearExpiryBatchesOneMonth();

    // 分页查询已过期的批次
    List<Batch> selectExpiredBatchesPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询已过期的批次总数
    int selectCountExpiredBatches();
}