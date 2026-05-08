<template>
  <div class="batch-manage-view">
    <h2>批次管理</h2>
    
    <div class="search-bar">
      <select v-model="searchType">
        <option value="goodsCode">按商品编码</option>
        <option value="batchNo">按批次号</option>
        <option value="warehouseCode">按仓库编码</option>
      </select>
      <input
        v-model="searchValue"
        type="text"
        :placeholder="getPlaceholder"
        @input="handleSearch"
      />
      <select v-model="statusFilter" @change="handleStatusFilter">
        <option value="">全部状态</option>
        <option value="1">正常</option>
        <option value="2">已过期</option>
        <option value="3">即将过期</option>
      </select>
      <button @click="loadAllBatches(true)">刷新</button>
    </div>

    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
    <div v-if="loading" class="loading">加载中...</div>

    <table v-if="pageData.records && pageData.records.length > 0" class="batch-table">
      <thead>
        <tr>
          <th>批次号</th>
          <th>商品编码</th>
          <th>商品名称</th>
          <th>仓库编码</th>
          <th>数量</th>
          <th>生产日期</th>
          <th>到期日期</th>
          <th>状态</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="batch in pageData.records" :key="batch.id">
          <td>{{ batch.batchNumber }}</td>
          <td>{{ batch.goodsCode }}</td>
          <td>{{ batch.goodsName }}</td>
          <td>{{ batch.warehouseCode }}</td>
          <td>{{ batch.remainingQuantity }}</td>
          <td>{{ formatDate(batch.productionDate) }}</td>
          <td :class="{ 'expired': isExpired(batch.expiryDate), 'near-expiry': isNearExpiry(batch.expiryDate) }">
            {{ formatDate(batch.expiryDate) }}
          </td>
          <td :class="{ 'status-normal': batch.status === '正常', 'status-expired': batch.status === '过期' }">
            {{ batch.status }}
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else-if="!loading && (!pageData.records || pageData.records.length === 0)" class="no-data">暂无批次数据</div>

    <div class="pagination">
      <button @click="changePage(1)" :disabled="pageData.page <= 1">首页</button>
      <button @click="changePage(pageData.page - 1)" :disabled="pageData.page <= 1">上一页</button>
      <span>第 {{ pageData.page }} 页，共 {{ pageData.totalPages }} 页</span>
      <button @click="changePage(pageData.page + 1)" :disabled="pageData.page >= pageData.totalPages">下一页</button>
      <button @click="changePage(pageData.totalPages)" :disabled="pageData.page >= pageData.totalPages">末页</button>
      <select v-model="pageSize" @change="handlePageSizeChange">
        <option :value="5">5条/页</option>
        <option :value="10" selected>10条/页</option>
        <option :value="20">20条/页</option>
        <option :value="50">50条/页</option>
      </select>
    </div>
  </div>
</template>

<script>
import { findAllBatches, getBatchByGoodsCodePage, getBatchByWarehouseCodePage, getBatchByStatusPage, getBatchByNearExpiryPage, getBatchByExpiredPage } from '../api/batch'

export default {
  name: 'BatchManageView',
  data() {
    return {
      pageData: {
        records: [],
        total: 0,
        page: 1,
        pageSize: 10,
        totalPages: 0
      },
      pageSize: 10,
      loading: false,
      errorMessage: '',
      successMessage: '',
      searchType: 'goodsCode',
      searchValue: '',
      statusFilter: ''
    }
  },
  mounted() {
    this.loadAllBatches(true)
  },
  computed: {
    getPlaceholder() {
      switch (this.searchType) {
        case 'goodsCode':
          return '请输入商品编码'
        case 'batchNo':
          return '请输入批次号'
        case 'warehouseCode':
          return '请输入仓库编码'
        default:
          return '请输入搜索内容'
      }
    }
  },
  methods: {
    clearMessages() {
      this.errorMessage = ''
      this.successMessage = ''
    },
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString()
    },
    isExpired(expiryDate) {
      if (!expiryDate) return false
      const today = new Date()
      const expiry = new Date(expiryDate)
      return expiry < today
    },
    isNearExpiry(expiryDate) {
      if (!expiryDate) return false
      const today = new Date()
      const expiry = new Date(expiryDate)
      const diffTime = expiry - today
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      return diffDays <= 30 && diffDays > 0
    },
    async loadAllBatches(resetPage = false) {
      this.clearMessages()
      this.searchValue = ''
      this.statusFilter = ''
      if (resetPage) {
        this.pageData.page = 1
      }
      this.loading = true
      try {
        const response = await findAllBatches(this.pageData.page, this.pageSize)
        if (response.code === 200) {
          this.pageData = response.data
        } else {
          this.errorMessage = response.message || '加载批次失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('加载批次错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleSearch() {
      if (!this.searchValue.trim()) {
        await this.loadAllBatches(true)
        return
      }
      this.clearMessages()
      this.statusFilter = ''
      this.pageData.page = 1
      this.loading = true
      try {
        if (this.searchType === 'goodsCode') {
          const response = await getBatchByGoodsCodePage(this.searchValue, this.pageData.page, this.pageSize)
          if (response.code === 200) {
            this.pageData = response.data
          } else {
            this.errorMessage = response.message || '搜索失败'
            this.pageData.records = []
          }
        } else if (this.searchType === 'warehouseCode') {
          const response = await getBatchByWarehouseCodePage(this.searchValue, this.pageData.page, this.pageSize)
          if (response.code === 200) {
            this.pageData = response.data
          } else {
            this.errorMessage = response.message || '搜索失败'
            this.pageData.records = []
          }
        } else {
          // 其他搜索类型的处理
          await this.loadAllBatches(true)
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('搜索批次错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleStatusFilter() {
      if (!this.statusFilter) {
        await this.loadAllBatches()
        return
      }
      this.clearMessages()
      this.searchValue = ''
      this.pageData.page = 1
      this.loading = true
      try {
        let response
        if (this.statusFilter === '2') {
          // 已过期
          response = await getBatchByExpiredPage(this.pageData.page, this.pageSize)
        } else if (this.statusFilter === '3') {
          // 即将过期
          response = await getBatchByNearExpiryPage(this.pageData.page, this.pageSize)
        } else {
          // 正常
          response = await getBatchByStatusPage('正常', this.pageData.page, this.pageSize)
        }
        if (response.code === 200) {
          this.pageData = response.data
        } else {
          this.errorMessage = response.message || '筛选失败'
          this.pageData.records = []
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('筛选批次错误:', error)
      } finally {
        this.loading = false
      }
    },
    async changePage(page) {
      if (page < 1 || page > this.pageData.totalPages) return
      this.pageData.page = page
      if (this.searchValue && this.searchType === 'goodsCode') {
        await this.handleSearch()
      } else if (this.statusFilter) {
        await this.handleStatusFilter()
      } else {
        await this.loadAllBatches()
      }
    },
    async handlePageSizeChange() {
      this.pageData.page = 1
      this.pageData.pageSize = this.pageSize
      if (this.searchValue && this.searchType === 'goodsCode') {
        await this.handleSearch()
      } else if (this.statusFilter) {
        await this.handleStatusFilter()
      } else {
        await this.loadAllBatches()
      }
    }
  }
}
</script>

<style scoped>
.batch-manage-view {
  padding: 20px;
}

.batch-manage-view h2 {
  margin-bottom: 20px;
  color: #333;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-bar select,
.search-bar input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.search-bar button {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-bar button:hover {
  background-color: #45a049;
}

.error-message {
  color: #f44336;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #ffebee;
  border-radius: 4px;
}

.success-message {
  color: #4CAF50;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #e8f5e9;
  border-radius: 4px;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.batch-table {
  width: 100%;
  border-collapse: collapse;
}

.batch-table th,
.batch-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  font-size: 14px;
}

.batch-table th {
  background-color: #f5f5f5;
  font-weight: 500;
  color: #666;
}

.batch-table tr:hover {
  background-color: #fafafa;
}

.expired {
  color: #f44336;
  font-weight: bold;
}

.near-expiry {
  color: #ff9800;
  font-weight: bold;
}

.status-normal {
  color: #4CAF50;
  font-weight: bold;
}

.status-expired {
  color: #f44336;
  font-weight: bold;
}

.status-near-expiry {
  color: #ff9800;
  font-weight: bold;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 15px;
}

.pagination button {
  padding: 8px 16px;
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:hover:not(:disabled) {
  background-color: #4CAF50;
  color: white;
  border-color: #4CAF50;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
</style>