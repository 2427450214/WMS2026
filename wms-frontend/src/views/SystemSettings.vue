<template>
  <div class="system-settings">
    <h2>系统设置</h2>
    
    <div class="settings-section">
      <h3>首页组件配置</h3>
      <p class="settings-tip">请选择要在首页显示的组件（最多选择4个）</p>
      
      <div class="components-grid">
        <div 
          v-for="(component, index) in availableComponents" 
          :key="component.key"
          class="component-card"
          :class="{ 'selected': selectedComponents.includes(component.key) }"
          @click="toggleComponent(component.key)"
        >
          <div class="component-icon">{{ component.icon }}</div>
          <div class="component-name">{{ component.name }}</div>
          <div class="component-desc">{{ component.description }}</div>
          <div v-if="selectedComponents.includes(component.key)" class="checkmark">✓</div>
        </div>
      </div>
      <div class="fixed-component-hint">
        <span class="hint-icon">📌</span>
        <span>时钟组件固定显示，无需选择</span>
      </div>
    </div>
    
    <div class="actions">
      <button class="btn-reset" @click="resetSettings" :disabled="loading">重置默认</button>
      <button class="btn-save" @click="saveSettings" :disabled="loading">{{ loading ? '保存中...' : '保存设置' }}</button>
    </div>
    
    <div v-if="message" :class="['message', message.type]">{{ message.text }}</div>
  </div>
</template>

<script>
import { getHomepageComponents, setHomepageComponents, resetHomepageComponents } from '../api/system'

export default {
  name: 'SystemSettings',
  data() {
    return {
      selectedComponents: [],
      availableComponents: [
        { key: 'dailyInbound', name: '近7天入库饼图', icon: '📊', description: '展示近一周每个商品入库占比' },
        { key: 'dailyInboundLine', name: '近7天入库趋势', icon: '📈', description: '展示近7天每天入库数量趋势' },
        { key: 'dailyOutbound', name: '近7天出库饼图', icon: '📊', description: '展示近一周每个商品出库占比' },
        { key: 'dailyOutboundLine', name: '近7天出库趋势', icon: '📉', description: '展示近7天每天出库数量趋势' },
        { key: 'topStock', name: '库存前三', icon: '📦', description: '展示库存数量最多的前3个商品' },
        { key: 'expiredWarning', name: '过期商品预警', icon: '🔴', description: '展示已过期的商品批次' },
        { key: 'nearExpiryWarning', name: '即将过期商品', icon: '🟡', description: '展示即将过期的商品批次' },
        { key: 'categoryStock', name: '分类库存统计', icon: '🏷️', description: '按分类统计库存数量' },
        { key: 'stockWarning', name: '库存预警统计', icon: '⚠️', description: '展示库存预警商品' },
        { key: 'stockTurnover', name: '库存周转率', icon: '🔄', description: '统计商品库存周转率' },
        { key: 'hotGoods', name: '热门商品', icon: '🔥', description: '展示热门出库商品' },
        { key: 'slowMovingGoods', name: '滞销商品', icon: '🐌', description: '展示滞销商品' },
        { key: 'dailyStockTrend', name: '每日库存趋势', icon: '📊', description: '展示每日库存变化趋势' },
        { key: 'warehouseStock', name: '仓库库存统计', icon: '🏭', description: '按仓库统计库存' },
        { key: 'userActivity', name: '用户活跃度', icon: '👥', description: '统计用户操作活跃度' },
        { key: 'operationType', name: '操作类型统计', icon: '📝', description: '统计各类操作占比' },
        { key: 'batchRemaining', name: '批次剩余库存', icon: '📦', description: '展示各批次剩余库存' },
        { key: 'batchExpiry', name: '批次过期预警', icon: '⏰', description: '批次过期预警统计' },
        { key: 'categoryInbound', name: '分类入库统计', icon: '📥', description: '按分类统计入库' },
        { key: 'categoryOutbound', name: '分类出库统计', icon: '📤', description: '按分类统计出库' },
        { key: 'dailyInOut', name: '每日出入库统计', icon: '📊', description: '展示每日出入库对比' }
      ],
      message: null,
      loading: false
    }
  },
  mounted() {
    this.loadSettings()
  },
  methods: {
    async loadSettings() {
      try {
        const response = await getHomepageComponents()
        if (response.code === 200) {
          this.selectedComponents = response.data
        }
      } catch (error) {
        console.error('加载设置失败:', error)
        this.selectedComponents = ['dailyInbound', 'expiredWarning', 'topStock', 'dailyOutbound']
      }
    },
    toggleComponent(key) {
      const index = this.selectedComponents.indexOf(key)
      if (index > -1) {
        this.selectedComponents.splice(index, 1)
      } else {
        if (this.selectedComponents.length >= 4) {
          this.showMessage('最多只能选择4个组件', 'error')
          return
        }
        this.selectedComponents.push(key)
      }
    },
    async resetSettings() {
      try {
        const response = await resetHomepageComponents()
        if (response.code === 200) {
          await this.loadSettings()
          this.showMessage('已重置为默认配置', 'success')
        }
      } catch (error) {
        console.error('重置设置失败:', error)
        this.showMessage('重置设置失败', 'error')
      }
    },
    async saveSettings() {
      if (this.selectedComponents.length === 0) {
        this.showMessage('请至少选择1个组件', 'error')
        return
      }
      if (this.selectedComponents.length > 4) {
        this.showMessage('最多只能选择4个组件', 'error')
        return
      }
      this.loading = true
      try {
        const response = await setHomepageComponents(this.selectedComponents)
        if (response.code === 200) {
          this.showMessage('设置保存成功！请刷新首页查看效果', 'success')
        } else {
          this.showMessage(response.message || '保存失败', 'error')
        }
      } catch (error) {
        console.error('保存设置失败:', error)
        this.showMessage('保存设置失败', 'error')
      } finally {
        this.loading = false
      }
    },
    showMessage(text, type) {
      this.message = { text, type }
      setTimeout(() => {
        this.message = null
      }, 3000)
    }
  }
}
</script>

<style scoped>
.system-settings {
  padding: 20px;
}

.system-settings h2 {
  margin: 0 0 20px 0;
  color: #333;
}

.settings-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.settings-section h3 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 16px;
}

.settings-tip {
  margin: 0 0 20px 0;
  color: #666;
  font-size: 13px;
}

.components-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.component-card {
  position: relative;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.2s;
  background: #f9fafb;
}

.component-card:hover {
  border-color: #9ca3af;
  background: #f3f4f6;
}

.component-card.selected {
  border-color: #4CAF50;
  background: #ecfdf5;
}

.component-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.component-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.component-desc {
  font-size: 12px;
  color: #666;
  line-height: 1.4;
}

.checkmark {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 24px;
  height: 24px;
  background: #4CAF50;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: bold;
}

.actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
  justify-content: flex-end;
}

.btn-reset {
  padding: 10px 24px;
  background: #f3f4f6;
  color: #333;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.btn-reset:hover:not(:disabled) {
  background: #e5e7eb;
}

.btn-reset:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-save {
  padding: 10px 24px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.btn-save:hover:not(:disabled) {
  background: #45a049;
}

.btn-save:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.message {
  margin-top: 16px;
  padding: 12px 16px;
  border-radius: 6px;
  font-size: 14px;
}

.message.success {
  background: #dcfce7;
  color: #166534;
}

.message.error {
  background: #fee2e2;
  color: #991b1b;
}

.fixed-component-hint {
  margin-top: 16px;
  padding: 12px 16px;
  background: #f0f9ff;
  border: 1px solid #bae6fd;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.hint-icon {
  font-size: 20px;
}

.fixed-component-hint span:last-child {
  color: #0c4a6e;
  font-size: 14px;
}
</style>
