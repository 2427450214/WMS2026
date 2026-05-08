<template>
  <div class="operation-log-view">
    <h2>系统操作日志</h2>
    
    <div class="search-bar">
      <select v-model="searchType">
        <option value="">全部</option>
        <option value="operationType">按操作类型</option>
        <option value="account">按用户账号</option>
      </select>
      <input
        v-if="searchType"
        v-model="searchValue"
        type="text"
        :placeholder="searchType === 'operationType' ? '请输入操作类型' : '请输入用户账号'"
        @input="handleSearch"
      />
      <button @click="loadAllLogs">刷新</button>
    </div>

    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <div v-if="loading" class="loading">加载中...</div>

    <table v-if="pageData.records && pageData.records.length > 0" class="log-table">
      <thead>
        <tr>
          <th>用户账号</th>
          <th>操作类型</th>
          <th>操作详情</th>
          <th>操作时间</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="log in pageData.records" :key="log.id">
          <td>{{ log.account }}</td>
          <td>{{ log.operationType }}</td>
          <td>{{ log.operationDetail }}</td>
          <td>{{ formatDate(log.createTime) }}</td>
        </tr>
      </tbody>
    </table>
    <div v-else-if="!loading" class="no-data">暂无操作日志数据</div>

    <div v-if="pageData.totalPages && pageData.totalPages > 1" class="pagination">
      <button @click="changePage(pageData.page - 1)" :disabled="pageData.page <= 1">上一页</button>
      <span>第 {{ pageData.page }} / {{ pageData.totalPages }} 页，共 {{ pageData.total }} 条</span>
      <button @click="changePage(pageData.page + 1)" :disabled="pageData.page >= pageData.totalPages">下一页</button>
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
import { getOperationLogByPage, getOperationLogByOperationTypePage, getOperationLogByAccountPage } from '../api/operationLog'

export default {
  name: 'OperationLogView',
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
      searchType: '',
      searchValue: '',
      searchTimer: null
    }
  },
  mounted() {
    this.loadAllLogs()
  },
  methods: {
    async loadAllLogs() {
      this.loading = true
      this.errorMessage = ''
      this.searchType = ''
      this.searchValue = ''
      
      try {
        const response = await getOperationLogByPage(this.pageData.page, this.pageSize)
        console.log('操作日志响应:', response)
        if (response.code === 200 && response.data) {
          this.pageData = response.data
          console.log('操作日志数据:', this.pageData.records)
        } else {
          this.errorMessage = response.message || '获取操作日志失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('获取操作日志错误:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      if (this.searchTimer) {
        clearTimeout(this.searchTimer)
      }
      this.searchTimer = setTimeout(() => {
        this.searchLogs()
      }, 300)
    },
    async searchLogs() {
      if (!this.searchValue.trim()) {
        this.pageData.page = 1
        this.loadAllLogs()
        return
      }
      
      this.loading = true
      this.errorMessage = ''
      
      try {
        let response
        if (this.searchType === 'operationType') {
          response = await getOperationLogByOperationTypePage(this.searchValue, this.pageData.page, this.pageSize)
        } else if (this.searchType === 'account') {
          response = await getOperationLogByAccountPage(this.searchValue, this.pageData.page, this.pageSize)
        }
        
        if (response.code === 200 && response.data) {
          this.pageData = response.data
        } else {
          this.errorMessage = response.message || '搜索失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('搜索操作日志错误:', error)
      } finally {
        this.loading = false
      }
    },
    changePage(page) {
      this.pageData.page = page
      if (this.searchValue && this.searchType) {
        this.searchLogs()
      } else {
        this.loadAllLogs()
      }
    },
    handlePageSizeChange() {
      this.pageData.page = 1
      this.pageData.pageSize = this.pageSize
      if (this.searchValue && this.searchType) {
        this.searchLogs()
      } else {
        this.loadAllLogs()
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      // 检查日期是否有效
      if (isNaN(date.getTime())) {
        return ''
      }
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.operation-log-view {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100%;
}

.operation-log-view h2 {
  margin: 0 0 20px 0;
  color: #262626;
  font-size: 24px;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  align-items: center;
}

.search-bar select,
.search-bar input {
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
}

.search-bar select {
  min-width: 120px;
}

.search-bar input {
  flex: 1;
  min-width: 200px;
}

.search-bar button {
  padding: 8px 20px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.search-bar button:hover {
  background-color: #40a9ff;
}

.error-message {
  padding: 12px;
  background-color: #fff2f0;
  border: 1px solid #ffccc7;
  border-radius: 4px;
  color: #ff4d4f;
  margin-bottom: 20px;
}

.loading,
.no-data {
  text-align: center;
  padding: 40px;
  color: #666;
  font-size: 14px;
}

.log-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.log-table thead {
  background-color: #fafafa;
}

.log-table th {
  padding: 14px 16px;
  text-align: left;
  font-weight: 600;
  color: #262626;
  border-bottom: 1px solid #e8e8e8;
  font-size: 14px;
}

.log-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  color: #595959;
  font-size: 14px;
}

.log-table tbody tr:hover {
  background-color: #fafafa;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-top: 20px;
  padding: 15px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.pagination button {
  padding: 8px 16px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.pagination button:hover:not(:disabled) {
  background-color: #1890ff;
  color: white;
  border-color: #1890ff;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.pagination select {
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
}

.pagination span {
  color: #666;
  font-size: 14px;
}
</style>
