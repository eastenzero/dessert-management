<script setup>
import { ref } from 'vue';
import { Dish, User, Money, Timer } from '@element-plus/icons-vue';

// Mock data for dashboard
const stats = ref([
  { title: '甜点总数', value: 120, icon: Dish, color: '#E56B6F' },
  { title: '今日订单', value: 45, icon: Money, color: '#67C23A' },
  { title: '活跃员工', value: 12, icon: User, color: '#409EFF' },
  { title: '平均耗时', value: '24m', icon: Timer, color: '#E6A23C' },
]);

const activities = ref([
  { content: '新甜点 "草莓千层" 上架', timestamp: '2024-02-16 10:00' },
  { content: '员工 张三 完成了入职培训', timestamp: '2024-02-15 14:30' },
  { content: '系统进行了一次维护', timestamp: '2024-02-14 02:00' },
]);
</script>

<template>
  <div class="dashboard fade-in">
    <div class="welcome-section">
      <h2>仪表盘</h2>
      <p>欢迎回到管理系统，这里是您的今日概览。</p>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="(stat, index) in stats" :key="index">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: stat.color + '20', color: stat.color }">
              <el-icon><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-title">{{ stat.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="content-row">
      <el-col :xs="24" :md="16">
        <el-card class="chart-card" header="近期动态">
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in activities"
              :key="index"
              :timestamp="activity.timestamp"
              :color="index === 0 ? '#E56B6F' : ''"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8">
        <el-card class="quick-actions" header="快捷入口">
          <div class="action-buttons">
            <el-button type="primary" plain @click="$router.push('/Dessert')">新增甜点</el-button>
            <el-button type="success" plain @click="$router.push('/Employee')">添加员工</el-button>
            <el-button type="warning" plain @click="$router.push('/Department')">部门设置</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.welcome-section {
  margin-bottom: 24px;
}

.welcome-section h2 {
  font-size: 24px;
  color: var(--color-brand-dark);
  margin-bottom: 8px;
}

.welcome-section p {
  color: var(--color-text-secondary);
}

.stats-row {
  margin-bottom: 24px;
}

.stat-card {
  border: none;
  background: white;
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 16px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-text-primary);
  line-height: 1.2;
}

.stat-title {
  font-size: 14px;
  color: var(--color-text-secondary);
}

.content-row {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.action-buttons .el-button {
  margin-left: 0;
  width: 100%;
  justify-content: flex-start;
}
</style>