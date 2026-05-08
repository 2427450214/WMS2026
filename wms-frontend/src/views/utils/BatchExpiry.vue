<template>
  <div class="chart-container">
    <h3 class="chart-title">批次过期预警</h3>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">当前网络异常</div>
    <div v-else-if="data.length === 0" class="no-data">暂无数据</div>
    <div v-else ref="chart" class="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getBatchExpiryStatistics } from '../../api/statistics'

const COLORS = [
  '#ef4444', '#f59e0b', '#10b981', '#3b82f6', '#8b5cf6',
  '#ec4899', '#f97316', '#14b8a6', '#6366f1', '#a855f7'
]

export default {
  name: 'BatchExpiry',
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
        const response = await getBatchExpiryStatistics()
        console.log('BatchExpiry - API响应:', response)
        if (response.code === 200) {
          this.data = response.data
          console.log('BatchExpiry - 数据:', this.data)
          this.loading = false
          await this.$nextTick()
          this.renderChart()
        } else {
          this.error = true
          this.loading = false
        }
      } catch (err) {
        console.error('获取批次过期预警数据失败:', err)
        this.error = true
        this.loading = false
      }
    },
    renderChart() {
      if (!this.$refs.chart) {
        console.warn('BatchExpiry - chart ref不存在')
        return
      }

      if (this.chart) {
        window.removeEventListener('resize', this.handleResize)
        this.chart.dispose()
        this.chart = null
      }

      this.chart = echarts.init(this.$refs.chart)

      const periods = this.data.map(item => item.period)
      const counts = this.data.map(item => item.count)

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
          data: periods,
          axisLabel: {
            fontSize: 10,
            interval: 0
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
            name: '批次数量',
            type: 'bar',
            data: counts.map((value, index) => ({
              value: value,
              itemStyle: {
                color: COLORS[index % COLORS.length]
              }
            })),
            label: {
              show: true,
              position: 'top',
              fontSize: 10
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
          console.warn('BatchExpiry - resize错误:', e)
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
