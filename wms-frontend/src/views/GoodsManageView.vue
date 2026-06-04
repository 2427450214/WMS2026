<template>
  <div class="goods-manage-view">
    <h2>商品管理</h2>
    
    <div class="search-bar">
      <select v-model="searchType">
        <option value="name">按商品名</option>
        <option value="code">按商品编码</option>
      </select>
      <input
        v-model="searchValue"
        type="text"
        :placeholder="searchType === 'name' ? '请输入商品名' : '请输入商品编码'"
        @input="handleSearch"
      />
      <select v-model="filterCategory" @change="filterByCategory">
        <option value="">全部分类</option>
        <option v-for="category in categories" :key="category.id" :value="category.id">
          {{ category.categoryName }}
        </option>
      </select>
      <select v-model="sortOrder" @change="handleSort">
        <option value="">默认排序</option>
        <option value="asc">库存升序</option>
        <option value="desc">库存降序</option>
      </select>
      <button @click="loadAllGoods(true)">刷新</button>
      <button @click="showAddModal = true" v-if="userRole === '管理员'" class="btn-add">添加商品</button>
    </div>

    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
    <div v-if="loading" class="loading">加载中...</div>

    <table v-if="pageData.records && pageData.records.length > 0" class="goods-table">
      <thead>
        <tr>
          <th>商品编码</th>
          <th>商品名称</th>
          <th>分类</th>
          <th>库存数量</th>
          <th>保质期(天)</th>
          <th>最低库存预警</th>
          <th>最高库存预警</th>
          <th v-if="userRole === '管理员'">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="goods in pageData.records" :key="goods.goodsCode">
          <td>{{ goods.goodsCode }}</td>
          <td>{{ goods.goodsName }}</td>
          <td>{{ getCategoryName(goods.categoryId) }}</td>
          <td :class="{ 'low-stock': isLowStock(goods), 'high-stock': isHighStock(goods) }">
            {{ goods.stockQuantity }}
          </td>
          <td>{{ goods.shelfLife === 0 ? '无限期' : goods.shelfLife }}</td>
          <td>{{ goods.minStockAlert }}</td>
          <td>{{ goods.maxStockAlert }}</td>
          <td v-if="userRole === '管理员'">
            <button @click="editGoods(goods)" class="btn-edit">编辑</button>
            <button @click="handleUpdateStatus(goods.goodsCode)" class="btn-delete">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else-if="!loading && (!pageData.records || pageData.records.length === 0)" class="no-data">暂无商品数据</div>

    <div v-if="!sortOrder" class="pagination">
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

    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ showAddModal ? '添加商品' : '编辑商品' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>商品名称</label>
            <input v-model="form.goodsName" type="text" required />
          </div>
          <div class="form-group">
            <label>分类</label>
            <select v-model="form.categoryId" required>
              <option value="">请选择分类</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.categoryName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>库存数量</label>
            <input v-model="form.stockQuantity" type="number" min="0" required />
          </div>
          <div class="form-group">
            <label>保质期(天)</label>
            <input v-model="form.shelfLife" type="number" min="0" required />
            <span class="hint">填写0表示无限期</span>
          </div>
          <div class="form-group">
            <label>最低库存预警</label>
            <input v-model="form.minStockAlert" type="number" min="0" required />
          </div>
          <div class="form-group">
            <label>最高库存预警</label>
            <input v-model="form.maxStockAlert" type="number" min="0" required />
          </div>
          <div class="form-buttons">
            <button type="button" @click="closeModal" class="btn-cancel">取消</button>
            <button type="submit" class="btn-submit">确定</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessageBox } from 'element-plus'
import { findAllCategories } from '../api/goodsCategory'
import { findAllGoods, findGoodsByName, findGoodsByCode, findGoodsByCategory, addGoods, updateGoods, updateGoodsStatus, getGoodsByPage, searchGoodsByPage, getGoodsByCategoryPage, orderGoodsByStock } from '../api/goods'

export default {
  name: 'GoodsManageView',
  data() {
    return {
      categories: [],
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
      filterCategory: '',
      sortOrder: '', // '', 'asc', 'desc'
      showAddModal: false,
      showEditModal: false,
      form: {
        goodsCode: '',
        goodsName: '',
        categoryId: '',
        stockQuantity: 0,
        shelfLife: 0,
        minStockAlert: 0,
        maxStockAlert: 0
      },
      userRole: ''
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('userRole')
    this.loadAllCategories()
    this.loadAllGoods(true)
  },
  methods: {
    clearMessages() {
      this.errorMessage = ''
      this.successMessage = ''
    },
    async loadAllCategories() {
      try {
        const response = await findAllCategories()
        if (response.code === 200) {
          this.categories = response.data || []
        }
      } catch (error) {
        console.error('加载分类错误:', error)
      }
    },
    async loadAllGoods(resetPage = false) {
      this.clearMessages()
      this.searchValue = ''
      this.filterCategory = ''
      if (resetPage) {
        this.pageData.page = 1
      }
      this.loading = true
      try {
        if (this.sortOrder) {
          const response = await orderGoodsByStock(this.sortOrder === 'asc')
          if (response.code === 200) {
            const data = response.data || []
            this.pageData.records = data
            this.pageData.total = data.length
            this.pageData.totalPages = 1
          } else {
            this.errorMessage = response.message || '加载商品失败'
          }
        } else {
          const response = await getGoodsByPage(this.pageData.page, this.pageSize)
          if (response.code === 200) {
            this.pageData = response.data
          } else {
            this.errorMessage = response.message || '加载商品失败'
          }
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('加载商品错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleSearch() {
      if (!this.searchValue.trim()) {
        await this.loadAllGoods(true)
        return
      }
      this.clearMessages()
      this.filterCategory = ''
      this.sortOrder = ''
      this.pageData.page = 1
      this.loading = true
      try {
        if (this.searchType === 'name') {
          const response = await searchGoodsByPage(this.searchValue, this.pageData.page, this.pageSize)
          if (response.code === 200) {
            this.pageData = response.data
          } else {
            this.errorMessage = response.message || '搜索失败'
            this.pageData.records = []
          }
        } else {
          const response = await findGoodsByCode(this.searchValue)
          if (response.code === 200) {
            const data = response.data ? [response.data] : []
            this.pageData.records = data
            this.pageData.total = data.length
            this.pageData.totalPages = 1
          } else {
            this.errorMessage = response.message || '搜索失败'
            this.pageData.records = []
          }
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('搜索商品错误:', error)
      } finally {
        this.loading = false
      }
    },
    async filterByCategory() {
      if (!this.filterCategory) {
        await this.loadAllGoods()
        return
      }
      this.clearMessages()
      this.searchValue = ''
      this.sortOrder = ''
      this.pageData.page = 1
      this.loading = true
      try {
        const response = await getGoodsByCategoryPage(this.filterCategory, this.pageData.page, this.pageSize)
        if (response.code === 200) {
          this.pageData = response.data
        } else {
          this.errorMessage = response.message || '筛选失败'
          this.pageData.records = []
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('筛选商品错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleSort() {
      this.clearMessages()
      this.searchValue = ''
      this.filterCategory = ''
      this.pageData.page = 1
      if (this.sortOrder) {
        await this.loadAllGoods(true)
      } else {
        await this.loadAllGoods(true)
      }
    },
    getCategoryName(categoryId) {
      const category = this.categories.find(c => c.id === categoryId)
      return category ? category.categoryName : '未分类'
    },
    isLowStock(goods) {
      return goods.stockQuantity <= goods.minStockAlert
    },
    isHighStock(goods) {
      return goods.stockQuantity >= goods.maxStockAlert
    },
    editGoods(goods) {
      this.form = {
        goodsCode: goods.goodsCode,
        goodsName: goods.goodsName,
        categoryId: goods.categoryId,
        stockQuantity: goods.stockQuantity || 0,
        shelfLife: goods.shelfLife || 0,
        minStockAlert: goods.minStockAlert || 0,
        maxStockAlert: goods.maxStockAlert || 0
      }
      this.showEditModal = true
    },
    async handleSubmit() {
      this.clearMessages()
      this.loading = true
      try {
        let response
        if (this.showAddModal) {
          response = await addGoods(this.form)
        } else {
          response = await updateGoods(this.form)
        }
        if (response.code === 200) {
          this.successMessage = response.message || '操作成功'
          this.closeModal()
          await this.loadAllGoods(true)
        } else {
          this.errorMessage = response.message || '操作失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('操作商品错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleUpdateStatus(goodsCode) {
      try {
        await ElMessageBox.confirm(
          '确定要删除该商品吗？',
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
          const response = await updateGoodsStatus(goodsCode, 1)
          if (response.code === 200) {
            this.successMessage = response.message || '删除成功'
            await this.loadAllGoods()
          } else {
            this.errorMessage = response.message || '删除失败'
          }
        } catch (error) {
          this.errorMessage = '网络错误，请稍后重试'
          console.error('删除商品错误:', error)
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
        goodsCode: '',
        goodsName: '',
        categoryId: '',
        stockQuantity: 0,
        shelfLife: 0,
        minStockAlert: 0,
        maxStockAlert: 0
      }
    },
    async changePage(page) {
      if (page < 1 || page > this.pageData.totalPages) return
      if (this.sortOrder) {
        // 排序状态下不分页
        return
      }
      this.pageData.page = page
      if (this.searchValue && this.searchType === 'name') {
        await this.handleSearch()
      } else if (this.filterCategory) {
        await this.filterByCategory()
      } else {
        await this.loadAllGoods()
      }
    },
    async handlePageSizeChange() {
      if (this.sortOrder) {
        // 排序状态下不改变分页大小
        return
      }
      this.pageData.page = 1
      this.pageData.pageSize = this.pageSize
      if (this.searchValue && this.searchType === 'name') {
        await this.handleSearch()
      } else if (this.filterCategory) {
        await this.filterByCategory()
      } else {
        await this.loadAllGoods()
      }
    }
  }
}
</script>

<style scoped>
.goods-manage-view {
  padding: 20px;
}

.goods-manage-view h2 {
  margin-bottom: 20px;
  color: #333;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-bar select,
.search-bar input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
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

.goods-table {
  width: 100%;
  border-collapse: collapse;
}

.goods-table th,
.goods-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  font-size: 14px;
}

.goods-table th {
  background-color: #f5f5f5;
  font-weight: 500;
  color: #666;
}

.goods-table tr:hover {
  background-color: #fafafa;
}

.low-stock {
  color: #ff9800;
  font-weight: bold;
}

.high-stock {
  color: #f44336;
  font-weight: bold;
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
  min-width: 450px;
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

.form-group .hint {
  display: block;
  font-size: 12px;
  color: #999;
  margin-top: 4px;
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
