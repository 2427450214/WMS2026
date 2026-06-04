<template>
  <div class="chart-container">
    <h3 class="chart-title">批次剩余库存</h3>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">当前网络异常</div>
    <div v-else-if="data.length === 0" class="no-data">暂无数据</div>
    <div v-else class="batch-list">
      <div v-for="(item, index) in data" :key="index" class="batch-item">
        <div class="batch-info">
          <div class="goods-name">{{ item.goodsName }}</div>
          <div class="batch-no">{{ item.batchNo }}</div>
        </div>
        <div class="batch-detail">
          <span class="remaining">{{ item.remainingQuantity }} / {{ item.batchQuantity }}</span>
          <span class="expiry-date">过期: {{ formatDate(item.expiryDate) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getBatchRemainingStatistics } from '../../api/statistics'

export default {
  name: 'BatchRemaining',
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
        const response = await getBatchRemainingStatistics()
        console.log('BatchRemaining - API响应:', response)
        if (response.code === 200) {
          this.data = response.data
          console.log('BatchRemaining - 数据:', this.data)
          this.loading = false
        } else {
          this.data = []
          this.loading = false
        }
      } catch (err) {
        console.error('获取批次剩余库存数据失败:', err)
        this.error = true
        this.loading = false
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return '无'
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

.batch-list {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.batch-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 10px;
  border-radius: 8px;
  background: #f0f9ff;
  flex-shrink: 0;
}

.batch-info {
  display: flex;
  flex-direction: column;
}

.goods-name {
  font-size: 12px;
  font-weight: 600;
  color: #333;
}

.batch-no {
  font-size: 10px;
  color: #666;
}

.batch-detail {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px;
}

.remaining {
  font-size: 12px;
  font-weight: 600;
  color: #333;
}

.expiry-date {
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
