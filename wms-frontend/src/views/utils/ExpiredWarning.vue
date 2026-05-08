<template>
  <div class="expired-container">
    <h3 class="expired-title">过期商品预警</h3>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">当前网络异常</div>
    <div v-else-if="data.length === 0" class="no-data">暂无过期商品</div>
    <div v-else class="expired-list">
      <div v-for="item in data" :key="item.batchNumber" class="expired-item">
        <div class="item-info">
          <span class="goods-code">{{ item.goodsName || item.goodsCode }}</span>
          <span class="batch-number">{{ item.batchNumber }}</span>
        </div>
        <div class="expiry-date">过期日期: {{ formatDate(item.expiryDate) }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { getExpiredBatches, getGoodsByCode } from '../../api/statistics'

export default {
  name: 'ExpiredWarning',
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
        const response = await getExpiredBatches()
        if (response.code === 200) {
          this.data = response.data
          // 为每个商品获取名字
          await this.fetchGoodsNames()
        } else {
          this.error = true
        }
      } catch (err) {
        console.error('获取过期商品失败:', err)
        this.error = true
      } finally {
        this.loading = false
      }
    },
    async fetchGoodsNames() {
      // 并行获取所有商品名字
      const promises = this.data.map(async (item) => {
        try {
          const response = await getGoodsByCode(item.goodsCode)
          if (response.code === 200 && response.data) {
            item.goodsName = response.data.goodsName
          }
        } catch (err) {
          console.error(`获取商品 ${item.goodsCode} 信息失败:`, err)
        }
      })
      await Promise.all(promises)
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toISOString().split('T')[0]
    }
  }
}
</script>

<style scoped>
.expired-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  background: white;
  border-radius: 12px;
  padding: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  box-sizing: border-box;
}

.expired-title {
  margin: 0 0 8px 0;
  font-size: 13px;
  color: #333;
  font-weight: 600;
  flex-shrink: 0;
}

.expired-list {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.expired-item {
  padding: 12px;
  background: #fef2f2;
  border-left: 4px solid #ef4444;
  border-radius: 4px;
}

.item-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.goods-code {
  font-weight: 600;
  color: #333;
}

.batch-number {
  color: #666;
  font-size: 12px;
}

.expiry-date {
  font-size: 13px;
  color: #ef4444;
}

.loading, .error, .no-data {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  font-size: 14px;
}

.error {
  color: #ef4444;
}

.expired-list::-webkit-scrollbar {
  width: 6px;
}

.expired-list::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 3px;
}
</style>
