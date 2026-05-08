<template>
  <div class="permission-manage-view">
    <h2>权限管理</h2>
    
    <div class="search-bar">
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

    <table v-if="users.length > 0" class="user-table">
      <thead>
        <tr>
          <th>账号</th>
          <th>姓名</th>
          <th>当前权限</th>
          <th>账号状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.account">
          <td>{{ user.account }}</td>
          <td>{{ user.name }}</td>
          <td>{{ getPermissionText(user.permissionLevel) }}</td>
          <td>{{ user.accountStatus }}</td>
          <td>
            <button v-if="user.permissionLevel !== 1" @click="handleSetAdmin(user)" class="btn-admin">
              设为管理员
            </button>
            <button v-else @click="handleRemoveAdmin(user)" class="btn-remove-admin">
              取消管理员
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else-if="!loading" class="no-data">暂无用户数据</div>

    <!-- 最高权限码输入弹窗 -->
    <div v-if="showInviteModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>请输入最高权限码</h3>
        <div class="form-group">
          <input
            v-model="inviteCode"
            type="text"
            placeholder="请输入最高权限码"
            @keyup.enter="confirmPermission"
          />
        </div>
        <div class="modal-actions">
          <button @click="closeModal" class="btn-cancel">取消</button>
          <button @click="confirmPermission" class="btn-confirm" :disabled="loading">
            {{ loading ? '处理中...' : '确认' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessageBox } from 'element-plus'
import { findAllUsers, findUsersByPermissionLevel } from '../api/user'

export default {
  name: 'PermissionManageView',
  data() {
    return {
      users: [],
      loading: false,
      errorMessage: '',
      successMessage: '',
      searchPermission: '',
      showInviteModal: false,
      inviteCode: '',
      pendingUser: null,
      pendingPermission: null
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
      this.loading = true
      try {
        const response = await findAllUsers()
        if (response.code === 200) {
          this.users = response.data
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
    async searchByPermission() {
      if (!this.searchPermission) {
        await this.loadAllUsers()
        return
      }
      this.clearMessages()
      this.loading = true
      try {
        const response = await findUsersByPermissionLevel(parseInt(this.searchPermission))
        if (response.code === 200) {
          this.users = response.data
        } else {
          this.errorMessage = response.message || '搜索失败'
          this.users = []
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('搜索用户错误:', error)
      } finally {
        this.loading = false
      }
    },
    handleSetAdmin(user) {
      this.pendingUser = user
      this.pendingPermission = 1
      this.inviteCode = ''
      this.showInviteModal = true
    },
    handleRemoveAdmin(user) {
      this.pendingUser = user
      this.pendingPermission = 0
      this.inviteCode = ''
      this.showInviteModal = true
    },
    closeModal() {
      this.showInviteModal = false
      this.pendingUser = null
      this.pendingPermission = null
      this.inviteCode = ''
    },
    async confirmPermission() {
      if (!this.inviteCode.trim()) {
        this.errorMessage = '请输入最高权限码'
        return
      }
      this.loading = true
      try {
        const response = await fetch('http://localhost:8080/user/updatePermission', {
          method: 'PUT',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: new URLSearchParams({
            account: this.pendingUser.account,
            permissionLevel: this.pendingPermission.toString(),
            inviteCode: this.inviteCode
          })
        })
        const data = await response.json()
        if (data.code === 200) {
          this.successMessage = data.message || '权限修改成功'
          await this.loadAllUsers()
          this.closeModal()
        } else {
          this.errorMessage = data.message || '权限修改失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('修改权限错误:', error)
      } finally {
        this.loading = false
      }
    },
    getPermissionText(level) {
      if (level === 1) return '管理员'
      if (level === 0) return '普通用户'
      return level
    }
  }
}
</script>

<style scoped>
.permission-manage-view {
  padding: 20px;
}

.permission-manage-view h2 {
  margin-bottom: 20px;
  color: #333;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

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

.btn-admin {
  padding: 6px 12px;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-admin:hover {
  background-color: #1976D2;
}

.btn-remove-admin {
  padding: 6px 12px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-remove-admin:hover {
  background-color: #d32f2f;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  padding: 24px;
  width: 90%;
  max-width: 400px;
}

.modal-content h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus {
  outline: none;
  border-color: #4CAF50;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

.btn-cancel {
  padding: 8px 20px;
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel:hover {
  background-color: #e0e0e0;
}

.btn-confirm {
  padding: 8px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-confirm:hover:not(:disabled) {
  background-color: #45a049;
}

.btn-confirm:disabled {
  background-color: #a5d6a7;
  cursor: not-allowed;
}
</style>
