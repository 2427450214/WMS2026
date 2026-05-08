<template>
  <div class="inbound-manage-container">
    <h2>入库管理</h2>
    
    <!-- 搜索和筛选 -->
    <div class="search-filter">
      <div class="search-item">
        <select v-model="searchType" class="search-type-select">
          <option value="inboundId">入库单号</option>
          <option value="warehouseName">仓库名称</option>
          <option value="goodsName">商品名称</option>
          <option value="batchNo">批次号</option>
        </select>
        <input type="text" v-model="searchValue" placeholder="输入搜索内容">
        <button class="search-btn" @click="searchInboundDetails">搜索</button>
        <button class="reset-btn" @click="resetSearch">重置</button>
      </div>
    </div>
    
    <!-- 新增入库按钮 -->
    <div class="action-buttons">
      <button class="add-btn" @click="openAddDialog">新增入库</button>
    </div>
    
    <!-- 入库明细表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>入库号</th>
            <th>批次号</th>
            <th>仓库名称</th>
            <th>商品名称</th>
            <th>数量</th>
            <th>入库日期</th>
            <th>修改日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in inboundDetails" :key="item.id">
            <td>{{ item.inboundId }}</td>
            <td>{{ item.batchNo }}</td>
            <td>{{ getWarehouseName(item.warehouseCode) }}</td>
            <td>{{ getGoodsName(item.goodsCode) }}</td>
            <td>{{ item.inboundQuantity }}</td>
            <td>{{ formatDate(item.createTime) }}</td>
            <td>{{ formatDate(item.updateTime) }}</td>
            <td>
              <button class="delete-btn" @click="confirmDelete(item.inboundId)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <!-- 分页 -->
    <div class="pagination">
      <button @click="changePage(1)" :disabled="currentPage === 1">首页</button>
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
      <button @click="changePage(totalPages)" :disabled="currentPage === totalPages">末页</button>
    </div>
    
    <!-- 新增入库对话框 -->
    <div v-if="showAddDialog" class="dialog-overlay">
      <div class="dialog">
        <h3>新增入库</h3>
        <form @submit.prevent="addInboundDetail">
          <div class="form-item">
            <label for="goodsSearch">商品:</label>
            <div class="select-with-search">
              <input 
                type="text" 
                id="goodsSearch" 
                v-model="goodsSearch" 
                placeholder="输入商品名称搜索"
                @input="filterGoods"
                @click="handleGoodsClick"
              >
              <div class="select-dropdown" v-if="showGoodsDropdown">
                <div 
                  v-for="good in filteredGoods" 
                  :key="good.goodsCode"
                  class="select-option"
                  @click.stop="selectGoods(good)"
                >
                  {{ good.goodsName }} ({{ good.goodsCode }})
                </div>
              </div>
            </div>
            <input type="hidden" v-model="newInboundDetail.goodsCode">
            <div class="selected-value" v-if="newInboundDetail.goodsCode">
              已选择: {{ getGoodsName(newInboundDetail.goodsCode) }}
            </div>
          </div>
          <div class="form-item">
            <label for="inboundQuantity">入库数量:</label>
            <input type="number" id="inboundQuantity" v-model.number="newInboundDetail.inboundQuantity" required min="1">
          </div>
          <div class="form-item">
            <label for="warehouseSearch">仓库:</label>
            <div class="select-with-search">
              <input 
                type="text" 
                id="warehouseSearch" 
                v-model="warehouseSearch" 
                placeholder="输入仓库名称搜索"
                @input="filterWarehouses"
                @click="handleWarehouseClick"
              >
              <div class="select-dropdown" v-if="showWarehouseDropdown">
                <div 
                  v-for="warehouse in filteredWarehouses" 
                  :key="warehouse.warehouseCode"
                  class="select-option"
                  @click.stop="selectWarehouse(warehouse)"
                >
                  {{ warehouse.warehouseName }} ({{ warehouse.warehouseCode }})
                </div>
              </div>
            </div>
            <input type="hidden" v-model="newInboundDetail.warehouseCode">
            <div class="selected-value" v-if="newInboundDetail.warehouseCode">
              已选择: {{ getWarehouseName(newInboundDetail.warehouseCode) }}
            </div>
          </div>
          <div class="dialog-buttons">
            <button type="submit" class="submit-btn" :disabled="!newInboundDetail.goodsCode || !newInboundDetail.warehouseCode">提交</button>
            <button type="button" class="cancel-btn" @click="closeAddDialog">取消</button>
          </div>
        </form>
      </div>
    </div>
    

  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  findAllInboundDetails, 
  addInboundDetail as apiAddInboundDetail, 
  deleteInboundDetail as apiDeleteInboundDetail,
  getInboundDetailsByPage,
  getInboundDetailsByInboundIdPage,
  getInboundDetailsByWarehouseCodePage,
  getInboundDetailsByGoodsCodePage,
  getInboundDetailsByBatchNoPage
} from '../api/inboundOutbound.js'
import { findAllWarehouses } from '../api/warehouse.js'
import { findAllGoods } from '../api/goods.js'

export default {
  name: 'InboundManageView',
  data() {
    return {
      inboundDetails: [],
      warehouses: [],
      goods: [],
      searchType: 'inboundId',
      searchValue: '',
      showAddDialog: false,
      showDeleteDialog: false,
      currentPage: 1,
      pageSize: 10,
      totalPages: 1,
      totalItems: 0,
      deleteInboundId: '',
      newInboundDetail: {
        goodsCode: '',
        inboundQuantity: 1,
        warehouseCode: ''
      },
      // 商品搜索和选择
      goodsSearch: '',
      filteredGoods: [],
      showGoodsDropdown: false,
      // 仓库搜索和选择
      warehouseSearch: '',
      filteredWarehouses: [],
      showWarehouseDropdown: false
    }
  },
  async mounted() {
    console.log('组件挂载，开始加载数据')
    // 串行调用API，确保使用同一个会话
    await this.loadInboundDetails()
    await this.loadWarehouses()
    await this.loadGoods()
    // 添加点击外部关闭下拉菜单的事件监听器
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeUnmount() {
    // 移除事件监听器
    document.removeEventListener('click', this.handleClickOutside)
  },
  methods: {
    async loadInboundDetails() {
      try {
        const response = await getInboundDetailsByPage(this.currentPage, this.pageSize)
        if (response.code === 200) {
          this.inboundDetails = response.data.records || []
          this.totalItems = response.data.total || 0
          this.totalPages = response.data.totalPages || 1
        } else {
          console.error('获取入库明细失败:', response.message)
        }
      } catch (error) {
        console.error('获取入库明细错误:', error)
      }
    },
    async loadWarehouses() {
      try {
        console.log('开始加载仓库数据')
        const response = await findAllWarehouses()
        console.log('仓库数据响应:', response)
        // 检查响应结构
        if (response && response.code === 200) {
          // 确保data是数组
          this.warehouses = Array.isArray(response.data) ? response.data : []
          console.log('仓库数据:', this.warehouses)
          console.log('仓库数据长度:', this.warehouses.length)
          // 更新过滤后的仓库列表
          this.filteredWarehouses = this.warehouses
          console.log('过滤后的仓库数据:', this.filteredWarehouses)
          console.log('过滤后的仓库数据长度:', this.filteredWarehouses.length)
        } else {
          console.error('获取仓库数据失败:', response?.message || '未知错误')
          // 确保warehouses和filteredWarehouses是数组
          this.warehouses = []
          this.filteredWarehouses = []
        }
      } catch (error) {
        console.error('获取仓库数据错误:', error)
        // 确保warehouses和filteredWarehouses是数组
        this.warehouses = []
        this.filteredWarehouses = []
      }
    },
    async loadGoods() {
      try {
        console.log('开始加载商品数据')
        const response = await findAllGoods()
        console.log('商品数据响应:', response)
        if (response.code === 200) {
          this.goods = response.data || []
          console.log('商品数据:', this.goods)
          // 更新过滤后的商品列表
          this.filteredGoods = this.goods
          console.log('过滤后的商品数据:', this.filteredGoods)
        } else {
          console.error('获取商品数据失败:', response.message)
        }
      } catch (error) {
        console.error('获取商品数据错误:', error)
      }
    },
    getWarehouseName(warehouseCode) {
      const warehouse = this.warehouses.find(w => w.warehouseCode === warehouseCode)
      return warehouse ? warehouse.warehouseName : warehouseCode
    },
    getGoodsName(goodsCode) {
      const good = this.goods.find(g => g.goodsCode === goodsCode)
      return good ? good.goodsName : goodsCode
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString()
    },
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page
        if (this.searchValue) {
          this.loadDataBySearch()
        } else {
          this.loadInboundDetails()
        }
      }
    },
    async searchInboundDetails() {
      // 根据搜索类型和值进行搜索
      this.currentPage = 1
      await this.loadDataBySearch()
    },
    resetSearch() {
      this.searchType = 'inboundId'
      this.searchValue = ''
      this.currentPage = 1
      this.loadInboundDetails()
    },
    async loadDataBySearch() {
      try {
        let response
        
        switch (this.searchType) {
          case 'inboundId':
            response = await getInboundDetailsByInboundIdPage(this.searchValue, this.currentPage, this.pageSize)
            break
          case 'warehouseName':
            // 根据仓库名称找到对应的仓库编码
            const warehouse = this.warehouses.find(w => 
              w.warehouseName.toLowerCase().includes(this.searchValue.toLowerCase())
            )
            if (warehouse) {
              response = await getInboundDetailsByWarehouseCodePage(warehouse.warehouseCode, this.currentPage, this.pageSize)
            } else {
              this.inboundDetails = []
              this.totalItems = 0
              this.totalPages = 1
              return
            }
            break
          case 'goodsName':
            // 根据商品名称找到对应的商品编码
            const good = this.goods.find(g => 
              g.goodsName.toLowerCase().includes(this.searchValue.toLowerCase())
            )
            if (good) {
              response = await getInboundDetailsByGoodsCodePage(good.goodsCode, this.currentPage, this.pageSize)
            } else {
              this.inboundDetails = []
              this.totalItems = 0
              this.totalPages = 1
              return
            }
            break
          case 'batchNo':
            response = await getInboundDetailsByBatchNoPage(this.searchValue, this.currentPage, this.pageSize)
            break
          default:
            this.loadInboundDetails()
            return
        }
        
        if (response.code === 200) {
          this.inboundDetails = response.data.records || []
          this.totalItems = response.data.total || 0
          this.totalPages = response.data.totalPages || 1
        } else {
          console.error('搜索入库明细失败:', response.message)
          this.inboundDetails = []
          this.totalItems = 0
          this.totalPages = 1
        }
      } catch (error) {
        console.error('搜索入库明细错误:', error)
        this.inboundDetails = []
        this.totalItems = 0
        this.totalPages = 1
      }
    },
    async addInboundDetail() {
      try {
        const response = await apiAddInboundDetail(this.newInboundDetail)
        if (response.code === 200) {
          ElMessage.success('新增入库成功')
          this.showAddDialog = false
          this.resetAddForm()
          this.loadInboundDetails()
        } else {
          ElMessage.error('新增入库失败: ' + response.message)
        }
      } catch (error) {
        console.error('新增入库错误:', error)
        ElMessage.error('新增入库失败')
      }
    },
    async confirmDelete(inboundId) {
      try {
        await ElMessageBox.confirm(
          '确定要删除该入库记录吗？',
          '确认操作',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        this.deleteInboundId = inboundId
        await this.deleteInboundDetail()
      } catch {
        // 用户取消，不做任何处理
      }
    },
    async deleteInboundDetail() {
      try {
        const response = await apiDeleteInboundDetail(this.deleteInboundId)
        if (response.code === 200) {
          ElMessage.success('删除入库成功')
          this.showDeleteDialog = false
          this.loadInboundDetails()
        } else {
          ElMessage.error('删除入库失败: ' + response.message)
        }
      } catch (error) {
        console.error('删除入库错误:', error)
        ElMessage.error('删除入库失败')
      }
    },
    // 商品搜索和选择
    filterGoods() {
      if (this.goodsSearch) {
        this.filteredGoods = this.goods.filter(good => 
          good.goodsName.toLowerCase().includes(this.goodsSearch.toLowerCase())
        )
        this.showGoodsDropdown = true
      } else {
        this.filteredGoods = this.goods
        this.showGoodsDropdown = true
      }
    },
    selectGoods(good) {
      this.newInboundDetail.goodsCode = good.goodsCode
      this.goodsSearch = good.goodsName
      this.showGoodsDropdown = false
    },
    // 仓库搜索和选择
    filterWarehouses() {
      if (this.warehouseSearch) {
        this.filteredWarehouses = this.warehouses.filter(warehouse => 
          warehouse.warehouseName.toLowerCase().includes(this.warehouseSearch.toLowerCase())
        )
        this.showWarehouseDropdown = true
      } else {
        this.filteredWarehouses = this.warehouses
        this.showWarehouseDropdown = true
      }
    },
    selectWarehouse(warehouse) {
      this.newInboundDetail.warehouseCode = warehouse.warehouseCode
      this.warehouseSearch = warehouse.warehouseName
      this.showWarehouseDropdown = false
    },
    // 打开新增对话框
    openAddDialog() {
      this.resetAddForm()
      this.showAddDialog = true
    },
    // 关闭新增对话框
    closeAddDialog() {
      this.showAddDialog = false
      this.resetAddForm()
    },
    // 重置新增表单
    resetAddForm() {
      this.newInboundDetail = {
        goodsCode: '',
        inboundQuantity: 1,
        warehouseCode: ''
      }
      this.goodsSearch = ''
      this.filteredGoods = this.goods
      this.showGoodsDropdown = false
      this.warehouseSearch = ''
      this.filteredWarehouses = this.warehouses
      this.showWarehouseDropdown = false
    },
    // 处理点击外部关闭下拉菜单
    handleClickOutside(event) {
      // 检查点击的元素是否在商品搜索框或下拉菜单内部
      const goodsSearch = document.querySelector('#goodsSearch')
      const warehouseSearch = document.querySelector('#warehouseSearch')
      const dropdowns = document.querySelectorAll('.select-dropdown')
      
      let clickedInside = false
      
      // 检查是否点击在商品搜索框内
      if (goodsSearch?.contains(event.target)) {
        clickedInside = true
      }
      
      // 检查是否点击在仓库搜索框内
      if (warehouseSearch?.contains(event.target)) {
        clickedInside = true
      }
      
      // 检查是否点击在任何下拉菜单内
      dropdowns.forEach(dropdown => {
        if (dropdown?.contains(event.target)) {
          clickedInside = true
        }
      })
      
      if (!clickedInside) {
        this.showGoodsDropdown = false
        this.showWarehouseDropdown = false
      }
    },
    // 处理商品输入框点击
    handleGoodsClick(event) {
      console.log('点击商品输入框')
      console.log('当前商品数据:', this.goods)
      console.log('当前商品数据长度:', this.goods.length)
      // 确保filteredGoods包含所有商品
      this.filteredGoods = this.goods
      console.log('过滤后的商品数据:', this.filteredGoods)
      // 关闭仓库下拉菜单
      this.showWarehouseDropdown = false
      // 显示商品下拉菜单
      this.showGoodsDropdown = true
      console.log('显示商品下拉菜单:', this.showGoodsDropdown)
      // 阻止事件冒泡，防止立即被handleClickOutside关闭
      event.stopPropagation()
    },
    // 处理仓库输入框点击
    handleWarehouseClick(event) {
      console.log('点击仓库输入框')
      console.log('当前仓库数据:', this.warehouses)
      console.log('当前仓库数据长度:', this.warehouses.length)
      // 确保filteredWarehouses包含所有仓库
      this.filteredWarehouses = this.warehouses
      console.log('过滤后的仓库数据:', this.filteredWarehouses)
      // 关闭商品下拉菜单
      this.showGoodsDropdown = false
      // 显示仓库下拉菜单
      this.showWarehouseDropdown = true
      console.log('显示仓库下拉菜单:', this.showWarehouseDropdown)
      // 阻止事件冒泡，防止立即被handleClickOutside关闭
      event.stopPropagation()
    }
  }
}
</script>

<style scoped>
.inbound-manage-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.search-filter {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.search-item label {
  font-weight: bold;
  min-width: 80px;
}

.search-item input {
    padding: 5px 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    width: 200px;
  }
  
  .search-type-select {
    padding: 5px 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-right: 10px;
  }
  
  /* 搜索选择器样式 */
  .select-with-search {
    position: relative;
    width: 100%;
  }
  
  .select-dropdown {
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    max-height: 200px;
    overflow-y: auto;
    border: 1px solid #ddd;
    border-top: none;
    border-radius: 0 0 4px 4px;
    background-color: white;
    z-index: 1000;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .select-option {
    padding: 8px 12px;
    cursor: pointer;
  }
  
  .select-option:hover {
    background-color: #f0f2f5;
  }
  
  .selected-value {
    margin-top: 5px;
    font-size: 12px;
    color: #666;
  }

.search-btn, .reset-btn, .add-btn, .delete-btn, .submit-btn, .cancel-btn, .confirm-btn {
  padding: 5px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.search-btn, .add-btn, .submit-btn, .confirm-btn {
  background-color: #4CAF50;
  color: white;
}

.reset-btn, .cancel-btn {
  background-color: #f44336;
  color: white;
}

.delete-btn {
  background-color: #ff9800;
  color: white;
}

.action-buttons {
  margin-bottom: 20px;
}

.table-container {
  overflow-x: auto;
  margin-bottom: 20px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th, .data-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.data-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.data-table tr:hover {
  background-color: #f5f5f5;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
}

.pagination button {
  padding: 5px 10px;
  border: 1px solid #ddd;
  background-color: white;
  cursor: pointer;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.dialog-overlay {
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

.dialog {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.dialog h3 {
  margin-top: 0;
  margin-bottom: 20px;
  text-align: center;
}

.form-item {
  margin-bottom: 15px;
}

.form-item label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-item input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.dialog-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
