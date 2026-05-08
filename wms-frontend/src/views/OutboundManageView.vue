<template>
  <div class="outbound-manage-container">
    <h2>出库管理</h2>
    
    <!-- 搜索和筛选 -->
    <div class="search-filter">
      <div class="search-item">
        <select v-model="searchType" class="search-type-select">
          <option value="outboundId">出库单号</option>
          <option value="warehouseName">仓库名称</option>
          <option value="goodsName">商品名称</option>
          <option value="batchNo">批次号</option>
        </select>
        <input type="text" v-model="searchValue" placeholder="输入搜索内容">
        <button class="search-btn" @click="searchOutboundDetails">搜索</button>
        <button class="reset-btn" @click="resetSearch">重置</button>
      </div>
    </div>
    
    <!-- 新增出库按钮 -->
    <div class="action-buttons">
      <button class="add-btn" @click="openAddDialog">新增出库</button>
    </div>
    
    <!-- 出库明细表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>出库号</th>
            <th>批次号</th>
            <th>仓库名称</th>
            <th>商品名称</th>
            <th>数量</th>
            <th>出库日期</th>
            <th>修改日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in outboundDetails" :key="item.id">
            <td>{{ item.outboundId }}</td>
            <td>{{ item.batchNo }}</td>
            <td>{{ getWarehouseName(item.warehouseCode) }}</td>
            <td>{{ getGoodsName(item.goodsCode) }}</td>
            <td>{{ item.outboundQuantity }}</td>
            <td>{{ formatDate(item.createTime) }}</td>
            <td>{{ formatDate(item.updateTime) }}</td>
            <td>
              <button class="delete-btn" @click="confirmDelete(item.outboundId)">删除</button>
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
    
    <!-- 新增出库对话框 -->
    <div v-if="showAddDialog" class="dialog-overlay">
      <div class="dialog">
        <h3>新增出库</h3>
        <form @submit.prevent="addOutboundDetail">
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
            <input type="hidden" v-model="newOutboundDetail.goodsCode">
            <div class="selected-value" v-if="newOutboundDetail.goodsCode">
              已选择: {{ getGoodsName(newOutboundDetail.goodsCode) }}
            </div>
          </div>
          <div class="form-item">
            <label for="outboundQuantity">出库数量:</label>
            <input type="number" id="outboundQuantity" v-model.number="newOutboundDetail.outboundQuantity" required min="1">
          </div>
          <div class="form-item">
            <label for="batchSearch">批次号:</label>
            <div class="select-with-search">
              <input 
                type="text" 
                id="batchSearch" 
                v-model="batchSearch" 
                placeholder="点击选择批次号"
                @input="filterBatches"
                @click="handleBatchClick"
              >
              <div class="select-dropdown" v-if="showBatchDropdown">
                <div 
                  v-for="batch in filteredBatches" 
                  :key="batch.batchNumber"
                  class="select-option"
                  @click.stop="selectBatch(batch)"
                >
                  <div class="batch-item">
                    <div class="batch-no">{{ batch.batchNumber }}</div>
                    <div class="batch-info">
                      <span>商品: {{ getGoodsName(batch.goodsCode) }}</span>
                      <span>仓库: {{ getWarehouseName(batch.warehouseCode) }}</span>
                      <span>剩余: {{ batch.remainingQuantity }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <input type="hidden" v-model="newOutboundDetail.batchNo">
            <div class="selected-value" v-if="newOutboundDetail.batchNo">
              已选择: {{ newOutboundDetail.batchNo }}
            </div>
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
            <input type="hidden" v-model="newOutboundDetail.warehouseCode">
            <div class="selected-value" v-if="newOutboundDetail.warehouseCode">
              已选择: {{ getWarehouseName(newOutboundDetail.warehouseCode) }}
            </div>
          </div>
          <div class="dialog-buttons">
            <button type="submit" class="submit-btn" :disabled="!newOutboundDetail.goodsCode || !newOutboundDetail.warehouseCode || !newOutboundDetail.batchNo">提交</button>
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
  findAllOutboundDetails, 
  addOutboundDetail as apiAddOutboundDetail, 
  deleteOutboundDetail as apiDeleteOutboundDetail,
  getOutboundDetailsByPage,
  getOutboundDetailsByOutboundIdPage,
  getOutboundDetailsByWarehouseCodePage,
  getOutboundDetailsByGoodsCodePage,
  getOutboundDetailsByBatchNoPage,
  findAllBatches,
  findBatchesByGoodsAndWarehouse
} from '../api/inboundOutbound.js'
import { findAllWarehouses } from '../api/warehouse.js'
import { findAllGoods } from '../api/goods.js'

export default {
  name: 'OutboundManageView',
  data() {
    return {
      outboundDetails: [],
      warehouses: [],
      goods: [],
      searchType: 'outboundId',
      searchValue: '',
      showAddDialog: false,
      showDeleteDialog: false,
      currentPage: 1,
      pageSize: 10,
      totalPages: 1,
      totalItems: 0,
      deleteOutboundId: '',
      newOutboundDetail: {
        goodsCode: '',
        outboundQuantity: 1,
        batchNo: '',
        warehouseCode: ''
      },
      // 商品搜索和选择
      goodsSearch: '',
      filteredGoods: [],
      showGoodsDropdown: false,
      // 仓库搜索和选择
      warehouseSearch: '',
      filteredWarehouses: [],
      showWarehouseDropdown: false,
      // 批次相关
      batches: [],
      batchSearch: '',
      filteredBatches: [],
      showBatchDropdown: false
    }
  },
  async mounted() {
    // 串行调用API，确保使用同一个会话
    await this.loadOutboundDetails()
    await this.loadWarehouses()
    await this.loadGoods()
    await this.loadAllBatches()
    // 添加点击外部关闭下拉菜单的事件监听器
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeUnmount() {
    // 移除事件监听器
    document.removeEventListener('click', this.handleClickOutside)
  },
  methods: {
    async loadOutboundDetails() {
      try {
        const response = await getOutboundDetailsByPage(this.currentPage, this.pageSize)
        if (response.code === 200) {
          this.outboundDetails = response.data.records || []
          this.totalItems = response.data.total || 0
          this.totalPages = response.data.totalPages || 1
        } else {
          console.error('获取出库明细失败:', response.message)
        }
      } catch (error) {
        console.error('获取出库明细错误:', error)
      }
    },
    async loadWarehouses() {
      try {
        const response = await findAllWarehouses()
        if (response.code === 200) {
          this.warehouses = response.data || []
          // 更新过滤后的仓库列表
          this.filteredWarehouses = this.warehouses
        }
      } catch (error) {
        console.error('获取仓库数据错误:', error)
      }
    },
    async loadGoods() {
      try {
        const response = await findAllGoods()
        if (response.code === 200) {
          this.goods = response.data || []
          // 更新过滤后的商品列表
          this.filteredGoods = this.goods
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
          this.loadOutboundDetails()
        }
      }
    },
    async searchOutboundDetails() {
      // 根据搜索类型和值进行搜索
      this.currentPage = 1
      await this.loadDataBySearch()
    },
    resetSearch() {
      this.searchType = 'outboundId'
      this.searchValue = ''
      this.currentPage = 1
      this.loadOutboundDetails()
    },
    async loadDataBySearch() {
      try {
        let response
        
        switch (this.searchType) {
          case 'outboundId':
            response = await getOutboundDetailsByOutboundIdPage(this.searchValue, this.currentPage, this.pageSize)
            break
          case 'warehouseName':
            // 根据仓库名称找到对应的仓库编码
            const warehouse = this.warehouses.find(w => 
              w.warehouseName.toLowerCase().includes(this.searchValue.toLowerCase())
            )
            if (warehouse) {
              response = await getOutboundDetailsByWarehouseCodePage(warehouse.warehouseCode, this.currentPage, this.pageSize)
            } else {
              this.outboundDetails = []
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
              response = await getOutboundDetailsByGoodsCodePage(good.goodsCode, this.currentPage, this.pageSize)
            } else {
              this.outboundDetails = []
              this.totalItems = 0
              this.totalPages = 1
              return
            }
            break
          case 'batchNo':
            response = await getOutboundDetailsByBatchNoPage(this.searchValue, this.currentPage, this.pageSize)
            break
          default:
            this.loadOutboundDetails()
            return
        }
        
        if (response.code === 200) {
          this.outboundDetails = response.data.records || []
          this.totalItems = response.data.total || 0
          this.totalPages = response.data.totalPages || 1
        } else {
          console.error('搜索出库明细失败:', response.message)
          this.outboundDetails = []
          this.totalItems = 0
          this.totalPages = 1
        }
      } catch (error) {
        console.error('搜索出库明细错误:', error)
        this.outboundDetails = []
        this.totalItems = 0
        this.totalPages = 1
      }
    },
    async addOutboundDetail() {
      try {
        const response = await apiAddOutboundDetail(this.newOutboundDetail)
        if (response.code === 200) {
          ElMessage.success('新增出库成功')
          this.showAddDialog = false
          this.resetAddForm()
          this.loadOutboundDetails()
        } else {
          ElMessage.error('新增出库失败: ' + response.message)
        }
      } catch (error) {
        console.error('新增出库错误:', error)
        ElMessage.error('新增出库失败')
      }
    },
    async confirmDelete(outboundId) {
      try {
        await ElMessageBox.confirm(
          '确定要删除该出库记录吗？',
          '确认操作',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        this.deleteOutboundId = outboundId
        await this.deleteOutboundDetail()
      } catch {
        // 用户取消，不做任何处理
      }
    },
    async deleteOutboundDetail() {
      try {
        const response = await apiDeleteOutboundDetail(this.deleteOutboundId)
        if (response.code === 200) {
          ElMessage.success('删除出库成功')
          this.showDeleteDialog = false
          this.loadOutboundDetails()
        } else {
          ElMessage.error('删除出库失败: ' + response.message)
        }
      } catch (error) {
        console.error('删除出库错误:', error)
        ElMessage.error('删除出库失败')
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
      this.newOutboundDetail.goodsCode = good.goodsCode
      this.goodsSearch = good.goodsName
      this.showGoodsDropdown = false
      // 改变商品时清空批次号
      this.clearBatch()
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
      this.newOutboundDetail.warehouseCode = warehouse.warehouseCode
      this.warehouseSearch = warehouse.warehouseName
      this.showWarehouseDropdown = false
      // 改变仓库时清空批次号
      this.clearBatch()
    },
    // 加载所有批次数据
    async loadAllBatches() {
      try {
        const response = await findAllBatches()
        if (response.code === 200) {
          // 只显示剩余数量大于0的批次
          this.batches = (response.data || []).filter(batch => batch.remainingQuantity > 0)
          this.filteredBatches = this.batches
        }
      } catch (error) {
        console.error('获取所有批次数据错误:', error)
      }
    },
    // 清空批次号
    clearBatch() {
      this.newOutboundDetail.batchNo = ''
      this.batchSearch = ''
      this.filteredBatches = this.batches
      this.showBatchDropdown = false
    },
    // 批次搜索和选择
    filterBatches() {
      // 先根据已选择的商品和仓库获取基础批次列表
      let baseBatches = this.batches
      
      if (this.newOutboundDetail.goodsCode && this.newOutboundDetail.warehouseCode) {
        baseBatches = this.batches.filter(batch => 
          batch.goodsCode === this.newOutboundDetail.goodsCode &&
          batch.warehouseCode === this.newOutboundDetail.warehouseCode
        )
      } else if (this.newOutboundDetail.goodsCode) {
        baseBatches = this.batches.filter(batch => 
          batch.goodsCode === this.newOutboundDetail.goodsCode
        )
      } else if (this.newOutboundDetail.warehouseCode) {
        baseBatches = this.batches.filter(batch => 
          batch.warehouseCode === this.newOutboundDetail.warehouseCode
        )
      }
      
      // 再根据搜索内容过滤
      if (this.batchSearch) {
        this.filteredBatches = baseBatches.filter(batch => 
          batch.batchNumber.toLowerCase().includes(this.batchSearch.toLowerCase())
        )
      } else {
        this.filteredBatches = baseBatches
      }
      this.showBatchDropdown = true
    },
    selectBatch(batch) {
      this.newOutboundDetail.batchNo = batch.batchNumber
      this.batchSearch = batch.batchNumber
      this.showBatchDropdown = false
      
      // 自动设置商品
      if (batch.goodsCode) {
        this.newOutboundDetail.goodsCode = batch.goodsCode
        const good = this.goods.find(g => g.goodsCode === batch.goodsCode)
        this.goodsSearch = good ? good.goodsName : batch.goodsCode
      }
      
      // 自动设置仓库
      if (batch.warehouseCode) {
        this.newOutboundDetail.warehouseCode = batch.warehouseCode
        const warehouse = this.warehouses.find(w => w.warehouseCode === batch.warehouseCode)
        this.warehouseSearch = warehouse ? warehouse.warehouseName : batch.warehouseCode
      }
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
      this.newOutboundDetail = {
        goodsCode: '',
        outboundQuantity: 1,
        batchNo: '',
        warehouseCode: ''
      }
      this.goodsSearch = ''
      this.filteredGoods = this.goods
      this.showGoodsDropdown = false
      this.warehouseSearch = ''
      this.filteredWarehouses = this.warehouses
      this.showWarehouseDropdown = false
      this.batchSearch = ''
      this.filteredBatches = this.batches
      this.showBatchDropdown = false
    },
    // 处理点击外部关闭下拉菜单
    handleClickOutside(event) {
      // 检查点击的元素是否在商品搜索框或下拉菜单内部
      const goodsSearch = document.querySelector('#goodsSearch')
      const warehouseSearch = document.querySelector('#warehouseSearch')
      const batchSearch = document.querySelector('#batchSearch')
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
      
      // 检查是否点击在批次搜索框内
      if (batchSearch?.contains(event.target)) {
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
        this.showBatchDropdown = false
      }
    },
    // 处理商品输入框点击
    handleGoodsClick(event) {
      // 确保filteredGoods包含所有商品
      this.filteredGoods = this.goods
      // 关闭其他下拉菜单
      this.showWarehouseDropdown = false
      this.showBatchDropdown = false
      // 显示下拉菜单
      this.showGoodsDropdown = true
      // 阻止事件冒泡，防止立即被handleClickOutside关闭
      event.stopPropagation()
    },
    // 处理仓库输入框点击
    handleWarehouseClick(event) {
      // 确保filteredWarehouses包含所有仓库
      this.filteredWarehouses = this.warehouses
      // 关闭其他下拉菜单
      this.showGoodsDropdown = false
      this.showBatchDropdown = false
      // 显示下拉菜单
      this.showWarehouseDropdown = true
      // 阻止事件冒泡，防止立即被handleClickOutside关闭
      event.stopPropagation()
    },
    // 处理批次输入框点击
    async handleBatchClick(event) {
      // 根据已选择的商品和仓库过滤批次
      if (this.newOutboundDetail.goodsCode && this.newOutboundDetail.warehouseCode) {
        // 同时选择了商品和仓库，调用接口查询对应批次
        try {
          const response = await findBatchesByGoodsAndWarehouse(
            this.newOutboundDetail.goodsCode,
            this.newOutboundDetail.warehouseCode
          )
          if (response.code === 200) {
            this.filteredBatches = (response.data || []).filter(batch => batch.remainingQuantity > 0)
          }
        } catch (error) {
          console.error('查询批次错误:', error)
          this.filteredBatches = this.batches
        }
      } else if (this.newOutboundDetail.goodsCode) {
        // 只选择了商品，过滤该商品的批次
        this.filteredBatches = this.batches.filter(batch => 
          batch.goodsCode === this.newOutboundDetail.goodsCode
        )
      } else if (this.newOutboundDetail.warehouseCode) {
        // 只选择了仓库，过滤该仓库的批次
        this.filteredBatches = this.batches.filter(batch => 
          batch.warehouseCode === this.newOutboundDetail.warehouseCode
        )
      } else {
        // 都没选择，显示所有批次
        this.filteredBatches = this.batches
      }
      
      // 关闭其他下拉菜单
      this.showGoodsDropdown = false
      this.showWarehouseDropdown = false
      // 显示下拉菜单
      this.showBatchDropdown = true
      // 阻止事件冒泡，防止立即被handleClickOutside关闭
      event.stopPropagation()
    }
  }
}
</script>

<style scoped>
.outbound-manage-container {
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
  
  .batch-item {
    display: flex;
    flex-direction: column;
    gap: 4px;
  }
  
  .batch-no {
    font-weight: bold;
    color: #333;
  }
  
  .batch-info {
    display: flex;
    flex-direction: column;
    gap: 2px;
    font-size: 12px;
    color: #666;
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
