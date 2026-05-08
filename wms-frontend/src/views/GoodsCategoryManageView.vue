<template>
  <div class="category-manage-view">
    <h2>商品分类</h2>
    
    <div class="search-bar">
      <input
        v-model="searchValue"
        type="text"
        placeholder="请输入分类名称"
        @input="handleSearch"
      />
      <button @click="loadAllCategories(true)">刷新</button>
      <button @click="showAddModal = true" v-if="userRole === '管理员'" class="btn-add">添加分类</button>
    </div>

    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
    <div v-if="loading" class="loading">加载中...</div>

    <div v-if="pageData.records && pageData.records.length > 0" class="category-list">
      <el-collapse v-model="activeCategoryNames" accordion>
        <el-collapse-item 
          v-for="category in pageData.records" 
          :key="category.id" 
          :name="category.id"
        >
          <template #title>
            <div class="category-title">
              <span class="category-name">{{ category.categoryName }}</span>
              <span class="category-count" v-if="getGoodsCount(category.id) > 0">
                ({{ getGoodsCount(category.id) }}件商品)
              </span>
              <div class="category-actions" v-if="userRole === '管理员'">
                <button @click.stop="editCategory(category)" class="btn-edit-small">编辑</button>
                <button @click.stop="handleUpdateStatus(category)" class="btn-delete-small">删除</button>
              </div>
            </div>
          </template>
          
          <div class="goods-in-category">
            <div v-if="getCategoryGoods(category.id).length > 0">
              <table class="goods-table">
                <thead>
                  <tr>
                    <th>商品编码</th>
                    <th>商品名称</th>
                    <th>库存数量</th>
                    <th>保质期(天)</th>
                    <th>最低库存预警</th>
                    <th>最高库存预警</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="goods in getCategoryGoods(category.id)" :key="goods.goodsCode">
                    <td>{{ goods.goodsCode }}</td>
                    <td>{{ goods.goodsName }}</td>
                    <td :class="{ 'low-stock': isLowStock(goods), 'high-stock': isHighStock(goods) }">
                      {{ goods.stockQuantity }}
                    </td>
                    <td>{{ goods.shelfLife }}</td>
                    <td>{{ goods.minStockAlert }}</td>
                    <td>{{ goods.maxStockAlert }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div v-else class="no-goods">该分类下暂无商品</div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>
    <div v-else-if="!loading" class="no-data">暂无分类数据</div>

    <div v-if="pageData.totalPages && pageData.totalPages > 1" class="pagination">
      <button @click="changePage(pageData.page - 1)" :disabled="pageData.page <= 1">上一页</button>
      <span>第 {{ pageData.page }} / {{ pageData.totalPages }} 页，共 {{ pageData.total }} 条</span>
      <button @click="changePage(pageData.page + 1)" :disabled="pageData.page >= pageData.totalPages">下一页</button>
      <select v-model="pageSize" @change="handlePageSizeChange">
        <option :value="5">5条/页</option>
        <option :value="10" selected>10条/页</option>
        <option :value="20">20条/页</option>
        <option :value="50">50条/页</option>
      </select>
    </div>

    <!-- 添加/编辑分类弹窗 -->
    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ showAddModal ? '添加分类' : '编辑分类' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>分类名称</label>
            <input v-model="form.categoryName" type="text" required />
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
import { findAllCategories, findCategoryByName, addGoodsCategory, updateGoodsCategory, updateCategoryStatus, getCategoryByPage, searchCategoryByPage } from '../api/goodsCategory'
import { findAllGoods, findGoodsByCategory } from '../api/goods'

export default {
  name: 'GoodsCategoryManageView',
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
      allGoods: [],
      activeCategoryNames: [],
      loading: false,
      errorMessage: '',
      successMessage: '',
      searchValue: '',
      showAddModal: false,
      showEditModal: false,
      form: {
        id: null,
        categoryName: ''
      },
      userRole: ''
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('userRole')
    this.loadAllCategories(true)
    this.loadAllGoods()
  },
  methods: {
    clearMessages() {
      this.errorMessage = ''
      this.successMessage = ''
    },
    async loadAllCategories(resetPage = false) {
      this.clearMessages()
      this.searchValue = ''
      if (resetPage) {
        this.pageData.page = 1
      }
      this.loading = true
      try {
        const response = await getCategoryByPage(this.pageData.page, this.pageSize)
        if (response.code === 200) {
          this.pageData = response.data
        } else {
          this.errorMessage = response.message || '加载分类失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('加载分类错误:', error)
      } finally {
        this.loading = false
      }
    },
    async loadAllGoods() {
      try {
        const response = await findAllGoods()
        if (response.code === 200) {
          this.allGoods = response.data
        }
      } catch (error) {
        console.error('加载商品错误:', error)
      }
    },
    async handleSearch() {
      if (!this.searchValue.trim()) {
        await this.loadAllCategories()
        return
      }
      this.clearMessages()
      this.pageData.page = 1
      this.loading = true
      try {
        const response = await searchCategoryByPage(this.searchValue, this.pageData.page, this.pageSize)
        if (response.code === 200) {
          this.pageData = response.data
        } else {
          this.errorMessage = response.message || '搜索失败'
          this.pageData.records = []
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('搜索分类错误:', error)
      } finally {
        this.loading = false
      }
    },
    getCategoryGoods(categoryId) {
      return this.allGoods.filter(goods => goods.categoryId === categoryId)
    },
    getGoodsCount(categoryId) {
      return this.getCategoryGoods(categoryId).length
    },
    isLowStock(goods) {
      return goods.stockQuantity <= goods.minStockAlert
    },
    isHighStock(goods) {
      return goods.stockQuantity >= goods.maxStockAlert
    },
    editCategory(category) {
      this.form = {
        id: category.id,
        categoryName: category.categoryName
      }
      this.showEditModal = true
    },
    async handleSubmit() {
      this.clearMessages()
      this.loading = true
      try {
        let response
        if (this.showAddModal) {
          response = await addGoodsCategory(this.form)
        } else {
          response = await updateGoodsCategory(this.form)
        }
        if (response.code === 200) {
          this.successMessage = response.message || '操作成功'
          this.closeModal()
          await this.loadAllCategories(true)
        } else {
          this.errorMessage = response.message || '操作失败'
        }
      } catch (error) {
        this.errorMessage = '网络错误，请稍后重试'
        console.error('操作分类错误:', error)
      } finally {
        this.loading = false
      }
    },
    async handleUpdateStatus(category) {
      try {
        await ElMessageBox.confirm(
          `确定要删除分类 ${category.categoryName}吗？`,
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
          const response = await updateCategoryStatus(category.id, 1)
          if (response.code === 200) {
            this.successMessage = response.message || '删除成功'
            await this.loadAllCategories()
          } else {
            this.errorMessage = response.message || '删除失败'
          }
        } catch (error) {
          this.errorMessage = '网络错误，请稍后重试'
          console.error('删除分类错误:', error)
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
        categoryName: ''
      }
    },
    async changePage(page) {
      if (page < 1 || page > this.pageData.totalPages) return
      this.pageData.page = page
      if (this.searchValue) {
        await this.handleSearch()
      } else {
        await this.loadAllCategories()
      }
    },
    async handlePageSizeChange() {
      this.pageData.page = 1
      this.pageData.pageSize = this.pageSize
      if (this.searchValue) {
        await this.handleSearch()
      } else {
        await this.loadAllCategories()
      }
    }
  }
}
</script>

<style scoped>
.category-manage-view {
  padding: 20px;
}

.category-manage-view h2 {
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

.category-list {
  margin-top: 20px;
}

.category-title {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
}

.category-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.category-count {
  font-size: 14px;
  color: #999;
}

.category-actions {
  margin-left: auto;
  display: flex;
  gap: 8px;
}

.btn-edit-small,
.btn-delete-small {
  padding: 4px 12px;
  font-size: 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  color: white;
}

.btn-edit-small {
  background-color: #2196F3;
}

.btn-edit-small:hover {
  background-color: #1976D2;
}

.btn-delete-small {
  background-color: #f44336;
}

.btn-delete-small:hover {
  background-color: #d32f2f;
}

.goods-in-category {
  padding: 10px 0;
}

.no-goods {
  text-align: center;
  padding: 30px;
  color: #999;
}

.goods-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.goods-table th,
.goods-table td {
  padding: 10px 12px;
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

.form-group input {
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

/* Element Plus 折叠面板样式覆盖 */
:deep(.el-collapse) {
  border: none;
}

:deep(.el-collapse-item__header) {
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 8px;
  padding-left: 20px;
  padding-right: 20px;
}

:deep(.el-collapse-item__wrap) {
  border: 1px solid #ddd;
  border-top: none;
  border-radius: 0 0 4px 4px;
  margin-top: -8px;
  margin-bottom: 8px;
  background-color: #fafafa;
}

:deep(.el-collapse-item__content) {
  padding: 0 20px;
}
</style>
