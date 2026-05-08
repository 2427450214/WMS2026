<template>
  <div class="in-out-log-view">
    <h2>出入库日志</h2>
    
    <div class="search-bar">
      <select v-model="logType" @change="handleLogTypeChange">
        <option value="">全部</option>
        <option value="入库">入库</option>
        <option value="出库">出库</option>
      </select>
      <select v-model="searchType">
        <option value="goodsCode">商品编码</option>
        <option value="warehouseCode">仓库编码</option>
        <option value="userName">用户名称</option>
        <option value="account">账号</option>
      </select>
      <input
        v-model="searchValue"
        type="text"
        placeholder="请输入搜索内容"
        @input="handleSearch"
      />
      <button @click="loadAllLogs(true)">刷新</button>
    </div>

    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
    <div v-if="loading" class="loading">加载中...</div>

    <table v-if="logs.length > 0" class="log-table">
      <thead>
        <tr>
          <th>操作类型</th>
          <th>出入库单号</th>
          <th>商品编码</th>
          <th>仓库编码</th>
          <th>变动前数量</th>
          <th>变动数量</th>
          <th>变动后数量</th>
          <th>用户名称</th>
          <th>账号</th>
          <th>操作时间</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="log in logs" :key="log.id">
          <td :class="{ 'inbound': log.operationType === '入库', 'outbound': log.operationType === '出库' }">
            {{ log.operationType }}
          </td>
          <td>{{ log.inOutDetailCode }}</td>
          <td>{{ log.goodsCode }}</td>
          <td>{{ log.warehouseCode }}</td>
          <td>{{ log.beforeQuantity }}</td>
          <td>{{ log.changeQuantity }}</td>
          <td>{{ log.afterQuantity }}</td>
          <td>{{ log.userName }}</td>
          <td>{{ log.account }}</td>
          <td>{{ formatDate(log.operationTime) }}</td>
        </tr>
      </tbody>
    </table>
    <div v-else-if="!loading && logs.length === 0" class="no-data">暂无出入库日志</div>

    <div v-if="totalPages > 1" class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1">上一页</button>
      <span>第 {{ currentPage }} / {{ totalPages }} 页，共 {{ total }} 条</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages">下一页</button>
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
import {
  getStockChangeLogByPage,
  getStockChangeLogByOperationTypePage,
  getStockChangeLogByGoodsCodePage,
  getStockChangeLogByWarehouseCodePage,
  getStockChangeLogByUserNamePage,
  getStockChangeLogByAccountPage
} from '../api/inOutLog'

export default {
  name: 'InOutLogView',
  data() {
    return {
      logs: [],
      loading: false,
      errorMessage: '',
      successMessage: '',
      logType: '',
      searchType: 'goodsCode',
      searchValue: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      totalPages: 0
    }
  },
  mounted() {
    this.loadAllLogs(true)
  },
  methods: {
    clearMessages() {
      this.errorMessage = ''
      this.successMessage = ''
    },
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },
    async loadAllLogs(resetPage = false) {
      this.clearMessages()
      if (resetPage) {
        this.currentPage = 1
      }
      this.loading = true
      try {
        let response
        
        if (this.searchValue) {
          // 有搜索值，根据搜索类型查询
          switch (this.searchType) {
            case 'goodsCode':
              response = await getStockChangeLogByGoodsCodePage(this.searchValue, this.currentPage, this.pageSize)
              break
            case 'warehouseCode':
              response = await getStockChangeLogByWarehouseCodePage(this.searchValue, this.currentPage, this.pageSize)
              break
            case 'userName':
              response = await getStockChangeLogByUserNamePage(this.searchValue, this.currentPage, this.pageSize)
              break
            case 'account':
              response = await getStockChangeLogByAccountPage(this.searchValue, this.currentPage, this.pageSize)
              break
          }
        } else if (this.logType) {
          // 有操作类型筛选
          response = await getStockChangeLogByOperationTypePage(this.logType, this.currentPage, this.pageSize)
        } else {
          // 没有筛选，查询全部
          response = await getStockChangeLogByPage(this.currentPage, this.pageSize)
        }
        
        if (response.code === 200) {
          this.logs = response.data.records || []
          this.total = response.data.total
          this.totalPages = response.data.totalPages
        } else {
          this.errorMessage = response.message || '加载出入库日志失败'
          this.logs = []
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('加载出入库日志错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleLogTypeChange() {
      this.searchValue = ''
      await this.loadAllLogs(true)
    },
    async handleSearch() {
      await this.loadAllLogs(true)
    },
    async changePage(page) {
      if (page < 1 || page > this.totalPages) return
      this.currentPage = page
      await this.loadAllLogs(false)
    },
    async handlePageSizeChange() {
      this.currentPage = 1
      await this.loadAllLogs(false)
    }
  }
}
</script>

<style scoped>
.in-out-log-view {
  padding: 20px;
}

.in-out-log-view h2 {
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

.log-table {
  width: 100%;
  border-collapse: collapse;
}

.log-table th,
.log-table td {
  padding: 10px 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  font-size: 13px;
}

.log-table th {
  background-color: #f5f5f5;
  font-weight: 500;
  color: #666;
}

.log-table tr:hover {
  background-color: #fafafa;
}

.inbound {
  color: #4CAF50;
  font-weight: bold;
}

.outbound {
  color: #f44336;
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
