// 批次相关 API 调用

const BASE_URL = 'http://localhost:8080'

// 查询所有批次 API
export const findAllBatches = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/batch/page?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询批次错误:', error)
    throw error
  }
}

// 根据商品编码查询批次 API
export const getBatchByGoodsCodePage = async (goodsCode, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/batch/page/goodsCode?goodsCode=${encodeURIComponent(goodsCode)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询批次错误:', error)
    throw error
  }
}

// 根据仓库编码查询批次 API
export const getBatchByWarehouseCodePage = async (warehouseCode, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/batch/page/warehouseCode?warehouseCode=${encodeURIComponent(warehouseCode)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询批次错误:', error)
    throw error
  }
}

// 根据状态查询批次 API
export const getBatchByStatusPage = async (status, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/batch/page/status?status=${status}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询批次错误:', error)
    throw error
  }
}

// 查询即将过期批次 API
export const getBatchByNearExpiryPage = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/batch/page/nearExpiryOneMonth?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询批次错误:', error)
    throw error
  }
}

// 查询已过期批次 API
export const getBatchByExpiredPage = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/batch/page/expired?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询批次错误:', error)
    throw error
  }
}
