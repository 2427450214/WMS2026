package org.example.wms_backend.service;

import org.example.wms_backend.entity.*;

import java.util.List;

/**
 * 统计服务接口
 */
public interface StatisticsService {
    /**
     * 按分类统计库存
     * @return 分类库存统计列表
     */
    List<CategoryStockStatistic> getCategoryStockStatistics();

    /**
     * 库存预警统计
     * @return 库存预警统计列表
     */
    List<StockWarningStatistic> getStockWarningStatistics();

    /**
     * 库存周转率统计
     * @param days 统计天数
     * @return 库存周转率统计列表
     */
    List<StockTurnoverStatistic> getStockTurnoverStatistics(int days);

    /**
     * 热门商品统计
     * @param limit 限制数量
     * @return 热门商品统计列表
     */
    List<HotGoodsStatistic> getHotGoodsStatistics(int limit);

    /**
     * 滞销商品统计
     * @return 滞销商品统计列表
     */
    List<SlowMovingGoodsStatistic> getSlowMovingGoodsStatistics();

    /**
     * 每日库存趋势统计
     * @param days 统计天数
     * @return 每日库存趋势统计列表
     */
    List<DailyStockTrendStatistic> getDailyStockTrendStatistics(int days);

    /**
     * 按仓库统计库存
     * @return 仓库库存统计列表
     */
    List<WarehouseStockStatistic> getWarehouseStockStatistics();

    /**
     * 用户活跃度统计
     * @param days 统计天数
     * @return 用户活跃度统计列表
     */
    List<UserActivityStatistic> getUserActivityStatistics(int days);

    /**
     * 操作类型统计
     * @param days 统计天数
     * @return 操作类型统计列表
     */
    List<OperationTypeStatistic> getOperationTypeStatistics(int days);

    /**
     * 批次剩余库存统计
     * @return 批次剩余库存统计列表
     */
    List<BatchRemainingStatistic> getBatchRemainingStatistics();

    /**
     * 批次过期预警统计
     * @return 批次过期预警统计列表
     */
    List<BatchExpiryStatistic> getBatchExpiryStatistics();

    /**
     * 按分类统计入库
     * @param days 统计天数
     * @return 分类入库统计列表
     */
    List<CategoryInOutStatistic> getCategoryInboundStatistics(int days);

    /**
     * 按分类统计出库
     * @param days 统计天数
     * @return 分类出库统计列表
     */
    List<CategoryInOutStatistic> getCategoryOutboundStatistics(int days);

    /**
     * 每日出入库统计
     * @param days 统计天数
     * @return 每日出入库统计列表
     */
    List<DailyInOutStatistic> getDailyInboundOutboundStatistics(int days);
}
