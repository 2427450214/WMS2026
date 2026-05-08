// 用户相关 API 调用

// 登录 API
export const login = async (account, password) => {
  try {
    const response = await fetch('http://localhost:8080/user/login', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({
        account,
        password
      })
    })
    
    return await response.json()
  } catch (error) {
    console.error('登录错误:', error)
    throw error
  }
}

// 注册 API
export const register = async (account, name, password, inviteCode) => {
  try {
    const response = await fetch('http://localhost:8080/user/register', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({
        account,
        name,
        password,
        inviteCode
      })
    })
    
    return await response.json()
  } catch (error) {
    console.error('注册错误:', error)
    throw error
  }
}

// 获取当前用户信息 API
export const getCurrentUserInfo = async () => {
  try {
    const response = await fetch('http://localhost:8080/user/info', {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('获取用户信息错误:', error)
    throw error
  }
}

// 查询所有用户 API (管理员)
export const findAllUsers = async () => {
  try {
    const response = await fetch('http://localhost:8080/user/findAll', {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询用户错误:', error)
    throw error
  }
}

// 根据账号查询用户 API
export const findUserByAccount = async (account) => {
  try {
    const response = await fetch(`http://localhost:8080/user/findByAccount?account=${encodeURIComponent(account)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询用户错误:', error)
    throw error
  }
}

// 根据姓名查询用户 API (管理员)
export const findUsersByName = async (name) => {
  try {
    const response = await fetch(`http://localhost:8080/user/findByName?name=${encodeURIComponent(name)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询用户错误:', error)
    throw error
  }
}

// 根据账号状态查询用户 API (管理员)
export const findUsersByStatus = async (accountStatus) => {
  try {
    const response = await fetch(`http://localhost:8080/user/findByStatus?accountStatus=${encodeURIComponent(accountStatus)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询用户错误:', error)
    throw error
  }
}

// 根据权限等级查询用户 API (管理员)
export const findUsersByPermissionLevel = async (permissionLevel) => {
  try {
    const response = await fetch(`http://localhost:8080/user/findByPermissionLevel?permissionLevel=${permissionLevel}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询用户错误:', error)
    throw error
  }
}

// 修改用户状态 API (管理员)
export const updateUserStatus = async (account) => {
  try {
    const response = await fetch('http://localhost:8080/user/updateStatus', {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({
        account
      })
    })
    
    return await response.json()
  } catch (error) {
    console.error('修改用户状态错误:', error)
    throw error
  }
}

// 更新用户信息 API
export const updateUserInfo = async (userUpdateDTO) => {
  try {
    const response = await fetch('http://localhost:8080/user/update', {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams(userUpdateDTO)
    })
    
    return await response.json()
  } catch (error) {
    console.error('更新用户信息错误:', error)
    throw error
  }
}

// 退出登录 API
export const logout = async () => {
  try {
    const response = await fetch('http://localhost:8080/user/logout', {
      method: 'POST',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('退出登录错误:', error)
    throw error
  }
}

// 分页查询用户 API (管理员)
export const getUserByPage = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`http://localhost:8080/user/page?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询用户错误:', error)
    throw error
  }
}

// 按姓名模糊查询分页 API (管理员)
export const searchUserByPage = async (name, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`http://localhost:8080/user/searchPage?name=${encodeURIComponent(name)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询用户错误:', error)
    throw error
  }
}
