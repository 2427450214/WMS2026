<template>
  <div class="test-view">
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h2>组件测试页面</h2>
          <p class="desc">所有可视化组件集中展示，方便测试和调试</p>
        </div>
        <button class="back-btn" @click="goBack">返回管理首页</button>
      </div>
    </div>
    
    <div class="components-grid">
      <div class="component-card">
      <h3 class="card-title">近7天入库饼图</h3>
      <div class="card-content">
        <DailyInboundChart />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">近7天入库趋势</h3>
      <div class="card-content">
        <DailyInboundLineChart />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">近7天出库饼图</h3>
      <div class="card-content">
        <DailyOutboundChart />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">近7天出库趋势</h3>
      <div class="card-content">
        <DailyOutboundLineChart />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">库存前三</h3>
      <div class="card-content">
        <TopStockChart />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">过期商品预警</h3>
      <div class="card-content">
        <ExpiredWarning />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">即将过期商品</h3>
      <div class="card-content">
        <NearExpiryWarning />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">分类库存统计</h3>
      <div class="card-content">
        <CategoryStockChart />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">库存预警统计</h3>
      <div class="card-content">
        <StockWarning />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">库存周转率</h3>
      <div class="card-content">
        <StockTurnover />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">热门商品</h3>
      <div class="card-content">
        <HotGoods />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">滞销商品</h3>
      <div class="card-content">
        <SlowMovingGoods />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">每日库存趋势</h3>
      <div class="card-content">
        <DailyStockTrend />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">仓库库存统计</h3>
      <div class="card-content">
        <WarehouseStock />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">用户活跃度</h3>
      <div class="card-content">
        <UserActivity />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">操作类型统计</h3>
      <div class="card-content">
        <OperationType />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">批次剩余库存</h3>
      <div class="card-content">
        <BatchRemaining />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">批次过期预警</h3>
      <div class="card-content">
        <BatchExpiry />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">分类入库统计</h3>
      <div class="card-content">
        <CategoryInbound />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">分类出库统计</h3>
      <div class="card-content">
        <CategoryOutbound />
      </div>
      </div>

      <div class="component-card">
      <h3 class="card-title">每日出入库统计</h3>
      <div class="card-content">
        <DailyInOut />
      </div>
      </div>

    </div>
  </div>
</template>

<script>
import DailyInboundChart from './utils/DailyInboundChart.vue'
import DailyInboundLineChart from './utils/DailyInboundLineChart.vue'
import DailyOutboundChart from './utils/DailyOutboundChart.vue'
import DailyOutboundLineChart from './utils/DailyOutboundLineChart.vue'
import TopStockChart from './utils/TopStockChart.vue'
import ExpiredWarning from './utils/ExpiredWarning.vue'
import NearExpiryWarning from './utils/NearExpiryWarning.vue'
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
import { useRouter } from 'vue-router'

export default {
  name: 'TestAllComponents',
  components: {
    DailyInboundChart,
    DailyInboundLineChart,
    DailyOutboundChart,
    DailyOutboundLineChart,
    TopStockChart,
    ExpiredWarning,
    NearExpiryWarning,
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
    DailyInOut
  },
  setup() {
    const router = useRouter()

    const goBack = () => {
      router.push('/admin')
    }

    return {
      goBack
    }
  }
}
</script>

<style scoped>
.test-view {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100%;
}

.page-header {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-text h2 {
  margin: 0 0 8px 0;
  color: #262626;
  font-size: 20px;
}

.header-text .desc {
  margin: 0;
  color: #8c8c8c;
  font-size: 14px;
}

.back-btn {
  padding: 10px 20px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.back-btn:hover {
  background-color: #40a9ff;
}

.components-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(450px, 1fr));
  gap: 20px;
}

.component-card {
  background-color: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.card-title {
  margin: 0 0 12px 0;
  font-size: 15px;
  color: #262626;
  font-weight: 600;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.card-content {
  height: 300px;
}
</style>
