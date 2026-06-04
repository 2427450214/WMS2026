<template>
  <div class="chart-container">
    <h3 class="chart-title">近7天商品出库</h3>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">当前网络异常</div>
    <div v-else-if="data.length === 0" class="no-data">暂无数据</div>
    <div v-else ref="chart" class="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getWeeklyOutboundStatistic } from '../../api/statistics'

const COLORS = [
  '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de',
  '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#ff6b6b',
  '#4ecdc4', '#45b7d1', '#96ceb4', '#ffeaa7', '#dfe6e9'
]

export default {
  name: 'DailyOutboundChart',
  data() {
    return {
      data: [],
      loading: false,
      error: false,
      chart: null
    }
  },
  mounted() {
    this.fetchData()
  },
  beforeUnmount() {
    if (this.chart) {
      window.removeEventListener('resize', this.handleResize)
      this.chart.dispose()
      this.chart = null
    }
  },
  methods: {
    async fetchData() {
      this.loading = true
      this.error = false
      try {
        const response = await getWeeklyOutboundStatistic()
        console.log('DailyOutboundChart - API响应:', response)
        if (response.code === 200) {
          this.data = response.data
          console.log('DailyOutboundChart - 数据:', this.data)
          this.loading = false
          await this.$nextTick()
          this.renderChart()
        } else {
          this.data = []
          this.loading = false
        }
      } catch (err) {
        console.error('获取出库数据失败:', err)
        this.error = true
        this.loading = false
      }
    },
    renderChart() {
      if (!this.$refs.chart) {
        console.warn('DailyOutboundChart - chart ref不存在')
        return
      }

      console.log('DailyOutboundChart - 开始渲染图表')

      if (this.chart) {
        window.removeEventListener('resize', this.handleResize)
        this.chart.dispose()
        this.chart = null
      }

      this.chart = echarts.init(this.$refs.chart)

      const pieData = this.data.map((item, index) => ({
        name: item.goodsName,
        value: item.totalOutboundQuantity,
        itemStyle: { color: COLORS[index % COLORS.length] }
      }))

      const option = {
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        series: [
          {
            name: '出库数量',
            type: 'pie',
            radius: ['25%', '50%'],
            center: ['50%', '50%'],
            avoidLabelOverlap: true,
            itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
            label: { show: true, formatter: '{b}\n{c}', fontSize: 10, position: 'outside' },
            labelLine: { show: true, length: 10, length2: 15, smooth: true },
            emphasis: { label: { show: true, fontSize: 12, fontWeight: 'bold' } },
            data: pieData
          }
        ]
      }

      this.chart.setOption(option)
      window.addEventListener('resize', this.handleResize)
    },
    handleResize() {
      if (this.chart && !this.chart.isDisposed()) {
        try {
          this.chart.resize()
        } catch (e) {
          console.warn('DailyOutboundChart - resize错误:', e)
        }
      }
    }
  }
}
</script>

<style scoped>
.chart-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  background: white;
  border-radius: 12px;
  padding: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
}
.chart-title {
  margin: 0 0 5px 0;
  font-size: 13px;
  color: #333;
  font-weight: 600;
  flex-shrink: 0;
}
.chart {
  flex: 1;
  min-height: 0;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}
.chart > div {
  width: 100% !important;
  height: 100% !important;
}
.loading, .error, .no-data {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  font-size: 13px;
}
.error {
  color: #ef4444;
}
</style>
