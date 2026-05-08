<template>
  <div class="login-container">
    <div class="login-form" :class="{ 'register-mode': isRegisterMode }">
      <div class="login-header">
        <h1>{{ isRegisterMode ? 'WMS 系统注册' : 'WMS 系统登录' }}</h1>
        <p>{{ isRegisterMode ? '请填写注册信息' : '请输入账号和密码登录系统' }}</p>
      </div>
      

      
      <form v-if="!isRegisterMode" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="account">账号</label>
          <input 
            type="text" 
            id="account" 
            v-model="loginForm.account" 
            placeholder="请输入8-12位数字账号"
          />
        </div>
        
        <div class="form-group password-group">
          <label for="password">密码</label>
          <div class="password-input-container">
            <input 
              :type="showLoginPassword ? 'text' : 'password'" 
              id="password" 
              v-model="loginForm.password" 
              placeholder="请输入密码"
            />
            <button 
              type="button" 
              class="password-toggle" 
              @mousedown="showLoginPassword = true"
              @mouseup="showLoginPassword = false"
              @mouseleave="showLoginPassword = false"
              :class="{ 'showing': showLoginPassword }"
            >
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
            </button>
          </div>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="login-btn" :disabled="loading">
            <span v-if="loading">登录中...</span>
            <span v-else>登录</span>
          </button>
        </div>
        
        <div class="form-switch">
          <span v-if="isRegisterMode" @click="isRegisterMode = false">已有账号？去登录</span>
          <span v-else @click="isRegisterMode = true">还没有账号？去注册</span>
        </div>
        
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
      </form>
      
      <form v-else @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="reg-account">账号</label>
          <input 
            type="text" 
            id="reg-account" 
            v-model="registerForm.account" 
            placeholder="请输入8-12位数字账号"
          />
        </div>
        
        <div class="form-group">
          <label for="reg-name">姓名</label>
          <input 
            type="text" 
            id="reg-name" 
            v-model="registerForm.name" 
            placeholder="请输入姓名"
          />
        </div>
        
        <div class="form-group password-group">
          <label for="reg-password">密码</label>
          <div class="password-input-container">
            <input 
              :type="showRegisterPassword ? 'text' : 'password'" 
              id="reg-password" 
              v-model="registerForm.password" 
              placeholder="请输入密码（8-16位，包含大小写字母、数字和特殊字符）"
            />
            <button 
              type="button" 
              class="password-toggle" 
              @mousedown="showRegisterPassword = true"
              @mouseup="showRegisterPassword = false"
              @mouseleave="showRegisterPassword = false"
              :class="{ 'showing': showRegisterPassword }"
            >
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
            </button>
          </div>
        </div>
        
        <div class="form-group password-group">
          <label for="reg-confirm-password">确认密码</label>
          <div class="password-input-container">
            <input 
              :type="showRegisterConfirmPassword ? 'text' : 'password'" 
              id="reg-confirm-password" 
              v-model="registerForm.confirmPassword" 
              placeholder="请确认密码"
            />
            <button 
              type="button" 
              class="password-toggle" 
              @mousedown="showRegisterConfirmPassword = true"
              @mouseup="showRegisterConfirmPassword = false"
              @mouseleave="showRegisterConfirmPassword = false"
              :class="{ 'showing': showRegisterConfirmPassword }"
            >
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
            </button>
          </div>
        </div>
        
        <div class="form-group">
          <label for="reg-invite-code">邀请码</label>
          <input 
            type="text" 
            id="reg-invite-code" 
            v-model="registerForm.inviteCode" 
            placeholder="请输入邀请码（选填）"
          />
        </div>
        
        <div class="form-actions">
          <button type="submit" class="login-btn" :disabled="loading">
            <span v-if="loading">注册中...</span>
            <span v-else>注册</span>
          </button>
        </div>
        
        <div class="form-switch">
          <span v-if="isRegisterMode" @click="isRegisterMode = false">已有账号？去登录</span>
          <span v-else @click="isRegisterMode = true">还没有账号？去注册</span>
        </div>
        
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { login, register } from '../api/user'
import { validateLoginForm, validateRegisterForm } from '../utils/validation/useValidation'

export default {
  name: 'LoginView',
  data() {
    return {
      isRegisterMode: false,
      loginForm: {
        account: '',
        password: ''
      },
      registerForm: {
        account: '',
        name: '',
        password: '',
        confirmPassword: '',
        inviteCode: ''
      },
      errorMessage: '',
      loading: false,
      showLoginPassword: false,
      showRegisterPassword: false,
      showRegisterConfirmPassword: false
    }
  },
  methods: {
    setErrorMessage(message) {
      this.errorMessage = message
      // 2秒后自动清空错误信息
      setTimeout(() => {
        this.errorMessage = ''
      }, 2000)
    },
    
    async handleLogin() {
      try {
        // 前端校验
        const validation = validateLoginForm(this.loginForm)
        if (!validation.valid) {
          this.setErrorMessage(validation.message)
          return
        }
        
        this.errorMessage = ''
        this.loading = true
        
        // 调用后端登录接口
        const result = await login(this.loginForm.account, this.loginForm.password)
        
        if (result.code === 200) {
          // 登录成功，保存登录状态到 localStorage
          const role = result.data?.role || 'user'
          const userName = result.data?.name || ''
          
          localStorage.setItem('isLoggedIn', 'true')
          localStorage.setItem('userRole', role)
          localStorage.setItem('userName', userName)
          
          // 根据用户角色跳转到对应主页
          if (role === '管理员') {
            this.$router.push('/admin')
          } else {
            this.$router.push('/user')
          }
        } else {
          this.setErrorMessage(result.message || '登录失败，请检查账号和密码')
        }
      } catch (error) {
        this.setErrorMessage('网络错误，请稍后重试')
        console.error('登录错误:', error)
      } finally {
        this.loading = false
      }
    },
    
    async handleRegister() {
      try {
        // 前端校验
        const validation = validateRegisterForm(this.registerForm)
        if (!validation.valid) {
          this.setErrorMessage(validation.message)
          return
        }
        
        this.errorMessage = ''
        this.loading = true
        
        // 调用后端注册接口
        const result = await register(
          this.registerForm.account,
          this.registerForm.name,
          this.registerForm.password,
          this.registerForm.inviteCode
        )
        
        if (result.code === 200) {
          // 注册成功，切换到登录模式
          this.setErrorMessage('注册成功，请登录')
          this.isRegisterMode = false
          // 清空表单
          this.registerForm = {
            account: '',
            name: '',
            password: '',
            confirmPassword: '',
            inviteCode: ''
          }
        } else {
          this.setErrorMessage(result.message || '注册失败，请重试')
        }
      } catch (error) {
        this.setErrorMessage('网络错误，请稍后重试')
        console.error('注册错误:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
@import '../styles/login.css';
</style>