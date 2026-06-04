<template>
  <div class="chart-container">
    <h3 class="chart-title">滞销商品</h3>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">当前网络异常</div>
    <div v-else-if="data.length === 0" class="no-data">暂无数据</div>
    <div v-else class="goods-list">
      <div v-for="(item, index) in data" :key="index" class="goods-item">
        <div class="goods-info">
          <div class="goods-name">{{ item.goodsName }}</div>
          <div class="goods-code">{{ item.goodsCode }}</div>
        </div>
        <div class="goods-detail">
          <span class="stock-quantity">库存: {{ item.stockQuantity }}</span>
          <span class="last-outbound">最后出库: {{ formatDate(item.lastOutboundTime) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getSlowMovingGoodsStatistics } from '../../api/statistics'

export default {
  name: 'SlowMovingGoods',
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
        const response = await getSlowMovingGoodsStatistics()
        console.log('SlowMovingGoods - API响应:', response)
        if (response.code === 200) {
          this.data = response.data
          console.log('SlowMovingGoods - 数据:', this.data)
          this.loading = false
        } else {
          this.data = []
          this.loading = false
        }
      } catch (err) {
        console.error('获取滞销商品数据失败:', err)
        this.error = true
        this.loading = false
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return '无记录'
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN')
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

.goods-list {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.goods-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 10px;
  border-radius: 8px;
  background: #f5f5f5;
  flex-shrink: 0;
}

.goods-info {
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

.goods-detail {
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

.last-outbound {
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
