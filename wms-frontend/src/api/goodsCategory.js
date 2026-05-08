// 商品分类相关 API 调用

const BASE_URL = 'http://localhost:8080'

// 添加商品分类 API
export const addGoodsCategory = async (goodsCategoryDTO) => {
  try {
    // 过滤掉 null 或 undefined 的字段
    const filteredData = {}
    for (const [key, value] of Object.entries(goodsCategoryDTO)) {
      if (value !== null && value !== undefined) {
        filteredData[key] = value
      }
    }
    const response = await fetch(`${BASE_URL}/goodsCategory/add`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams(filteredData)
    })
    
    return await response.json()
  } catch (error) {
    console.error('添加商品分类错误:', error)
    throw error
  }
}

// 查询所有商品分类 API
export const findAllCategories = async () => {
  try {
    const response = await fetch(`${BASE_URL}/goodsCategory/findAll`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品分类错误:', error)
    throw error
  }
}

// 根据ID查询商品分类 API
export const findCategoryById = async (id) => {
  try {
    const response = await fetch(`${BASE_URL}/goodsCategory/findById?id=${id}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品分类错误:', error)
    throw error
  }
}

// 根据名称查询商品分类 API
export const findCategoryByName = async (categoryName) => {
  try {
    const response = await fetch(`${BASE_URL}/goodsCategory/findByName?categoryName=${encodeURIComponent(categoryName)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品分类错误:', error)
    throw error
  }
}

// 修改商品分类 API
export const updateGoodsCategory = async (goodsCategoryDTO) => {
  try {
    // 过滤掉 null 或 undefined 的字段
    const filteredData = {}
    for (const [key, value] of Object.entries(goodsCategoryDTO)) {
      if (value !== null && value !== undefined) {
        filteredData[key] = value
      }
    }
    const response = await fetch(`${BASE_URL}/goodsCategory/update`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams(filteredData)
    })
    
    return await response.json()
  } catch (error) {
    console.error('修改商品分类错误:', error)
    throw error
  }
}

// 修改商品分类状态 API
export const updateCategoryStatus = async (id, isDeleted) => {
  try {
    const response = await fetch(`${BASE_URL}/goodsCategory/updateStatus`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({ id, isDeleted })
    })
    
    return await response.json()
  } catch (error) {
    console.error('修改商品分类状态错误:', error)
    throw error
  }
}

// 分页查询商品分类 API
export const getCategoryByPage = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/goodsCategory/page?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品分类错误:', error)
    throw error
  }
}

// 按分类名模糊查询分页 API
export const searchCategoryByPage = async (categoryName, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/goodsCategory/searchPage?categoryName=${encodeURIComponent(categoryName)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询商品分类错误:', error)
    throw error
  }
}
