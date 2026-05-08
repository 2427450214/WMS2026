// 仓库相关 API 调用

const BASE_URL = 'http://localhost:8080'

// 新增仓库 API
export const addWarehouse = async (warehouseDTO) => {
  try {
    // 过滤掉 null 或 undefined 的字段
    const filteredData = {}
    for (const [key, value] of Object.entries(warehouseDTO)) {
      if (value !== null && value !== undefined) {
        filteredData[key] = value
      }
    }
    const response = await fetch(`${BASE_URL}/warehouse/add`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams(filteredData)
    })
    
    return await response.json()
  } catch (error) {
    console.error('新增仓库错误:', error)
    throw error
  }
}

// 查询所有仓库 API
export const findAllWarehouses = async () => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/findAll`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询仓库错误:', error)
    throw error
  }
}

// 根据仓库编码查询仓库 API
export const findWarehouseByCode = async (warehouseCode) => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/findByCode?warehouseCode=${encodeURIComponent(warehouseCode)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询仓库错误:', error)
    throw error
  }
}

// 根据仓库状态查询仓库 API
export const findWarehouseByStatus = async (status) => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/findByStatus?status=${encodeURIComponent(status)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询仓库错误:', error)
    throw error
  }
}

// 根据仓库类型查询仓库 API
export const findWarehouseByType = async (warehouseType) => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/findByType?warehouseType=${encodeURIComponent(warehouseType)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询仓库错误:', error)
    throw error
  }
}

// 根据仓库名称模糊查询 API
export const findWarehouseByName = async (warehouseName) => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/findByName?warehouseName=${encodeURIComponent(warehouseName)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询仓库错误:', error)
    throw error
  }
}

// 根据仓库名称查找仓库编码 API
export const findWarehouseCodeByName = async (warehouseName) => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/findCodeByName?warehouseName=${encodeURIComponent(warehouseName)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询仓库编码错误:', error)
    throw error
  }
}

// 修改仓库 API
export const updateWarehouse = async (warehouseDTO) => {
  try {
    // 过滤掉 null 或 undefined 的字段
    const filteredData = {}
    for (const [key, value] of Object.entries(warehouseDTO)) {
      if (value !== null && value !== undefined) {
        filteredData[key] = value
      }
    }
    const response = await fetch(`${BASE_URL}/warehouse/update`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams(filteredData)
    })
    
    return await response.json()
  } catch (error) {
    console.error('修改仓库错误:', error)
    throw error
  }
}

// 修改仓库状态 API
export const updateWarehouseStatus = async (warehouseCode, isDeleted) => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/updateStatus`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({ warehouseCode, isDeleted })
    })
    
    return await response.json()
  } catch (error) {
    console.error('修改仓库状态错误:', error)
    throw error
  }
}

// 分页查询仓库 API
export const getWarehouseByPage = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/page?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询仓库错误:', error)
    throw error
  }
}

// 按仓库名称模糊查询分页 API
export const searchWarehouseByPage = async (warehouseName, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/page/name?warehouseName=${encodeURIComponent(warehouseName)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询仓库错误:', error)
    throw error
  }
}

// 按仓库编码模糊查询分页 API
export const searchWarehouseByCodePage = async (warehouseCode, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/warehouse/page/code?warehouseCode=${encodeURIComponent(warehouseCode)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询仓库错误:', error)
    throw error
  }
}
