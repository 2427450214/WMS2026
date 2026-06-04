<template>
  <div class="chart-container">
    <h3 class="chart-title">库存预警统计</h3>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">当前网络异常</div>
    <div v-else-if="data.length === 0" class="no-data">暂无数据</div>
    <div v-else class="warning-list">
      <div v-for="(item, index) in data" :key="index" class="warning-item" :class="item.warningType">
        <div class="warning-info">
          <div class="goods-name">{{ item.goodsName }}</div>
          <div class="goods-code">{{ item.goodsCode }}</div>
        </div>
        <div class="warning-detail">
          <span class="stock-quantity">库存: {{ item.stockQuantity }}</span>
          <span class="warning-range">[{{ item.minStockWarning }} - {{ item.maxStockWarning }}]</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getStockWarningStatistics } from '../../api/statistics'

export default {
  name: 'StockWarning',
  data() {
    return {
      data: [],
      loading: false,
      error: false
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      this.error = false
      try {
        const response = await getStockWarningStatistics()
        console.log('StockWarning - API响应:', response)
        if (response.code === 200) {
          this.data = response.data
          console.log('StockWarning - 数据:', this.data)
          this.loading = false
        } else {
          this.data = []
          this.loading = false
        }
      } catch (err) {
        console.error('获取库存预警数据失败:', err)
        this.error = true
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.chart-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  background: white;
  border-radius: 12px;
  padding: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  overflow: hidden;
}

.chart-title {
  margin: 0 0 5px 0;
  font-size: 13px;
  color: #333;
  font-weight: 600;
  flex-shrink: 0;
}

.warning-list {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.warning-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 10px;
  border-radius: 8px;
  flex-shrink: 0;
}

.warning-item.low {
  background: #fef2f2;
  border-left: 3px solid #ef4444;
}

.warning-item.high {
  background: #fffbeb;
  border-left: 3px solid #f59e0b;
}

.warning-info {
  display: flex;
  flex-direction: column;
}

.goods-name {
  font-size: 12px;
  font-weight: 600;
  color: #333;
}

.goods-code {
  font-size: 10px;
  color: #666;
}

.warning-detail {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px;
}

.stock-quantity {
  font-size: 12px;
  font-weight: 600;
  color: #333;
}

.warning-range {
  font-size: 10px;
  color: #888;
}

.loading, .error, .no-data {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  font-size: 13px;
}

.error {
  color: #ef4444;
}
</style>
