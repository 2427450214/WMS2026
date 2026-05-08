<template>
  <div class="goods-data-view">
    <div class="page-header">
      <h2>商品数据统计</h2>
    </div>
    
    <div class="statistics-grid">
      <div class="grid-item">
        <CategoryStockChart />
      </div>
      <div class="grid-item">
        <StockWarning />
      </div>
      <div class="grid-item">
        <StockTurnover />
      </div>
      <div class="grid-item">
        <HotGoods />
      </div>
      <div class="grid-item">
        <SlowMovingGoods />
      </div>
      <div class="grid-item">
        <ExpiredWarning />
      </div>
      <div class="grid-item">
        <NearExpiryWarning />
      </div>
    </div>
  </div>
</template>

<script>
import CategoryStockChart from './utils/CategoryStockChart.vue'
import StockWarning from './utils/StockWarning.vue'
import StockTurnover from './utils/StockTurnover.vue'
import HotGoods from './utils/HotGoods.vue'
import SlowMovingGoods from './utils/SlowMovingGoods.vue'
import ExpiredWarning from './utils/ExpiredWarning.vue'
import NearExpiryWarning from './utils/NearExpiryWarning.vue'

export default {
  name: 'GoodsDataView',
  components: {
    CategoryStockChart,
    StockWarning,
    StockTurnover,
    HotGoods,
    SlowMovingGoods,
    ExpiredWarning,
    NearExpiryWarning
  }
}
</script>

<style scoped>
.goods-data-view {
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
