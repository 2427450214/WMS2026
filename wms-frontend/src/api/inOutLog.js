// 出入库日志相关 API 调用

const BASE_URL = 'http://localhost:8080'

// 查询所有库存变动日志（分页）
export const getStockChangeLogByPage = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/stockChangeLog/page?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询库存变动日志错误:', error)
    throw error
  }
}

// 根据操作类型查询库存变动日志（分页）
export const getStockChangeLogByOperationTypePage = async (operationType, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/stockChangeLog/page/operationType?operationType=${encodeURIComponent(operationType)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询库存变动日志错误:', error)
    throw error
  }
}

// 根据商品编码查询库存变动日志（分页）
export const getStockChangeLogByGoodsCodePage = async (goodsCode, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/stockChangeLog/page/goodsCode?goodsCode=${encodeURIComponent(goodsCode)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询库存变动日志错误:', error)
    throw error
  }
}

// 根据仓库编码查询库存变动日志（分页）
export const getStockChangeLogByWarehouseCodePage = async (warehouseCode, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/stockChangeLog/page/warehouseCode?warehouseCode=${encodeURIComponent(warehouseCode)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询库存变动日志错误:', error)
    throw error
  }
}

// 根据用户名称查询库存变动日志（分页）
export const getStockChangeLogByUserNamePage = async (userName, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/stockChangeLog/page/userName?userName=${encodeURIComponent(userName)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询库存变动日志错误:', error)
    throw error
  }
}

// 根据账号查询库存变动日志（分页）
export const getStockChangeLogByAccountPage = async (account, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/stockChangeLog/page/account?account=${encodeURIComponent(account)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询库存变动日志错误:', error)
    throw error
  }
}
