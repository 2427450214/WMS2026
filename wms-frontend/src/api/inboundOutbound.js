// 出入库相关 API 调用

const BASE_URL = 'http://localhost:8080'

// 新增入库明细 API
export const addInboundDetail = async (inboundDetailDTO) => {
  try {
    // 过滤掉 null 或 undefined 的字段
    const filteredData = {}
    for (const [key, value] of Object.entries(inboundDetailDTO)) {
      if (value !== null && value !== undefined) {
        filteredData[key] = value
      }
    }
    const response = await fetch(`${BASE_URL}/inboundDetail/add`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams(filteredData)
    })
    
    return await response.json()
  } catch (error) {
    console.error('新增入库明细错误:', error)
    throw error
  }
}

// 查询所有入库明细 API
export const findAllInboundDetails = async () => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/findAll`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 根据仓库编码查询入库明细 API
export const findInboundDetailsByWarehouseCode = async (warehouseCode) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/findByWarehouseCode?warehouseCode=${encodeURIComponent(warehouseCode)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 根据商品编码查询入库明细 API
export const findInboundDetailsByGoodsCode = async (goodsCode) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/findByGoodsCode?goodsCode=${encodeURIComponent(goodsCode)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 根据批次号查询入库明细 API
export const findInboundDetailsByBatchNo = async (batchNo) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/findByBatchNo?batchNo=${encodeURIComponent(batchNo)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 根据入库单ID查询入库明细 API
export const findInboundDetailsByInboundId = async (inboundId) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/findByInboundId?inboundId=${encodeURIComponent(inboundId)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 删除入库明细 API
export const deleteInboundDetail = async (inboundId) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/delete?inboundId=${encodeURIComponent(inboundId)}`, {
      method: 'DELETE',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('删除入库明细错误:', error)
    throw error
  }
}

// 分页查询入库明细 API
export const getInboundDetailsByPage = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/page?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 根据入库单ID分页查询入库明细 API
export const getInboundDetailsByInboundIdPage = async (inboundId, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/page/inboundId?inboundId=${encodeURIComponent(inboundId)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 根据仓库编码分页查询入库明细 API
export const getInboundDetailsByWarehouseCodePage = async (warehouseCode, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/page/warehouseCode?warehouseCode=${encodeURIComponent(warehouseCode)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 根据商品编码分页查询入库明细 API
export const getInboundDetailsByGoodsCodePage = async (goodsCode, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/page/goodsCode?goodsCode=${encodeURIComponent(goodsCode)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 根据批次号分页查询入库明细 API
export const getInboundDetailsByBatchNoPage = async (batchNo, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/inboundDetail/page/batchNo?batchNo=${encodeURIComponent(batchNo)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询入库明细错误:', error)
    throw error
  }
}

// 新增出库明细 API
export const addOutboundDetail = async (outboundDetailDTO) => {
  try {
    // 过滤掉 null 或 undefined 的字段
    const filteredData = {}
    for (const [key, value] of Object.entries(outboundDetailDTO)) {
      if (value !== null && value !== undefined) {
        filteredData[key] = value
      }
    }
    const response = await fetch(`${BASE_URL}/outboundDetail/add`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(filteredData)
    })
    
    return await response.json()
  } catch (error) {
    console.error('新增出库明细错误:', error)
    throw error
  }
}

// 查询所有出库明细 API
export const findAllOutboundDetails = async () => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/findAll`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 根据仓库编码查询出库明细 API
export const findOutboundDetailsByWarehouseCode = async (warehouseCode) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/findByWarehouseCode?warehouseCode=${encodeURIComponent(warehouseCode)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 根据商品编码查询出库明细 API
export const findOutboundDetailsByGoodsCode = async (goodsCode) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/findByGoodsCode?goodsCode=${encodeURIComponent(goodsCode)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 根据批次号查询出库明细 API
export const findOutboundDetailsByBatchNo = async (batchNo) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/findByBatchNo?batchNo=${encodeURIComponent(batchNo)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 根据出库单ID查询出库明细 API
export const findOutboundDetailsByOutboundId = async (outboundId) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/findByOutboundId?outboundId=${encodeURIComponent(outboundId)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 删除出库明细 API
export const deleteOutboundDetail = async (outboundId) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/delete?outboundId=${encodeURIComponent(outboundId)}`, {
      method: 'DELETE',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('删除出库明细错误:', error)
    throw error
  }
}

// 分页查询出库明细 API
export const getOutboundDetailsByPage = async (page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/page?page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 根据出库单ID分页查询出库明细 API
export const getOutboundDetailsByOutboundIdPage = async (outboundId, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/page/outboundId?outboundId=${encodeURIComponent(outboundId)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 根据仓库编码分页查询出库明细 API
export const getOutboundDetailsByWarehouseCodePage = async (warehouseCode, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/page/warehouseCode?warehouseCode=${encodeURIComponent(warehouseCode)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 根据商品编码分页查询出库明细 API
export const getOutboundDetailsByGoodsCodePage = async (goodsCode, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/page/goodsCode?goodsCode=${encodeURIComponent(goodsCode)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 根据批次号分页查询出库明细 API
export const getOutboundDetailsByBatchNoPage = async (batchNo, page = 1, pageSize = 10) => {
  try {
    const response = await fetch(`${BASE_URL}/outboundDetail/page/batchNo?batchNo=${encodeURIComponent(batchNo)}&page=${page}&pageSize=${pageSize}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询出库明细错误:', error)
    throw error
  }
}

// 查询所有批次 API
export const findAllBatches = async () => {
  try {
    const response = await fetch(`${BASE_URL}/batch/findAll`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询所有批次错误:', error)
    throw error
  }
}

// 根据商品编码和仓库编码查询批次 API
export const findBatchesByGoodsAndWarehouse = async (goodsCode, warehouseCode) => {
  try {
    const response = await fetch(`${BASE_URL}/batch/findByGoodsAndWarehouse?goodsCode=${encodeURIComponent(goodsCode)}&warehouseCode=${encodeURIComponent(warehouseCode)}`, {
      method: 'GET',
      credentials: 'include'
    })
    
    return await response.json()
  } catch (error) {
    console.error('查询批次错误:', error)
    throw error
  }
}
