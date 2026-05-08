<template>
  <div class="personal-info-view">
    <h2>个人信息</h2>
    
    <div v-if="loading" class="loading">加载中...</div>
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>

    <div v-if="user && !loading" class="info-card">
      <div class="info-section">
        <div class="info-item">
          <label>账号:</label>
          <span>{{ user.account }}</span>
        </div>
        <div class="info-item">
          <label>姓名:</label>
          <span>{{ user.name }}</span>
        </div>
        <div class="info-item">
          <label>权限等级:</label>
          <span>{{ getPermissionText(user.permissionLevel) }}</span>
        </div>
        <div class="info-item">
          <label>账号状态:</label>
          <span>{{ user.accountStatus }}</span>
        </div>
      </div>

      <div class="edit-section">
        <h3>修改信息</h3>
        <form @submit.prevent="handleUpdate">
          <div class="form-group">
            <label for="name">姓名</label>
            <input
              id="name"
              v-model="editForm.name"
              type="text"
              placeholder="请输入新姓名（2-10位中英文）"
            />
          </div>
          <div class="form-group">
            <label for="oldPassword">旧密码</label>
            <input
              id="oldPassword"
              v-model="editForm.oldPassword"
              type="password"
              placeholder="请输入旧密码"
            />
          </div>
          <div class="form-group">
            <label for="newPassword">新密码</label>
            <input
              id="newPassword"
              v-model="editForm.newPassword"
              type="password"
              placeholder="请输入新密码（8-16位，包含大小写字母、数字、特殊字符）"
            />
          </div>
          <div class="form-group">
            <label for="confirmPassword">确认新密码</label>
            <input
              id="confirmPassword"
              v-model="editForm.confirmPassword"
              type="password"
              placeholder="请再次输入新密码"
            />
          </div>
          <div class="form-actions">
            <button type="submit" :disabled="updating" class="btn-update">
              {{ updating ? '更新中...' : '更新信息' }}
            </button>
            <button type="button" @click="resetForm" class="btn-reset">重置</button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="showPasswordModal" class="modal-overlay" @click.self="closePasswordModal">
      <div class="modal-content">
        <h3>确认身份</h3>
        <p>修改姓名需要验证身份，请输入密码</p>
        <div class="form-group">
          <label for="confirmPasswordInput">密码</label>
          <input
            id="confirmPasswordInput"
            v-model="confirmPasswordInput"
            type="password"
            placeholder="请输入密码"
            @keyup.enter="confirmPasswordUpdate"
          />
        </div>
        <div class="modal-actions">
          <button @click="closePasswordModal" class="btn-cancel">取消</button>
          <button @click="confirmPasswordUpdate" :disabled="updating" class="btn-confirm">
            {{ updating ? '确认中...' : '确认' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getCurrentUserInfo, updateUserInfo } from '../api/user'

export default {
  name: 'PersonalInfoView',
  data() {
    return {
      user: null,
      loading: false,
      updating: false,
      errorMessage: '',
      successMessage: '',
      editForm: {
        name: '',
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      showPasswordModal: false,
      confirmPasswordInput: ''
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    clearMessages() {
      this.errorMessage = ''
      this.successMessage = ''
    },
    async loadUserInfo() {
      this.clearMessages()
      this.loading = true
      try {
        const response = await getCurrentUserInfo()
        if (response.code === 200) {
          this.user = response.data
          this.editForm.name = this.user.name || ''
        } else {
          this.errorMessage = response.message || '加载用户信息失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('加载用户信息错误:', error)
      } finally {
        this.loading = false
      }
    },
    resetForm() {
      this.editForm = {
        name: this.user ? this.user.name : '',
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
      this.clearMessages()
    },
    async handleUpdate() {
      this.clearMessages()
      
      const isNameChanged = this.editForm.name && this.editForm.name !== this.user.name
      const isPasswordChanged = this.editForm.oldPassword && this.editForm.newPassword

      if (!isNameChanged && !isPasswordChanged) {
        this.errorMessage = '请至少填写一项要修改的内容'
        return
      }

      if (isPasswordChanged) {
        if (!this.editForm.oldPassword) {
          this.errorMessage = '修改密码时，旧密码不能为空'
          return
        }
        if (!this.editForm.newPassword) {
          this.errorMessage = '修改密码时，新密码不能为空'
          return
        }
        if (!this.editForm.confirmPassword) {
          this.errorMessage = '请再次输入新密码'
          return
        }
        if (this.editForm.newPassword !== this.editForm.confirmPassword) {
          this.errorMessage = '两次输入的密码不一致'
          return
        }
        if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/.test(this.editForm.newPassword)) {
          this.errorMessage = '新密码必须为8-16位，包含大小写字母、数字和特殊字符'
          return
        }
      }

      if (isNameChanged) {
        if (!/^[\u4e00-\u9fa5a-zA-Z]{2,10}$/.test(this.editForm.name)) {
          this.errorMessage = '姓名只能是2-10位中英文'
          return
        }
      }

      if (isNameChanged && !isPasswordChanged) {
        this.showPasswordModal = true
        return
      }

      await this.doUpdate()
    },
    async confirmPasswordUpdate() {
      if (!this.confirmPasswordInput.trim()) {
        this.errorMessage = '请输入密码'
        return
      }
      this.editForm.oldPassword = this.confirmPasswordInput
      await this.doUpdate()
    },
    async doUpdate() {
      this.updating = true
      this.showPasswordModal = false
      
      try {
        const updateData = {
          account: this.user.account
        }
        if (this.editForm.name && this.editForm.name !== this.user.name) {
          updateData.name = this.editForm.name
        }
        if (this.editForm.oldPassword) {
          updateData.oldPassword = this.editForm.oldPassword
        }
        if (this.editForm.newPassword) {
          updateData.newPassword = this.editForm.newPassword
        }

        const response = await updateUserInfo(updateData)
        if (response.code === 200) {
          this.successMessage = response.message || '信息更新成功'
          if (this.editForm.name && this.editForm.name !== this.user.name) {
            localStorage.setItem('userName', this.editForm.name)
            this.$emit('userNameUpdated', this.editForm.name)
          }
          await this.loadUserInfo()
          this.resetForm()
        } else {
          this.errorMessage = response.message || '信息更新失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('更新用户信息错误:', error)
      } finally {
        this.updating = false
        this.confirmPasswordInput = ''
      }
    },
    closePasswordModal() {
      this.showPasswordModal = false
      this.confirmPasswordInput = ''
      this.clearMessages()
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
.personal-info-view {
  padding: 20px;
}

.personal-info-view h2 {
  margin-bottom: 20px;
  color: #333;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #666;
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

.info-card {
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.info-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.info-item {
  display: flex;
  padding: 10px 0;
  font-size: 15px;
}

.info-item label {
  width: 100px;
  color: #666;
  font-weight: 500;
}

.info-item span {
  color: #333;
}

.edit-section h3 {
  margin-bottom: 20px;
  color: #333;
  font-size: 18px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  max-width: 400px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus {
  outline: none;
  border-color: #4CAF50;
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.btn-update {
  padding: 10px 24px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 15px;
}

.btn-update:hover:not(:disabled) {
  background-color: #45a049;
}

.btn-update:disabled {
  background-color: #a5d6a7;
  cursor: not-allowed;
}

.btn-reset {
  padding: 10px 24px;
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  font-size: 15px;
}

.btn-reset:hover {
  background-color: #e0e0e0;
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
  margin-bottom: 10px;
  color: #333;
}

.modal-content p {
  color: #666;
  margin-bottom: 20px;
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
