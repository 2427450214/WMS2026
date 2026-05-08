
// 通用API请求封装
import router from '../router'

const BASE_URL = 'http://localhost:8080'

// 处理401未授权
function handleUnauthorized() {
  // 清除登录状态
  localStorage.removeItem('isLoggedIn')
  localStorage.removeItem('userRole')
  localStorage.removeItem('userName')
  // 跳转到登录页
  router.push('/login')
}

// 通用fetch请求
export async function request(url, options = {}) {
  try {
    const response = await fetch(`${BASE_URL}${url}`, {
      ...options,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        ...options.headers
      }
    })

    // 检查状态码
    if (response.status === 401) {
      handleUnauthorized()
      throw new Error('未授权，请重新登录')
    }

    const data = await response.json()
    return data
  } catch (error) {
    if (error.message === '未授权，请重新登录') {
      throw error
    }
    console.error('API请求失败:', error)
    throw error
  }
}

export default request
