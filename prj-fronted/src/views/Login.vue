<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as AuthService from '../api/Auth'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)
const loginForm = ref({
  username: '',
  password: ''
})

const handleLogin = () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  
  loading.value = true
  AuthService.login({
    username: loginForm.value.username,
    password: loginForm.value.password
  })
    .then(resp => {
      if (resp.data.code === 200 && resp.data.data && resp.data.data.token) {
        localStorage.setItem('token', resp.data.data.token)
        localStorage.setItem('currentUser', loginForm.value.username) // Store user for display
        ElMessage.success('登录成功')
        router.push('/')
      } else {
        ElMessage.error(resp.data.msg || '登录失败')
      }
    })
    .catch(() => {
      ElMessage.error('登录异常，请检查服务端')
    })
    .finally(() => {
      loading.value = false
    })
}
</script>

<template>
  <div class="login-container">
    <div class="login-left">
      <div class="brand-welcome">
        <h1>Sweet Admin</h1>
        <p>让每一份甜蜜都井井有条</p>
      </div>
    </div>
    <div class="login-right">
      <div class="login-card">
        <h2>欢迎回来</h2>
        <p class="subtitle">请登录您的账户</p>
        
        <el-form :model="loginForm" class="login-form">
          <el-form-item>
            <el-input 
              v-model="loginForm.username" 
              placeholder="用户名" 
              :prefix-icon="User"
              size="large"
            />
          </el-form-item>
          <el-form-item>
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="密码" 
              :prefix-icon="Lock" 
              show-password 
              size="large"
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-button 
            type="primary" 
            class="login-btn" 
            :loading="loading" 
            @click="handleLogin"
            size="large"
          >
            立即登录
          </el-button>
          
          <div class="form-footer">
            <router-link to="/register">还没有账号？立即注册</router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.login-left {
  flex: 1;
  background-image: url('/img/dessert_login_background.png');
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Overlay for better text readability if needed, or just style */
.login-left::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0, 0, 0, 0.3);
}

.brand-welcome {
  position: relative;
  z-index: 1;
  text-align: center;
  color: white;
}

.brand-welcome h1 {
  font-size: 3rem;
  margin-bottom: 0.5rem;
  color: white;
  text-shadow: 0 2px 10px rgba(0,0,0,0.3);
}

.brand-welcome p {
  font-size: 1.5rem;
  opacity: 0.9;
  text-shadow: 0 1px 5px rgba(0,0,0,0.3);
}

.login-right {
  flex: 0 0 500px; /* Fixed width for form area */
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-card {
  width: 100%;
  max-width: 360px;
}

.login-card h2 {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  color: var(--color-brand-dark);
}

.subtitle {
  color: var(--color-text-secondary);
  margin-bottom: 2rem;
}

.login-btn {
  width: 100%;
  margin-top: 1rem;
  font-weight: 600;
}

.form-footer {
  margin-top: 1.5rem;
  text-align: center;
  font-size: 0.9rem;
}

@media (max-width: 900px) {
  .login-left {
    display: none;
  }
  .login-right {
    flex: 1;
  }
}
</style>
