<template>
  <div class="warehouse-manage-view">
    <h2>仓库管理</h2>
    
    <div class="search-bar">
      <select v-model="searchType" class="search-type-select">
        <option value="name">按仓库名称</option>
        <option value="code">按仓库编码</option>
      </select>
      <input
        v-model="searchValue"
        type="text"
        :placeholder="searchType === 'name' ? '请输入仓库名称' : '请输入仓库编码'"
        @input="handleSearch"
      />
      <select v-model="filterWarehouseType" @change="handleFilter" class="filter-select">
        <option value="">全部类型</option>
        <option value="普通">普通</option>
        <option value="冷藏">冷藏</option>
        <option value="危险品">危险品</option>
        <option value="临时">临时</option>
      </select>
      <select v-model="filterStatus" @change="handleFilter" class="filter-select">
        <option value="">全部状态</option>
        <option value="启用">启用</option>
        <option value="弃用">弃用</option>
        <option value="维护">维护</option>
        <option value="检查中">检查中</option>
      </select>
      <button @click="loadAllWarehouses(true)">刷新</button>
      <button @click="showAddModal = true" v-if="userRole === '管理员'" class="btn-add">添加仓库</button>
    </div>

    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
    <div v-if="loading" class="loading">加载中...</div>

    <table v-if="pageData.records && pageData.records.length > 0" class="warehouse-table">
      <thead>
        <tr>
          <th>仓库编码</th>
          <th>仓库名称</th>
          <th>仓库类型</th>
          <th>状态</th>
          <th>创建时间</th>
          <th v-if="userRole === '管理员'">操作</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="warehouse in pageData.records" :key="warehouse.warehouseCode" @click="openWarehouseDrawer(warehouse)" class="warehouse-row">
          <td>{{ warehouse.warehouseCode }}</td>
          <td>
            <span class="warehouse-link">{{ warehouse.warehouseName }}</span>
          </td>
          <td>
            <span :class="'warehouse-type-' + warehouse.warehouseType">{{ warehouse.warehouseType }}</span>
          </td>
          <td>
            <span :class="'status-' + warehouse.status">{{ warehouse.status }}</span>
          </td>
          <td>{{ formatDate(warehouse.createTime) }}</td>
          <td v-if="userRole === '管理员'" @click.stop>
            <button @click="editWarehouse(warehouse)" class="btn-edit">编辑</button>
            <button @click="handleUpdateStatus(warehouse.warehouseCode)" class="btn-delete">删除</button>
          </td>
          <td class="arrow-cell">
            <span class="drawer-arrow">→</span>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else-if="!loading && (!pageData.records || pageData.records.length === 0)" class="no-data">暂无仓库数据</div>

    <div class="pagination">
      <button @click="changePage(1)" :disabled="pageData.page <= 1">首页</button>
      <button @click="changePage(pageData.page - 1)" :disabled="pageData.page <= 1">上一页</button>
      <span>第 {{ pageData.page }} 页，共 {{ pageData.totalPages }} 页</span>
      <button @click="changePage(pageData.page + 1)" :disabled="pageData.page >= pageData.totalPages">下一页</button>
      <button @click="changePage(pageData.totalPages)" :disabled="pageData.page >= pageData.totalPages">末页</button>
      <select v-model="pageSize" @change="handlePageSizeChange">
        <option :value="5">5条/页</option>
        <option :value="10" selected>10条/页</option>
        <option :value="20">20条/页</option>
        <option :value="50">50条/页</option>
      </select>
    </div>

    <!-- 添加/编辑仓库弹窗 -->
    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ showAddModal ? '添加仓库' : '编辑仓库' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>仓库名称</label>
            <input v-model="form.warehouseName" type="text" required />
          </div>
          <div class="form-group">
            <label>仓库类型</label>
            <select v-model="form.warehouseType" required>
              <option value="">请选择仓库类型</option>
              <option value="普通">普通</option>
              <option value="冷藏">冷藏</option>
              <option value="危险品">危险品</option>
              <option value="临时">临时</option>
            </select>
          </div>
          <div class="form-group">
            <label>状态</label>
            <select v-model="form.status" required>
              <option value="">请选择状态</option>
              <option value="启用">启用</option>
              <option value="弃用">弃用</option>
              <option value="维护">维护</option>
              <option value="检查中">检查中</option>
            </select>
          </div>
          <div class="form-buttons">
            <button type="button" @click="closeModal" class="btn-cancel">取消</button>
            <button type="submit" class="btn-submit">确定</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 仓库抽屉 -->
    <div v-if="showDrawer" class="drawer-overlay" @click.self="closeDrawer">
      <div class="drawer-content">
        <div class="drawer-header">
          <h3>仓库详情 - {{ currentWarehouse?.warehouseName }}</h3>
          <button @click="closeDrawer" class="btn-close">×</button>
        </div>
        <div class="drawer-body">
          <div class="warehouse-info">
            <p><strong>仓库编码：</strong>{{ currentWarehouse?.warehouseCode }}</p>
            <p><strong>仓库名称：</strong>{{ currentWarehouse?.warehouseName }}</p>
            <p><strong>仓库类型：</strong>{{ currentWarehouse?.warehouseType }}</p>
            <p><strong>状态：</strong>{{ currentWarehouse?.status }}</p>
          </div>
          <div class="batch-list-section">
            <h4>仓库商品批次</h4>
            <table class="batch-table" v-if="batchPageData.records && batchPageData.records.length > 0">
              <thead>
                <tr>
                  <th>批次号</th>
                  <th>商品编码</th>
                  <th>商品名称</th>
                  <th>生产日期</th>
                  <th>到期日期</th>
                  <th>批次数量</th>
                  <th>剩余数量</th>
                  <th>状态</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="batch in batchPageData.records" :key="batch.batchNumber">
                  <td>{{ batch.batchNumber }}</td>
                  <td>{{ batch.goodsCode }}</td>
                  <td>{{ batch.goodsName }}</td>
                  <td>{{ formatDate(batch.productionDate) }}</td>
                  <td :class="{'expiry-warning': isNearExpiry(batch.expiryDate), 'expired': isExpired(batch.expiryDate)}">
                    {{ formatDate(batch.expiryDate) }}
                  </td>
                  <td>{{ batch.batchQuantity }}</td>
                  <td>{{ batch.remainingQuantity }}</td>
                  <td>
                    <span :class="'batch-status-' + batch.status">{{ batch.status }}</span>
                  </td>
                </tr>
              </tbody>
            </table>
            <div v-else-if="!batchLoading" class="no-data">该仓库暂无商品批次</div>
            
            <div v-if="batchPageData.totalPages && batchPageData.totalPages > 1" class="pagination">
              <button @click="changeBatchPage(batchPageData.page - 1)" :disabled="batchPageData.page <= 1">上一页</button>
              <span>第 {{ batchPageData.page }} / {{ batchPageData.totalPages }} 页，共 {{ batchPageData.total }} 条</span>
              <button @click="changeBatchPage(batchPageData.page + 1)" :disabled="batchPageData.page >= batchPageData.totalPages">下一页</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessageBox } from 'element-plus'
import { findAllWarehouses, addWarehouse, updateWarehouse, updateWarehouseStatus, getWarehouseByPage, searchWarehouseByPage, searchWarehouseByCodePage, findWarehouseByStatus, findWarehouseByType } from '../api/warehouse'
import { getBatchByWarehouseCodePage } from '../api/batch'

export default {
  name: 'WarehouseManageView',
  data() {
    return {
      pageData: {
        records: [],
        total: 0,
        page: 1,
        pageSize: 10,
        totalPages: 0
      },
      pageSize: 10,
      loading: false,
      errorMessage: '',
      successMessage: '',
      searchType: 'name',
      searchValue: '',
      filterWarehouseType: '',
      filterStatus: '',
      showAddModal: false,
      showEditModal: false,
      form: {
        id: null,
        warehouseName: '',
        warehouseCode: '',
        warehouseType: '',
        status: ''
      },
      userRole: '',
      // 抽屉相关
      showDrawer: false,
      currentWarehouse: null,
      batchPageData: {
        records: [],
        total: 0,
        page: 1,
        pageSize: 10,
        totalPages: 0
      },
      batchLoading: false
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('userRole')
    this.loadAllWarehouses(true)
  },
  methods: {
    clearMessages() {
      this.errorMessage = ''
      this.successMessage = ''
    },
    async loadAllWarehouses(resetPage = false) {
      this.clearMessages()
      this.searchValue = ''
      this.filterWarehouseType = ''
      this.filterStatus = ''
      if (resetPage) {
        this.pageData.page = 1
      }
      this.loading = true
      try {
        const response = await getWarehouseByPage(this.pageData.page, this.pageSize)
        if (response.code === 200) {
          this.pageData = response.data
        } else {
          this.errorMessage = response.message || '加载仓库失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('加载仓库错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleSearch() {
      if (!this.searchValue.trim()) {
        await this.loadAllWarehouses(true)
        return
      }
      this.clearMessages()
      this.filterWarehouseType = ''
      this.filterStatus = ''
      this.pageData.page = 1
      this.loading = true
      try {
        let response
        if (this.searchType === 'name') {
          response = await searchWarehouseByPage(this.searchValue, this.pageData.page, this.pageSize)
        } else {
          response = await searchWarehouseByCodePage(this.searchValue, this.pageData.page, this.pageSize)
        }
        if (response.code === 200) {
          this.pageData = response.data
        } else {
          this.errorMessage = response.message || '搜索失败'
          this.pageData.records = []
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('搜索仓库错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleFilter() {
      this.clearMessages()
      this.searchValue = ''
      this.pageData.page = 1
      this.loading = true
      try {
        let response
        if (this.filterWarehouseType) {
          response = await findWarehouseByType(this.filterWarehouseType)
          if (response.code === 200) {
            this.pageData.records = response.data
            this.pageData.total = response.data.length
            this.pageData.totalPages = 1
          } else {
            this.errorMessage = response.message || '筛选失败'
            this.pageData.records = []
          }
        } else if (this.filterStatus) {
          response = await findWarehouseByStatus(this.filterStatus)
          if (response.code === 200) {
            this.pageData.records = response.data
            this.pageData.total = response.data.length
            this.pageData.totalPages = 1
          } else {
            this.errorMessage = response.message || '筛选失败'
            this.pageData.records = []
          }
        } else {
          await this.loadAllWarehouses(true)
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('筛选仓库错误:', error)
      } finally {
        this.loading = false
      }
    },
    // 打开仓库抽屉
    async openWarehouseDrawer(warehouse) {
      this.currentWarehouse = warehouse
      this.showDrawer = true
      this.batchPageData.page = 1
      await this.loadBatchList(warehouse.warehouseCode)
    },
    // 关闭抽屉
    closeDrawer() {
      this.showDrawer = false
      this.currentWarehouse = null
      this.batchPageData = {
        records: [],
        total: 0,
        page: 1,
        pageSize: 10,
        totalPages: 0
      }
    },
    // 加载批次列表
    async loadBatchList(warehouseCode, resetPage = false) {
      this.batchLoading = true
      try {
        const response = await getBatchByWarehouseCodePage(warehouseCode, this.batchPageData.page, this.batchPageData.pageSize)
        if (response.code === 200) {
          this.batchPageData = response.data
        } else {
          this.errorMessage = response.message || '加载批次失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('加载批次错误:', error)
      } finally {
        this.batchLoading = false
      }
    },
    // 切换批次页码
    async changeBatchPage(page) {
      if (page < 1 || page > this.batchPageData.totalPages) return
      this.batchPageData.page = page
      await this.loadBatchList(this.currentWarehouse.warehouseCode)
    },
    // 判断是否接近过期
    isNearExpiry(expiryDate) {
      if (!expiryDate) return false
      const expiry = new Date(expiryDate)
      const now = new Date()
      const diffDays = Math.ceil((expiry - now) / (1000 * 60 * 60 * 24))
      return diffDays > 0 && diffDays <= 30
    },
    // 判断是否已过期
    isExpired(expiryDate) {
      if (!expiryDate) return false
      const expiry = new Date(expiryDate)
      const now = new Date()
      return expiry < now
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    },
    editWarehouse(warehouse) {
      this.form = {
        id: warehouse.id,
        warehouseName: warehouse.warehouseName,
        warehouseCode: warehouse.warehouseCode,
        warehouseType: warehouse.warehouseType,
        status: warehouse.status
      }
      this.showEditModal = true
    },
    async handleSubmit() {
      this.clearMessages()
      this.loading = true
      try {
        let response
        if (this.showAddModal) {
          response = await addWarehouse(this.form)
        } else {
          response = await updateWarehouse(this.form)
        }
        if (response.code === 200) {
          this.successMessage = response.message || '操作成功'
          this.closeModal()
          await this.loadAllWarehouses(true)
        } else {
          this.errorMessage = response.message || '操作失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('操作仓库错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleUpdateStatus(warehouseCode) {
      try {
        await ElMessageBox.confirm(
          '确定要删除该仓库吗？',
          '确认操作',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        this.clearMessages()
        this.loading = true
        try {
          const response = await updateWarehouseStatus(warehouseCode, 1)
          if (response.code === 200) {
            this.successMessage = response.message || '删除成功'
            await this.loadAllWarehouses(true)
          } else {
            this.errorMessage = response.message || '删除失败'
          }
        } catch (error) {
          this.errorMessage = '网络错误，请稍后重试'
          console.error('删除仓库错误:', error)
        } finally {
          this.loading = false
        }
      } catch {
        // 用户取消，不做任何处理
      }
    },
    closeModal() {
      this.showAddModal = false
      this.showEditModal = false
      this.resetForm()
    },
    resetForm() {
      this.form = {
        id: null,
        warehouseName: '',
        warehouseCode: '',
        warehouseType: '',
        status: ''
      }
    },
    async changePage(page) {
      if (page < 1 || page > this.pageData.totalPages) return
      this.pageData.page = page
      if (this.searchValue) {
        await this.handleSearch()
      } else {
        await this.loadAllWarehouses()
      }
    },
    async handlePageSizeChange() {
      this.pageData.page = 1
      this.pageData.pageSize = this.pageSize
      if (this.searchValue) {
        await this.handleSearch()
      } else {
        await this.loadAllWarehouses(true)
      }
    }
  }
}
</script>

<style scoped>
.warehouse-manage-view {
  padding: 20px;
}

.warehouse-manage-view h2 {
  margin-bottom: 20px;
  color: #333;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-bar input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  flex: 1;
  max-width: 300px;
}

.search-bar button {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-bar button:hover {
  background-color: #45a049;
}

.search-bar .btn-add {
  background-color: #2196F3;
}

.search-bar .btn-add:hover {
  background-color: #1976D2;
}

.search-bar .filter-select,
.search-bar .search-type-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.warehouse-row {
  cursor: pointer;
  transition: all 0.2s ease;
  background-color: rgba(255, 255, 255, 0.8);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.warehouse-row:hover {
  background-color: rgba(240, 249, 255, 0.95);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-1px);
}

.warehouse-link {
  color: rgb(156, 163, 175);
  text-decoration: none;
  font-weight: 500;
}

.arrow-cell {
  text-align: center;
}

.drawer-arrow {
  font-size: 20px;
  color: rgb(156, 163, 175);
  transition: all 0.2s ease;
}

.warehouse-row:hover .drawer-arrow {
  color: #409eff;
  transform: translateX(3px);
}

/* 抽屉样式 */
.drawer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: flex-end;
  z-index: 1000;
}

.drawer-content {
  width: 60%;
  min-width: 600px;
  max-width: 900px;
  background-color: white;
  height: 100%;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.drawer-header h3 {
  margin: 0;
}

.btn-close {
  background: none;
  border: none;
  font-size: 28px;
  cursor: pointer;
  color: #666;
}

.btn-close:hover {
  color: #333;
}

.drawer-body {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.warehouse-info {
  margin-bottom: 30px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.warehouse-info p {
  margin: 8px 0;
}

.batch-list-section h4 {
  margin-bottom: 15px;
  color: #333;
}

.batch-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.batch-table th,
.batch-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  font-size: 13px;
}

.batch-table th {
  background-color: #f5f5f5;
  font-weight: 500;
  color: #666;
}

.expiry-warning {
  color: #e6a23c;
  font-weight: 500;
}

.expired {
  color: #f56c6c;
  font-weight: 500;
}

.batch-status-正常 {
  background-color: #e1f3d8;
  color: #67c23a;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.batch-status-过期 {
  background-color: #fde2e2;
  color: #f56c6c;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.error-message {
  color: #f44336;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #ffebee;
  border-radius: 4px;
}

.success-message {
  color: #4CAF50;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #e8f5e9;
  border-radius: 4px;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.warehouse-table {
  width: 100%;
  border-collapse: collapse;
}

.warehouse-table th,
.warehouse-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  font-size: 14px;
}

.warehouse-table th {
  background-color: #f5f5f5;
  font-weight: 500;
  color: #666;
}

.warehouse-table tr:hover {
  background-color: #fafafa;
}

.warehouse-type-普通 {
  background-color: #e3f2fd;
  color: #1976D2;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.warehouse-type-冷藏 {
  background-color: #e0f7fa;
  color: #0097A7;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.warehouse-type-危险品 {
  background-color: #ffebee;
  color: #c62828;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.warehouse-type-临时 {
  background-color: #fff3e0;
  color: #ef6c00;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-启用 {
  background-color: #e8f5e9;
  color: #2e7d32;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-弃用 {
  background-color: #ffebee;
  color: #c62828;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-维护 {
  background-color: #fff3e0;
  color: #ef6c00;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-检查中 {
  background-color: #f3e5f5;
  color: #7b1fa2;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.btn-edit,
.btn-delete {
  padding: 6px 12px;
  margin-right: 8px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  color: white;
  font-size: 13px;
}

.btn-edit {
  background-color: #2196F3;
}

.btn-edit:hover {
  background-color: #1976D2;
}

.btn-delete {
  background-color: #f44336;
}

.btn-delete:hover {
  background-color: #d32f2f;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 15px;
}

.pagination button {
  padding: 8px 16px;
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:hover:not(:disabled) {
  background-color: #4CAF50;
  color: white;
  border-color: #4CAF50;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 30px;
  border-radius: 8px;
  min-width: 400px;
  max-width: 90%;
}

.modal-content h3 {
  margin-bottom: 20px;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #666;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

.form-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

.btn-cancel {
  padding: 10px 20px;
  background-color: #999;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel:hover {
  background-color: #777;
}

.btn-submit {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-submit:hover {
  background-color: #45a049;
}
</style>
