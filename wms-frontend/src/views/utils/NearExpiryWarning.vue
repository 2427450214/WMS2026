<template>
  <div class="expired-container">
    <h3 class="expired-title">即将过期商品</h3>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">当前网络异常</div>
    <div v-else-if="data.length === 0" class="no-data">暂无即将过期商品</div>
    <div v-else class="expired-list">
      <div v-for="(item, index) in data" :key="index" class="expired-item">
        <div class="item-info">
          <span class="goods-name">{{ item.goodsName || '未知商品' }}</span>
          <span class="batch-no">{{ item.batchNumber }}</span>
        </div>
        <div class="expiry-date">
          <span class="warning-icon">⚠️</span>
          过期日期: {{ formatDate(item.expiryDate) }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getNearExpiryOneMonth, getGoodsByCode } from '../../api/statistics'

export default {
  name: 'NearExpiryWarning',
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
        const response = await getNearExpiryOneMonth()
        if (response.code === 200) {
          const batches = response.data
          // 获取每个批次的商品信息
          for (const batch of batches) {
            if (batch.goodsCode && !batch.goodsName) {
              try {
                const goodsRes = await getGoodsByCode(batch.goodsCode)
                if (goodsRes.code === 200) {
                  batch.goodsName = goodsRes.data.goodsName
                }
              } catch (e) {
                console.error('获取商品信息失败:', e)
              }
            }
          }
          this.data = batches
        } else {
          this.data = []
        }
        this.loading = false
      } catch (err) {
        console.error('获取即将过期商品失败:', err)
        this.error = true
        this.loading = false
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
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
  background: #fef3c7;
  border-left: 3px solid #f59e0b;
  padding: 8px 10px;
  border-radius: 6px;
}
.item-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}
.goods-name {
  font-size: 13px;
  font-weight: 600;
  color: #92400e;
}
.batch-no {
  font-size: 11px;
  color: #78350f;
  background: rgba(245, 158, 11, 0.2);
  padding: 2px 6px;
  border-radius: 4px;
}
.expiry-date {
  font-size: 12px;
  color: #92400e;
  display: flex;
  align-items: center;
  gap: 4px;
}
.warning-icon {
  font-size: 14px;
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
.no-data {
  color: #9ca3af;
}
</style>
