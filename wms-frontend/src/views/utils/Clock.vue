<template>
  <div class="clock-container">
    <div class="time">{{ currentTime }}</div>
    <div class="date">{{ currentDate }}</div>
  </div>
</template>

<script>
export default {
  name: 'Clock',
  data() {
    return {
      currentTime: '',
      currentDate: '',
      timer: null
    }
  },
  mounted() {
    this.updateClock()
    this.timer = setInterval(() => {
      this.updateClock()
    }, 1000)
  },
  beforeUnmount() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    updateClock() {
      const now = new Date()
      this.currentTime = now.toTimeString().slice(0, 8)
      this.currentDate = now.toISOString().split('T')[0]
    }
  }
}
</script>

<style scoped>
.clock-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  color: white;
  width: 100%;
  height: 100%;
}

.time {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 5px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
}

.date {
  font-size: 12px;
  opacity: 0.9;
}
</style>
