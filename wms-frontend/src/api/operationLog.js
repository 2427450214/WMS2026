// 操作日志相关 API 调用
import request from './request'

// 查询所有操作日志
export const getAllOperationLogs = async () => {
  return await request('/operationLog/findAll')
}

// 根据操作类型查询操作日志
export const getOperationLogsByType = async (operationType) => {
  return await request(`/operationLog/findByOperationType?operationType=${encodeURIComponent(operationType)}`)
}

// 根据用户账号查询操作日志
export const getOperationLogsByAccount = async (account) => {
  return await request(`/operationLog/findByAccount?account=${encodeURIComponent(account)}`)
}

// 分页查询操作日志
export const getOperationLogByPage = async (page = 1, pageSize = 10) => {
  return await request(`/operationLog/page?page=${page}&pageSize=${pageSize}`)
}

// 根据操作类型模糊查询分页
export const getOperationLogByOperationTypePage = async (operationType, page = 1, pageSize = 10) => {
  return await request(`/operationLog/page/operationType?operationType=${encodeURIComponent(operationType)}&page=${page}&pageSize=${pageSize}`)
}

// 根据用户账号模糊查询分页
export const getOperationLogByAccountPage = async (account, page = 1, pageSize = 10) => {
  return await request(`/operationLog/page/account?account=${encodeURIComponent(account)}&page=${page}&pageSize=${pageSize}`)
}
