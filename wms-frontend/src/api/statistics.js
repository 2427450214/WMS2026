
// 统计相关API调用
import request from './request'

// 获取近一周内每个商品入库统计（饼图用）
export const getWeeklyInboundStatistic = async () => {
  return await request('/inboundDetail/getWeeklyInboundStatistic')
}

// 获取近7天每天入库统计（折线图用）
export const getDailyInboundStatistic = async () => {
  return await request('/inboundDetail/getDailyInboundStatistic')
}

// 获取近一周内每个商品出库统计（饼图用）
export const getWeeklyOutboundStatistic = async () => {
  return await request('/outboundDetail/getWeeklyOutboundStatistic')
}

// 获取近7天每天出库统计（折线图用）
export const getDailyOutboundStatistic = async () => {
  return await request('/outboundDetail/getDailyOutboundStatistic')
}

// 获取库存数量最多的前N个商品
export const getTopStockGoods = async (limit = 3) => {
  return await request(`/goods/getTopStockGoods?limit=${limit}`)
}

// 获取已过期的批次
export const getExpiredBatches = async () => {
  return await request('/batch/getExpiredBatches')
}

// 获取接近过期日期一个月的批次
export const getNearExpiryOneMonth = async () => {
  return await request('/batch/nearExpiryOneMonth')
}

// 通过商品编码获取商品信息
export const getGoodsByCode = async (goodsCode) => {
  return await request(`/goods/findByCode?goodsCode=${goodsCode}`)
}

// 按分类统计库存
export const getCategoryStockStatistics = async () => {
  return await request('/statistics/categoryStock')
}

// 库存预警统计
export const getStockWarningStatistics = async () => {
  return await request('/statistics/stockWarning')
}

// 库存周转率统计
export const getStockTurnoverStatistics = async (days = 30) => {
  return await request(`/statistics/stockTurnover?days=${days}`)
}

// 热门商品统计
export const getHotGoodsStatistics = async (limit = 10) => {
  return await request(`/statistics/hotGoods?limit=${limit}`)
}

// 滞销商品统计
export const getSlowMovingGoodsStatistics = async () => {
  return await request('/statistics/slowMovingGoods')
}

// 每日库存趋势统计
export const getDailyStockTrendStatistics = async (days = 30) => {
  return await request(`/statistics/dailyStockTrend?days=${days}`)
}

// 按仓库统计库存
export const getWarehouseStockStatistics = async () => {
  return await request('/statistics/warehouseStock')
}

// 用户活跃度统计
export const getUserActivityStatistics = async (days = 30) => {
  return await request(`/statistics/userActivity?days=${days}`)
}

// 操作类型统计
export const getOperationTypeStatistics = async (days = 30) => {
  return await request(`/statistics/operationType?days=${days}`)
}

// 批次剩余库存统计
export const getBatchRemainingStatistics = async () => {
  return await request('/statistics/batchRemaining')
}

// 批次过期预警统计
export const getBatchExpiryStatistics = async () => {
  return await request('/statistics/batchExpiry')
}

// 按分类统计入库
export const getCategoryInboundStatistics = async (days = 30) => {
  return await request(`/statistics/categoryInbound?days=${days}`)
}

// 按分类统计出库
export const getCategoryOutboundStatistics = async (days = 30) => {
  return await request(`/statistics/categoryOutbound?days=${days}`)
}

// 每日出入库统计
export const getDailyInboundOutboundStatistics = async (days = 30) => {
  return await request(`/statistics/dailyInOut?days=${days}`)
}
