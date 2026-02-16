<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Menu as IconMenu,
  Dish,
  User,
  Setting,
  SwitchButton,
  Fold,
  Expand
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const isCollapse = ref(false)
const currentUser = localStorage.getItem('currentUser') || 'Admin'

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('currentUser')
  ElMessage.success('已退出登录')
  router.push('/login')
}

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<template>
  <el-container class="admin-layout">
    <el-aside :width="isCollapse ? '64px' : '240px'" class="sidebar">
      <div class="logo-area">
        <img src="/img/dessert_login_background.png" alt="Logo" class="logo-img" v-if="!isCollapse"> 
        <!-- Using the generated image as a placeholder logo for now, or just text -->
        <span v-if="!isCollapse" class="logo-text">SweetAdmin</span>
        <span v-else class="logo-text-mini">S</span>
      </div>
      
      <el-menu
        :default-active="route.path"
        class="el-menu-vertical"
        :collapse="isCollapse"
        background-color="#FEFAE0"
        text-color="#6D4C41"
        active-text-color="#E56B6F"
        router
      >
        <el-menu-item index="/">
          <el-icon><IconMenu /></el-icon>
          <template #title>仪表盘</template>
        </el-menu-item>
        
        <el-menu-item index="/Dessert">
          <el-icon><Dish /></el-icon>
          <template #title>甜点管理</template>
        </el-menu-item>
        
        <el-menu-item index="/Category">
          <el-icon><IconMenu /></el-icon>
          <template #title>分类管理</template>
        </el-menu-item>
        
        <el-menu-item index="/Employee">
          <el-icon><User /></el-icon>
          <template #title>员工管理</template>
        </el-menu-item>
        
        <el-menu-item index="/Department">
          <el-icon><Setting /></el-icon>
          <template #title>部门管理</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container class="main-content-wrapper">
      <el-header class="header">
        <div class="header-left">
          <el-button type="text" @click="toggleSidebar" class="toggle-btn">
            <el-icon :size="20">
              <Expand v-if="isCollapse" />
              <Fold v-else />
            </el-icon>
          </el-button>
        </div>
        
        <div class="header-right">
          <div class="user-info">
            <el-avatar :size="32" style="background: var(--color-brand-primary)">{{ currentUser.charAt(0).toUpperCase() }}</el-avatar>
            <span class="username">{{ currentUser }}</span>
          </div>
          <el-button type="info" link @click="handleLogout">
            <el-icon><SwitchButton /></el-icon> 退出
          </el-button>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
      
      <el-footer height="40px" class="footer">
        Copyright © 2024 Sweet Admin. All Rights Reserved.
      </el-footer>
    </el-container>
  </el-container>
</template>

<style scoped>
.admin-layout {
  height: 100vh;
}

.sidebar {
  background-color: var(--color-bg-body);
  border-right: 1px solid var(--color-border);
  transition: width var(--transition-normal);
  display: flex;
  flex-direction: column;
}

.logo-area {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

.logo-img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
  object-fit: cover;
}

.logo-text {
  font-weight: 700;
  font-size: 18px;
  color: var(--color-brand-dark);
}

.logo-text-mini {
  font-weight: 700;
  font-size: 20px;
  color: var(--color-brand-primary);
}

.el-menu-vertical {
  border-right: none;
  flex: 1;
}

.header {
  height: 60px;
  background: white;
  border-bottom: 1px solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.toggle-btn {
  color: var(--color-text-primary);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.username {
  font-weight: 500;
}

.main-content {
  background-color: #f5f7fa; /* Slightly darker than body for contrast with cards */
  padding: 20px;
}

.footer {
  text-align: center;
  color: var(--color-text-secondary);
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Transitions */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
