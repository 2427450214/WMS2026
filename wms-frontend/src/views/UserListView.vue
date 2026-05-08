<template>
  <div class="user-list-view">
    <h2>查看用户</h2>
    
    <div class="search-bar">
      <select v-model="searchType">
        <option value="account">按账号</option>
        <option value="name">按姓名</option>
      </select>
      <input
        v-model="searchValue"
        type="text"
        :placeholder="searchType === 'account' ? '请输入账号' : '请输入姓名'"
        @input="handleSearch"
      />
      <select v-model="searchStatus" @change="searchByStatus">
        <option value="">全部状态</option>
        <option value="启用">启用</option>
        <option value="封禁">封禁</option>
      </select>
      <select v-model="searchPermission" @change="searchByPermission">
        <option value="">全部权限</option>
        <option value="1">管理员</option>
        <option value="0">普通用户</option>
      </select>
      <button @click="loadAllUsers">刷新</button>
    </div>

    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
    <div v-if="loading" class="loading">加载中...</div>

    <table v-if="pageData.records && pageData.records.length > 0" class="user-table">
      <thead>
        <tr>
          <th>账号</th>
          <th>姓名</th>
          <th>权限等级</th>
          <th>账号状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in pageData.records" :key="user.account">
          <td>{{ user.account }}</td>
          <td>{{ user.name }}</td>
          <td>{{ getPermissionText(user.permissionLevel) }}</td>
          <td>{{ user.accountStatus }}</td>
          <td>
            <button @click="handleUpdateStatus(user)" class="btn-status">
              {{ user.isDeleted === 1 ? '恢复' : '禁用' }}
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else-if="!loading" class="no-data">暂无用户数据</div>

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
import { ElMessageBox } from 'element-plus'
import { findAllUsers, findUsersByName, findUsersByStatus, findUsersByPermissionLevel, updateUserStatus, getUserByPage, searchUserByPage } from '../api/user'

export default {
  name: 'UserListView',
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
      searchType: 'account',
      searchValue: '',
      searchStatus: '',
      searchPermission: ''
    }
  },
  mounted() {
    this.loadAllUsers()
  },
  methods: {
    clearMessages() {
      this.errorMessage = ''
      this.successMessage = ''
    },
    async loadAllUsers() {
      this.clearMessages()
      this.searchValue = ''
      this.searchStatus = ''
      this.searchPermission = ''
      this.pageData.page = 1
      this.loading = true
      try {
        const response = await getUserByPage(this.pageData.page, this.pageSize)
        if (response.code === 200) {
          this.pageData = response.data
        } else {
          this.errorMessage = response.message || '加载用户失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('加载用户错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleSearch() {
      if (!this.searchValue.trim()) {
        await this.loadAllUsers()
        return
      }
      this.clearMessages()
      this.searchStatus = ''
      this.searchPermission = ''
      this.pageData.page = 1
      this.loading = true
      try {
        if (this.searchType === 'account') {
          const response = await fetch(`http://localhost:8080/user/findByAccount?account=${encodeURIComponent(this.searchValue)}`, {
            method: 'GET',
            credentials: 'include'
          })
          const data = await response.json()
          if (data.code === 200) {
            this.pageData.records = [data.data]
            this.pageData.total = 1
            this.pageData.totalPages = 1
          } else {
            this.errorMessage = data.message || '搜索失败'
            this.pageData.records = []
          }
        } else if (this.searchType === 'name') {
          const response = await searchUserByPage(this.searchValue, this.pageData.page, this.pageSize)
          if (response.code === 200) {
            this.pageData = response.data
          } else {
            this.errorMessage = response.message || '搜索失败'
            this.pageData.records = []
          }
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('搜索用户错误:', error)
      } finally {
        this.loading = false
      }
    },
    async searchByStatus() {
      if (!this.searchStatus) {
        await this.loadAllUsers()
        return
      }
      this.clearMessages()
      this.searchValue = ''
      this.searchPermission = ''
      this.pageData.page = 1
      this.loading = true
      try {
        const response = await findUsersByStatus(this.searchStatus)
        if (response.code === 200) {
          this.pageData.records = response.data
          this.pageData.total = response.data.length
          this.pageData.totalPages = 1
        } else {
          this.errorMessage = response.message || '搜索失败'
          this.pageData.records = []
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('搜索用户错误:', error)
      } finally {
        this.loading = false
      }
    },
    async searchByPermission() {
      if (!this.searchPermission) {
        await this.loadAllUsers()
        return
      }
      this.clearMessages()
      this.searchValue = ''
      this.searchStatus = ''
      this.pageData.page = 1
      this.loading = true
      try {
        const response = await findUsersByPermissionLevel(parseInt(this.searchPermission))
        if (response.code === 200) {
          this.pageData.records = response.data
          this.pageData.total = response.data.length
          this.pageData.totalPages = 1
        } else {
          this.errorMessage = response.message || '搜索失败'
          this.pageData.records = []
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('搜索用户错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleUpdateStatus(user) {
      const action = user.isDeleted === 1 ? '恢复' : '禁用'
      try {
        await ElMessageBox.confirm(
          `确定要${action}用户 ${user.name}（账号：${user.account}）吗？`,
          '确认操作',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        // 确认后执行
        this.clearMessages()
        this.loading = true
        try {
          const response = await updateUserStatus(user.account)
          if (response.code === 200) {
            this.successMessage = response.message || '状态修改成功'
            await this.loadAllUsers()
          } else {
            this.errorMessage = response.message || '状态修改失败'
          }
        } catch (error) {
          this.errorMessage = '网络错误，请稍后重试'
          console.error('修改用户状态错误:', error)
        } finally {
          this.loading = false
        }
      } catch {
        // 用户取消，不做任何处理
      }
    },
    getPermissionText(level) {
      if (level === 1) return '管理员'
      if (level === 0) return '普通用户'
      return level
    },
    async changePage(page) {
      if (page < 1 || page > this.pageData.totalPages) return
      this.pageData.page = page
      if (this.searchValue && this.searchType === 'name') {
        await this.handleSearch()
      } else {
        await this.loadAllUsers()
      }
    },
    async handlePageSizeChange() {
      this.pageData.page = 1
      this.pageData.pageSize = this.pageSize
      if (this.searchValue && this.searchType === 'name') {
        await this.handleSearch()
      } else {
        await this.loadAllUsers()
      }
    }
  }
}
</script>

<style scoped>
.user-list-view {
  padding: 20px;
}

.user-list-view h2 {
  margin-bottom: 20px;
  color: #333;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-bar input,
.search-bar select {
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

.user-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

.user-table th,
.user-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.user-table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.user-table tr:hover {
  background-color: #fafafa;
}

.btn-status {
  padding: 6px 12px;
  background-color: #ff9800;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-status:hover {
  background-color: #e68a00;
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
