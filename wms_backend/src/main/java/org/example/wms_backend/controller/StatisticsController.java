package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.entity.*;
import org.example.wms_backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 按分类统计库存
     */
    @GetMapping("/categoryStock")
    public Map<String, Object> getCategoryStockStatistics() {
        List<CategoryStockStatistic> statistics = statisticsService.getCategoryStockStatistics();
        return ResponseResult.success(statistics);
    }

    /**
     * 库存预警统计
     */
    @GetMapping("/stockWarning")
    public Map<String, Object> getStockWarningStatistics() {
        List<StockWarningStatistic> statistics = statisticsService.getStockWarningStatistics();
        return ResponseResult.success(statistics);
    }

    /**
     * 库存周转率统计
     */
    @GetMapping("/stockTurnover")
    public Map<String, Object> getStockTurnoverStatistics(@RequestParam(defaultValue = "30") int days) {
        List<StockTurnoverStatistic> statistics = statisticsService.getStockTurnoverStatistics(days);
        return ResponseResult.success(statistics);
    }

    /**
     * 热门商品统计
     */
    @GetMapping("/hotGoods")
    public Map<String, Object> getHotGoodsStatistics(@RequestParam(defaultValue = "10") int limit) {
        List<HotGoodsStatistic> statistics = statisticsService.getHotGoodsStatistics(limit);
        return ResponseResult.success(statistics);
    }

    /**
     * 滞销商品统计
     */
    @GetMapping("/slowMovingGoods")
    public Map<String, Object> getSlowMovingGoodsStatistics() {
        List<SlowMovingGoodsStatistic> statistics = statisticsService.getSlowMovingGoodsStatistics();
        return ResponseResult.success(statistics);
    }

    /**
     * 每日库存趋势统计
     */
    @GetMapping("/dailyStockTrend")
    public Map<String, Object> getDailyStockTrendStatistics(@RequestParam(defaultValue = "30") int days) {
        List<DailyStockTrendStatistic> statistics = statisticsService.getDailyStockTrendStatistics(days);
        return ResponseResult.success(statistics);
    }

    /**
     * 按仓库统计库存
     */
    @GetMapping("/warehouseStock")
    public Map<String, Object> getWarehouseStockStatistics() {
        List<WarehouseStockStatistic> statistics = statisticsService.getWarehouseStockStatistics();
        return ResponseResult.success(statistics);
    }

    /**
     * 用户活跃度统计
     */
    @GetMapping("/userActivity")
    public Map<String, Object> getUserActivityStatistics(@RequestParam(defaultValue = "30") int days) {
        List<UserActivityStatistic> statistics = statisticsService.getUserActivityStatistics(days);
        return ResponseResult.success(statistics);
    }

    /**
     * 操作类型统计
     */
    @GetMapping("/operationType")
    public Map<String, Object> getOperationTypeStatistics(@RequestParam(defaultValue = "30") int days) {
        List<OperationTypeStatistic> statistics = statisticsService.getOperationTypeStatistics(days);
        return ResponseResult.success(statistics);
    }

    /**
     * 批次剩余库存统计
     */
    @GetMapping("/batchRemaining")
    public Map<String, Object> getBatchRemainingStatistics() {
        List<BatchRemainingStatistic> statistics = statisticsService.getBatchRemainingStatistics();
        return ResponseResult.success(statistics);
    }

    /**
     * 批次过期预警统计
     */
    @GetMapping("/batchExpiry")
    public Map<String, Object> getBatchExpiryStatistics() {
        List<BatchExpiryStatistic> statistics = statisticsService.getBatchExpiryStatistics();
        return ResponseResult.success(statistics);
    }

    /**
     * 按分类统计入库
     */
    @GetMapping("/categoryInbound")
    public Map<String, Object> getCategoryInboundStatistics(@RequestParam(defaultValue = "30") int days) {
        List<CategoryInOutStatistic> statistics = statisticsService.getCategoryInboundStatistics(days);
        return ResponseResult.success(statistics);
    }

    /**
     * 按分类统计出库
     */
    @GetMapping("/categoryOutbound")
    public Map<String, Object> getCategoryOutboundStatistics(@RequestParam(defaultValue = "30") int days) {
        List<CategoryInOutStatistic> statistics = statisticsService.getCategoryOutboundStatistics(days);
        return ResponseResult.success(statistics);
    }

    /**
     * 每日出入库统计
     */
    @GetMapping("/dailyInOut")
    public Map<String, Object> getDailyInboundOutboundStatistics(@RequestParam(defaultValue = "30") int days) {
        List<DailyInOutStatistic> statistics = statisticsService.getDailyInboundOutboundStatistics(days);
        return ResponseResult.success(statistics);
    }
}
