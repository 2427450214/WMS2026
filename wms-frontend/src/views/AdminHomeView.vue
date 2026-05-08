<template>
  <div class="admin-home">
    <el-container class="admin-container">
      <el-aside :width="isCollapse ? '64px' : '240px'" class="admin-aside">
        <div class="logo" :class="{ 'collapsed': isCollapse }" @click="goToHome">
          <h2 v-if="!isCollapse">WMS管理系统</h2>
          <el-icon v-else class="logo-icon"><HomeFilled /></el-icon>
        </div>
        <div class="menu-container">
          <el-menu
            :default-active="activeMenu"
            :default-openeds="expandedMenus"
            class="admin-menu"
            :collapse="isCollapse"
            @select="handleMenuSelect"
          >
            <el-sub-menu index="1">
              <template #title>
                <el-icon><User /></el-icon>
                <span>用户管理</span>
              </template>
              <el-menu-item index="user-list">查看用户</el-menu-item>
              <el-menu-item index="permission-manage">权限管理</el-menu-item>
            </el-sub-menu>
            
            <el-sub-menu index="2">
              <template #title>
                <el-icon><Goods /></el-icon>
                <span>商品管理</span>
              </template>
              <el-menu-item index="2-1">商品列表</el-menu-item>
              <el-menu-item index="2-2">商品分类</el-menu-item>
              <el-menu-item index="2-3">批次列表</el-menu-item>
            </el-sub-menu>
            
            <el-sub-menu index="3">
              <template #title>
                <el-icon><HomeFilled /></el-icon>
                <span>仓库管理</span>
              </template>
              <el-menu-item index="3-1">仓库列表</el-menu-item>
              <el-menu-item index="3-2">入库管理</el-menu-item>
              <el-menu-item index="3-3">出库管理</el-menu-item>
              <el-menu-item index="3-4">出入库日志</el-menu-item>
            </el-sub-menu>
            
            <el-sub-menu index="4">
              <template #title>
                <el-icon><DataLine /></el-icon>
                <span>数据统计</span>
              </template>
              <el-menu-item index="4-1">商品数据</el-menu-item>
              <el-menu-item index="4-2">其它数据</el-menu-item>
            </el-sub-menu>
            
            <el-menu-item index="operation-log">
              <el-icon><Timer /></el-icon>
              <span>系统操作日志</span>
            </el-menu-item>
          </el-menu>
        </div>
      </el-aside>
      
      <el-container class="admin-main-container">
        <el-header class="admin-header">
          <div class="header-left">
            <el-button
              @click="toggleCollapse"
              class="collapse-btn"
            >
              <el-icon v-if="isCollapse"><Expand /></el-icon>
              <el-icon v-else><Fold /></el-icon>
            </el-button>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="40" class="user-avatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                <span class="user-name">{{ userRole === '管理员' ? userName + '管理员' : userName }}</span>
                <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>
                    个人信息
                  </el-dropdown-item>
                  <el-dropdown-item command="settings">
                    <el-icon><Setting /></el-icon>
                    系统设置
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <el-main class="admin-main">
          <div v-if="$route.path === '/admin/dashboard' || $route.path === '/admin'" class="dashboard-content">
            <div class="welcome-content">
              <h2>欢迎使用 WMS 管理系统</h2>
              <p>这是管理员主页，左侧菜单可以展开/收起</p>
            </div>
            
            <div class="statistics-grid" :class="{ 'menu-expanded': !isCollapse }">
              <!-- 左上角 - 显示第一个选择的组件 -->
              <div v-if="selectedComponents[0]" class="grid-item top-left">
                <component :is="getComponentByKey(selectedComponents[0])" />
              </div>
              <!-- 右上角 - 显示第二个选择的组件 -->
              <div v-if="selectedComponents[1]" class="grid-item top-right">
                <component :is="getComponentByKey(selectedComponents[1])" />
              </div>
              <!-- 左下角 - 显示第三个选择的组件 -->
              <div v-if="selectedComponents[2]" class="grid-item bottom-left">
                <component :is="getComponentByKey(selectedComponents[2])" />
              </div>
              <!-- 右下角 - 显示第四个选择的组件 -->
              <div v-if="selectedComponents[3]" class="grid-item bottom-right">
                <component :is="getComponentByKey(selectedComponents[3])" />
              </div>
              <!-- 时钟组件 -->
              <div v-if="isCollapse" class="grid-item center">
                <Clock />
              </div>
            </div>
          </div>
          
          <div v-else>
            <router-view />
          </div>
          
          <div v-if="$route.path === '/admin/profile'">
            <PersonalInfoView @userNameUpdated="handleUserNameUpdated" />
          </div>
          <div v-if="$route.path === '/admin/settings'">
            <SystemSettings />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { HomeFilled, Fold, Expand, Goods, DataLine, User, Setting, SwitchButton, ArrowDown, Timer } from '@element-plus/icons-vue'
import DailyInboundChart from './utils/DailyInboundChart.vue'
import DailyInboundLineChart from './utils/DailyInboundLineChart.vue'
import DailyOutboundChart from './utils/DailyOutboundChart.vue'
import DailyOutboundLineChart from './utils/DailyOutboundLineChart.vue'
import TopStockChart from './utils/TopStockChart.vue'
import ExpiredWarning from './utils/ExpiredWarning.vue'
import NearExpiryWarning from './utils/NearExpiryWarning.vue'
import Clock from './utils/Clock.vue'
import CategoryStockChart from './utils/CategoryStockChart.vue'
import StockWarning from './utils/StockWarning.vue'
import StockTurnover from './utils/StockTurnover.vue'
import HotGoods from './utils/HotGoods.vue'
import SlowMovingGoods from './utils/SlowMovingGoods.vue'
import DailyStockTrend from './utils/DailyStockTrend.vue'
import WarehouseStock from './utils/WarehouseStock.vue'
import UserActivity from './utils/UserActivity.vue'
import OperationType from './utils/OperationType.vue'
import BatchRemaining from './utils/BatchRemaining.vue'
import BatchExpiry from './utils/BatchExpiry.vue'
import CategoryInbound from './utils/CategoryInbound.vue'
import CategoryOutbound from './utils/CategoryOutbound.vue'
import DailyInOut from './utils/DailyInOut.vue'
import UserListView from './UserListView.vue'
import PermissionManageView from './PermissionManageView.vue'
import PersonalInfoView from './PersonalInfoView.vue'
import SystemSettings from './SystemSettings.vue'
import GoodsManageView from './GoodsManageView.vue'
import GoodsCategoryManageView from './GoodsCategoryManageView.vue'
import BatchManageView from './BatchManageView.vue'
import WarehouseManageView from './WarehouseManageView.vue'
import InboundManageView from './InboundManageView.vue'
import OutboundManageView from './OutboundManageView.vue'
import InOutLogView from './InOutLogView.vue'
import GoodsDataView from './GoodsDataView.vue'
import WarehouseDataView from './WarehouseDataView.vue'
import OperationLogView from './OperationLogView.vue'
import { getHomepageComponents } from '../api/system'

export default {
  name: 'AdminHomeView',
  components: {
    HomeFilled,
    Fold,
    Expand,
    Goods,
    DataLine,
    User,
    Setting,
    SwitchButton,
    ArrowDown,
    Timer,
    DailyInboundChart,
    DailyInboundLineChart,
    DailyOutboundChart,
    DailyOutboundLineChart,
    TopStockChart,
    ExpiredWarning,
    NearExpiryWarning,
    Clock,
    CategoryStockChart,
    StockWarning,
    StockTurnover,
    HotGoods,
    SlowMovingGoods,
    DailyStockTrend,
    WarehouseStock,
    UserActivity,
    OperationType,
    BatchRemaining,
    BatchExpiry,
    CategoryInbound,
    CategoryOutbound,
    DailyInOut,
    UserListView,
    PermissionManageView,
    PersonalInfoView,
    SystemSettings,
    GoodsManageView,
    GoodsCategoryManageView,
    BatchManageView,
    WarehouseManageView,
    InboundManageView,
    OutboundManageView,
    InOutLogView,
    GoodsDataView,
    WarehouseDataView,
    OperationLogView
  },
  data() {
    return {
      isCollapse: true,
      activeMenu: '',
      expandedMenus: [],
      userName: '',
      userRole: '',
      currentComponent: 'dashboard',
      selectedComponents: []
    }
  },
  mounted() {
    const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
    if (!isLoggedIn) {
      this.$router.push('/login')
    } else {
      this.userName = localStorage.getItem('userName') || ''
      this.userRole = localStorage.getItem('userRole') || ''
      this.loadSelectedComponents()
      this.initFromRoute()
    }
  },
  watch: {
    '$route'() {
      this.initFromRoute()
    }
  },
  methods: {
    getComponentByKey(key) {
      const componentMap = {
        dailyInbound: DailyInboundChart,
        dailyInboundLine: DailyInboundLineChart,
        dailyOutbound: DailyOutboundChart,
        dailyOutboundLine: DailyOutboundLineChart,
        topStock: TopStockChart,
        expiredWarning: ExpiredWarning,
        nearExpiryWarning: NearExpiryWarning,
        categoryStock: CategoryStockChart,
        stockWarning: StockWarning,
        stockTurnover: StockTurnover,
        hotGoods: HotGoods,
        slowMovingGoods: SlowMovingGoods,
        dailyStockTrend: DailyStockTrend,
        warehouseStock: WarehouseStock,
        userActivity: UserActivity,
        operationType: OperationType,
        batchRemaining: BatchRemaining,
        batchExpiry: BatchExpiry,
        categoryInbound: CategoryInbound,
        categoryOutbound: CategoryOutbound,
        dailyInOut: DailyInOut
      }
      return componentMap[key] || null
    },
    getGridClass() {
      const count = this.selectedComponents.length
      let gridClass = ''
      if (this.isCollapse) {
        if (count === 1) gridClass = 'grid-1'
        else if (count === 2) gridClass = 'grid-2'
        else if (count === 3) gridClass = 'grid-3'
        else if (count >= 4) gridClass = 'grid-4'
      } else {
        if (count === 1) gridClass = 'grid-1 menu-expanded'
        else if (count === 2) gridClass = 'grid-2 menu-expanded'
        else if (count === 3) gridClass = 'grid-3 menu-expanded'
        else if (count >= 4) gridClass = 'grid-4 menu-expanded'
      }
      return gridClass
    },
    initFromRoute() {
      const path = this.$route.path
      if (path === '/admin/dashboard' || path === '/admin') {
        this.currentComponent = 'dashboard'
        this.activeMenu = ''
      } else if (path === '/admin/user-list') {
        this.currentComponent = 'user-list'
        this.activeMenu = 'user-list'
      } else if (path === '/admin/permission-manage') {
        this.currentComponent = 'permission-manage'
        this.activeMenu = 'permission-manage'
      } else if (path === '/admin/goods-manage') {
        this.currentComponent = 'goods-manage'
        this.activeMenu = '2-1'
        if (!this.expandedMenus.includes('2')) {
          this.expandedMenus.push('2')
        }
      } else if (path === '/admin/goods-category-manage') {
        this.currentComponent = 'goods-category-manage'
        this.activeMenu = '2-2'
        if (!this.expandedMenus.includes('2')) {
          this.expandedMenus.push('2')
        }
      } else if (path === '/admin/batch-manage') {
        this.currentComponent = 'batch-manage'
        this.activeMenu = '2-3'
        if (!this.expandedMenus.includes('2')) {
          this.expandedMenus.push('2')
        }
      } else if (path === '/admin/warehouse-manage') {
        this.currentComponent = 'warehouse-manage'
        this.activeMenu = '3-1'
        if (!this.expandedMenus.includes('3')) {
          this.expandedMenus.push('3')
        }
      } else if (path === '/admin/inbound-manage') {
        this.currentComponent = 'inbound-manage'
        this.activeMenu = '3-2'
        if (!this.expandedMenus.includes('3')) {
          this.expandedMenus.push('3')
        }
      } else if (path === '/admin/outbound-manage') {
        this.currentComponent = 'outbound-manage'
        this.activeMenu = '3-3'
        if (!this.expandedMenus.includes('3')) {
          this.expandedMenus.push('3')
        }
      } else if (path === '/admin/in-out-log') {
        this.currentComponent = 'in-out-log'
        this.activeMenu = '3-4'
        if (!this.expandedMenus.includes('3')) {
          this.expandedMenus.push('3')
        }
      } else if (path === '/admin/goods-data') {
        this.currentComponent = 'goods-data'
        this.activeMenu = '4-1'
        if (!this.expandedMenus.includes('4')) {
          this.expandedMenus.push('4')
        }
      } else if (path === '/admin/warehouse-data') {
        this.currentComponent = 'warehouse-data'
        this.activeMenu = '4-2'
        if (!this.expandedMenus.includes('4')) {
          this.expandedMenus.push('4')
        }
      } else if (path === '/admin/operation-log') {
        this.currentComponent = 'operation-log'
        this.activeMenu = 'operation-log'
      } else if (path === '/admin/profile') {
        this.currentComponent = 'profile'
        this.activeMenu = ''
      } else if (path === '/admin/settings') {
        this.currentComponent = 'settings'
        this.activeMenu = ''
      }
    },
    async loadSelectedComponents() {
      try {
        const response = await getHomepageComponents()
        if (response.code === 200) {
          this.selectedComponents = response.data
        }
      } catch (error) {
        console.error('加载组件配置失败:', error)
        this.selectedComponents = ['dailyInbound', 'expiredWarning', 'topStock', 'dailyOutbound']
      }
    },
    toggleCollapse() {
      this.isCollapse = !this.isCollapse
      if (!this.isCollapse) {
        this.expandedMenus = []
      }
    },
    handleMenuSelect(index) {
      this.activeMenu = index
      if (index === 'user-list') {
        this.$router.push('/admin/user-list')
      } else if (index === 'permission-manage') {
        this.$router.push('/admin/permission-manage')
      } else if (index === '2-1') {
        this.$router.push('/admin/goods-manage')
      } else if (index === '2-2') {
        this.$router.push('/admin/goods-category-manage')
      } else if (index === '2-3') {
        this.$router.push('/admin/batch-manage')
      } else if (index === '3-1') {
        this.$router.push('/admin/warehouse-manage')
      } else if (index === '3-2') {
        this.$router.push('/admin/inbound-manage')
      } else if (index === '3-3') {
        this.$router.push('/admin/outbound-manage')
      } else if (index === '3-4') {
        this.$router.push('/admin/in-out-log')
      } else if (index === '4-1') {
        this.$router.push('/admin/goods-data')
      } else if (index === '4-2') {
        this.$router.push('/admin/warehouse-data')
      } else if (index === 'operation-log') {
        this.$router.push('/admin/operation-log')
      } else {
        this.$router.push('/admin/dashboard')
      }
    },
    handleCommand(command) {
      console.log('用户操作:', command)
      if (command === 'logout') {
        localStorage.removeItem('isLoggedIn')
        localStorage.removeItem('userRole')
        localStorage.removeItem('userName')
        this.$router.push('/login')
      } else if (command === 'profile') {
        this.$router.push('/admin/profile')
      } else if (command === 'settings') {
        this.$router.push('/admin/settings')
      }
    },
    goToHome() {
      this.$router.push('/admin/dashboard')
      this.loadSelectedComponents()
    },
    handleUserNameUpdated(newName) {
      this.userName = newName
    }
  }
}
</script>

<style scoped>
.admin-home {
  width: 100%;
  height: 100vh;
  min-height: 100vh;
  background-color: #f0f2f5;
  display: flex;
  flex-direction: column;
}

.admin-container {
  width: 100%;
  height: 100%;
  display: flex;
}

.admin-aside {
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  transition: width 0.3s;
  overflow-y: hidden;
  height: 100%;
  position: relative;
}

.menu-container {
  height: calc(100% - 60px);
  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.menu-container::-webkit-scrollbar {
  display: none;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #3b82f6 0%, #60a5fa 100%);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  cursor: pointer;
  transition: background 0.3s;
  position: sticky;
  top: 0;
  z-index: 10;
}

.logo:hover {
  background: linear-gradient(135deg, #2563eb 0%, #3b82f6 100%);
}

.logo h2 {
  color: #ffffff;
  font-size: 18px;
  margin: 0;
  font-weight: 600;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.logo.collapsed .logo-icon {
  font-size: 24px;
  color: #ffffff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.admin-menu {
  border: none;
  background-color: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.el-icon {
  cursor: pointer;
  transition: color 0.3s;
  color: #ffffff;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
}

.el-icon:hover {
  color: #e0f2fe;
}

.admin-menu .el-menu-item {
  color: #ffffff;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
}

.admin-menu .el-menu-item span {
  color: #ffffff;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
}

.admin-menu .el-menu-item:hover {
  color: #e0f2fe;
}

.admin-menu .el-menu-item:hover span {
  color: #e0f2fe;
}

.admin-menu .el-menu-item.is-active {
  color: #3b82f6;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
}

.admin-menu .el-menu-item.is-active span {
  color: #3b82f6;
}

.admin-menu .el-sub-menu__title {
  color: #ffffff;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
}

.admin-menu .el-sub-menu__title span {
  color: #ffffff;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
}

.admin-menu .el-sub-menu__title:hover {
  background-color: #dbeafe;
}

.admin-menu .el-sub-menu__title:hover span {
  color: #1e3a8a;
}

.admin-menu .el-sub-menu__icon-arrow {
  color: #ffffff;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
}

.admin-menu .el-sub-menu .el-menu {
  background-color: rgba(255, 255, 255, 0.9);
}

.admin-menu .el-sub-menu .el-menu-item {
  color: #374151;
}

.admin-menu .el-sub-menu .el-menu-item:hover {
  background-color: #dbeafe;
}

.admin-menu .el-sub-menu .el-menu-item.is-active {
  color: #1e3a8a;
  background-color: rgba(30, 58, 138, 0.1);
}

.el-menu--popup {
  background-color: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
}

.el-menu--popup .el-menu-item {
  color: #1f2937;
  font-weight: 500;
}

.el-menu--popup .el-menu-item:hover {
  color: #1e3a8a;
  background-color: #eff6ff;
}

.admin-main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.admin-header {
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  padding-left: 0;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  border-bottom: 1px solid #e8e8e8;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-btn {
  font-size: 20px;
  border: none;
  background: rgba(255, 255, 255, 0.2);
  cursor: pointer;
  color: #7e7c7c;
  padding-left: 10px;
  padding-right: 10px;
  width: 30px;
  height: 30px;
  border-radius: 4px;
  transition: all 0.3s;
}

.collapse-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  color: #7e7c7c;
}

.collapse-btn svg {
  color: #7e7c7c;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f0f0f0;
}

.user-avatar {
  background-color: #1890ff;
}

.user-name {
  margin: 0 10px;
  color: #595959;
  font-size: 14px;
}

.dropdown-icon {
  color: #8c8c8c;
  font-size: 14px;
}

.admin-main {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}

.dashboard-content {
  height: 100%;
}

.welcome-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  margin-bottom: 20px;
}

.welcome-content h2 {
  color: #262626;
  margin-bottom: 10px;
  font-size: 20px;
}

.welcome-content p {
  color: #8c8c8c;
  margin: 0;
}

.statistics-grid {
  display: grid;
  grid-template-columns: 1.4fr 0.6fr 1.4fr;
  grid-template-rows: 1fr 1fr;
  gap: 20px;
  height: calc(100vh - 280px);
  min-height: 500px;
  width: 100%;
  box-sizing: border-box;
}

.grid-item {
  min-height: 240px;
  width: 100%;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
}

.top-left {
  grid-column: 1;
  grid-row: 1;
}

.top-right {
  grid-column: 3;
  grid-row: 1;
}

.center {
  grid-column: 2;
  grid-row: 1 / 3;
  display: flex;
  align-items: center;
  justify-content: center;
}

.bottom-left {
  grid-column: 1;
  grid-row: 2;
}

.bottom-right {
  grid-column: 3;
  grid-row: 2;
}

.statistics-grid.menu-expanded {
  grid-template-columns: 1fr 1fr;
}

.statistics-grid.menu-expanded .top-left {
  grid-column: 1;
  grid-row: 1;
}

.statistics-grid.menu-expanded .top-right {
  grid-column: 2;
  grid-row: 1;
}

.statistics-grid.menu-expanded .bottom-left {
  grid-column: 1;
  grid-row: 2;
}

.statistics-grid.menu-expanded .bottom-right {
  grid-column: 2;
  grid-row: 2;
}
</style>
