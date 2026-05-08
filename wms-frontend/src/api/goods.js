// 商品相关 API 调用

const BASE_URL = 'http://localhost:8080'

// 新增商品 API
export const addGoods = async (goodsDTO) => {
  try {
    // 过滤掉 null 或 undefined 的字段
    const filteredData = {}
    for (const [key, value] of Object.entries(goodsDTO)) {
      if (value !== null && value !== undefined) {
        filteredData[key] = value
      }
    }
    const response = await fetch(`${BASE_URL}/goods/add`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams(filteredData)
    })
    
    return await response.json()
  } catch (error) {
    console.error('新增商品错误:', error)
    throw error
  }
}

// 根据商品名模糊查询 API
export const findGoodsByName = async (goodsName) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/findByName?goodsName=${encodeURIComponent(goodsName)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 查询所有商品 API
export const findAllGoods = async () => {
  try {
    const response = await fetch(`${BASE_URL}/goods/findAll`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 根据商品编码查询商品 API
export const findGoodsByCode = async (goodsCode) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/findByCode?goodsCode=${encodeURIComponent(goodsCode)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 根据分类查询商品 API
export const findGoodsByCategory = async (categoryId) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/findByCategory?categoryId=${categoryId}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 修改商品 API
export const updateGoods = async (goodsDTO) => {
  try {
    // 过滤掉 null 或 undefined 的字段
    const filteredData = {}
    for (const [key, value] of Object.entries(goodsDTO)) {
      if (value !== null && value !== undefined) {
        filteredData[key] = value
      }
    }
    const response = await fetch(`${BASE_URL}/goods/update`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams(filteredData)
    })
    
    return await response.json()
  } catch (error) {
    console.error('修改商品错误:', error)
    throw error
  }
}

// 修改商品状态 API
export const updateGoodsStatus = async (goodsCode, isDeleted) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/updateStatus`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({ goodsCode, isDeleted })
    })
    
    return await response.json()
  } catch (error) {
    console.error('修改商品状态错误:', error)
    throw error
  }
}

// 根据库存数量排序查询商品 API
export const orderGoodsByStock = async (asc) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/orderByStock?asc=${asc}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 查询库存量接近最低库存预警线的商品 API
export const findNearMinStock = async () => {
  try {
    const response = await fetch(`${BASE_URL}/goods/nearMinStock`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 查询库存接近最高库存线的商品 API
export const findNearMaxStock = async () => {
  try {
    const response = await fetch(`${BASE_URL}/goods/nearMaxStock`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 根据商品名查找商品编码 API
export const findCodeByName = async (goodsName) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/findCodeByName?goodsName=${encodeURIComponent(goodsName)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品编码错误:', error)
    throw error
  }
}

// 查询库存数量最多的前N个商品 API
export const getTopStockGoods = async (limit = 3) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/getTopStockGoods?limit=${limit}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 分页查询商品 API
export const getGoodsByPage = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/page?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 按商品名模糊查询分页 API
export const searchGoodsByPage = async (goodsName, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/searchPage?goodsName=${encodeURIComponent(goodsName)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}

// 按分类查询分页 API
export const getGoodsByCategoryPage = async (categoryId, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/goods/categoryPage?categoryId=${categoryId}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品错误:', error)
    throw error
  }
}
