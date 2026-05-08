<template>
  <div class="other-data-view">
    <div class="page-header">
      <h2>其它数据统计</h2>
    </div>
    
    <div class="statistics-grid">
      <div class="grid-item">
        <DailyInboundChart />
      </div>
      <div class="grid-item">
        <DailyInboundLineChart />
      </div>
      <div class="grid-item">
        <DailyOutboundChart />
      </div>
      <div class="grid-item">
        <DailyOutboundLineChart />
      </div>
      <div class="grid-item">
        <TopStockChart />
      </div>
      <div class="grid-item">
        <DailyStockTrend />
      </div>
      <div class="grid-item">
        <WarehouseStock />
      </div>
      <div class="grid-item">
        <UserActivity />
      </div>
      <div class="grid-item">
        <OperationType />
      </div>
      <div class="grid-item">
        <BatchRemaining />
      </div>
      <div class="grid-item">
        <BatchExpiry />
      </div>
      <div class="grid-item">
        <CategoryInbound />
      </div>
      <div class="grid-item">
        <CategoryOutbound />
      </div>
      <div class="grid-item">
        <DailyInOut />
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
import DailyStockTrend from './utils/DailyStockTrend.vue'
import WarehouseStock from './utils/WarehouseStock.vue'
import UserActivity from './utils/UserActivity.vue'
import OperationType from './utils/OperationType.vue'
import BatchRemaining from './utils/BatchRemaining.vue'
import BatchExpiry from './utils/BatchExpiry.vue'
import CategoryInbound from './utils/CategoryInbound.vue'
import CategoryOutbound from './utils/CategoryOutbound.vue'
import DailyInOut from './utils/DailyInOut.vue'

export default {
  name: 'OtherDataView',
  components: {
    DailyInboundChart,
    DailyInboundLineChart,
    DailyOutboundChart,
    DailyOutboundLineChart,
    TopStockChart,
    DailyStockTrend,
    WarehouseStock,
    UserActivity,
    OperationType,
    BatchRemaining,
    BatchExpiry,
    CategoryInbound,
    CategoryOutbound,
    DailyInOut
  }
}
</script>

<style scoped>
.other-data-view {
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

.page-header h2 {
  margin: 0;
  color: #262626;
  font-size: 20px;
}

.statistics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.grid-item {
  min-height: 320px;
}
</style>
