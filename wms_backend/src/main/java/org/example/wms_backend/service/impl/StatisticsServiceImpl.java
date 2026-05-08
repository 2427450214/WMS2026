package org.example.wms_backend.service.impl;

import org.example.wms_backend.entity.*;
import org.example.wms_backend.mapper.*;
import org.example.wms_backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private BatchMapper batchMapper;

    @Autowired
    private InboundDetailMapper inboundDetailMapper;

    @Autowired
    private OutboundDetailMapper outboundDetailMapper;

    @Override
    public List<CategoryStockStatistic> getCategoryStockStatistics() {
        return goodsMapper.selectCategoryStockStatistics();
    }

    @Override
    public List<StockWarningStatistic> getStockWarningStatistics() {
        return goodsMapper.selectStockWarningStatistics();
    }

    @Override
    public List<StockTurnoverStatistic> getStockTurnoverStatistics(int days) {
        return goodsMapper.selectStockTurnoverStatistics(days);
    }

    @Override
    public List<HotGoodsStatistic> getHotGoodsStatistics(int limit) {
        return goodsMapper.selectHotGoodsStatistics(limit);
    }

    @Override
    public List<SlowMovingGoodsStatistic> getSlowMovingGoodsStatistics() {
        return goodsMapper.selectSlowMovingGoodsStatistics();
    }

    @Override
    public List<DailyStockTrendStatistic> getDailyStockTrendStatistics(int days) {
        return goodsMapper.selectDailyStockTrendStatistics(days);
    }

    @Override
    public List<WarehouseStockStatistic> getWarehouseStockStatistics() {
        return warehouseMapper.selectWarehouseStockStatistics();
    }

    @Override
    public List<UserActivityStatistic> getUserActivityStatistics(int days) {
        return operationLogMapper.selectUserActivityStatistics(days);
    }

    @Override
    public List<OperationTypeStatistic> getOperationTypeStatistics(int days) {
        return operationLogMapper.selectOperationTypeStatistics(days);
    }

    @Override
    public List<BatchRemainingStatistic> getBatchRemainingStatistics() {
        return batchMapper.selectBatchRemainingStatistics();
    }

    @Override
    public List<BatchExpiryStatistic> getBatchExpiryStatistics() {
        return batchMapper.selectBatchExpiryStatistics();
    }

    @Override
    public List<CategoryInOutStatistic> getCategoryInboundStatistics(int days) {
        return inboundDetailMapper.selectCategoryInboundStatistics(days);
    }

    @Override
    public List<CategoryInOutStatistic> getCategoryOutboundStatistics(int days) {
        return outboundDetailMapper.selectCategoryOutboundStatistics(days);
    }

    @Override
    public List<DailyInOutStatistic> getDailyInboundOutboundStatistics(int days) {
        return inboundDetailMapper.selectDailyInboundOutboundStatistics(days);
    }
}
