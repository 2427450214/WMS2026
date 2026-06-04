<template>
  <div class="chart-container">
    <h3 class="chart-title">库存周转率</h3>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">当前网络异常</div>
    <div v-else-if="data.length === 0" class="no-data">暂无数据</div>
    <div v-else ref="chart" class="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getStockTurnoverStatistics } from '../../api/statistics'

const COLORS = [
  '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de',
  '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#ff6b6b',
  '#4ecdc4', '#45b7d1', '#96ceb4', '#ffeaa7', '#dfe6e9'
]

export default {
  name: 'StockTurnover',
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
        const response = await getStockTurnoverStatistics(30)
        console.log('StockTurnover - API响应:', response)
        if (response.code === 200) {
          this.data = response.data
          console.log('StockTurnover - 数据:', this.data)
          this.loading = false
          await this.$nextTick()
          this.renderChart()
        } else {
          this.data = []
          this.loading = false
        }
      } catch (err) {
        console.error('获取库存周转率数据失败:', err)
        this.error = true
        this.loading = false
      }
    },
    renderChart() {
      if (!this.$refs.chart) {
        console.warn('StockTurnover - chart ref不存在')
        return
      }

      if (this.chart) {
        window.removeEventListener('resize', this.handleResize)
        this.chart.dispose()
        this.chart = null
      }

      this.chart = echarts.init(this.$refs.chart)

      const goodsNames = this.data.map(item => item.goodsName)
      const turnoverRates = this.data.map(item => Number(item.turnoverRate.toFixed(2)))

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '15%',
          right: '10%',
          bottom: '15%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: goodsNames,
          axisLabel: {
            fontSize: 10,
            interval: 0,
            rotate: 30
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            fontSize: 10
          }
        },
        series: [
          {
            name: '周转率',
            type: 'bar',
            data: turnoverRates.map((value, index) => ({
              value: value,
              itemStyle: {
                color: COLORS[index % COLORS.length]
              }
            })),
            label: {
              show: true,
              position: 'top',
              fontSize: 10,
              formatter: '{c}%'
            },
            barWidth: '50%'
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
          console.warn('StockTurnover - resize错误:', e)
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
