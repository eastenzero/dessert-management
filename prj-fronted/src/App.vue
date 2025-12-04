<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue'
import * as AuthService from './api/Auth'

document.title = "甜点信息管理系统";

const loginDialogVisible = ref(false)
const loginForm = ref({
  username: '',
  password: ''
})
const isLoggedIn = ref(!!localStorage.getItem('token'))
const currentUser = ref('')

const handleLogin = () => {
  loginDialogVisible.value = true
}

const submitLogin = () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  AuthService.login({
    username: loginForm.value.username,
    password: loginForm.value.password
  })
    .then(resp => {
      if (resp.data.code === 200 && resp.data.data && resp.data.data.token) {
        const token = resp.data.data.token
        localStorage.setItem('token', token)
        isLoggedIn.value = true
        currentUser.value = loginForm.value.username
        ElMessage.success('登录成功')
        loginDialogVisible.value = false
      } else {
        ElMessage.error(resp.data.msg || '登录失败')
      }
    })
    .catch(() => {
      ElMessage.error('登录异常，请检查服务端')
    })
}

const handleLogout = () => {
  AuthService.logout()
    .finally(() => {
      localStorage.removeItem('token')
      isLoggedIn.value = false
      currentUser.value = ''
      ElMessage.info('已退出登录')
    })
}
</script>
<!-- 页面布局 -->
<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        甜点信息管理系统
        <div id="loginOut">
          <el-button type="link"  
           @click="handleLogin"><img src="../public/img/login.png">登录</el-button> 
          欢迎^^{{ currentUser }}<el-button type="link"  
         @click="handleLogout"><img src="../public/img/logout.png">退出</el-button> 
        </div>
      </el-header>
      <div class="common-layout">
        <el-container>
          <el-aside width="200px">
            <router-link to="/Register" class="nav-link module">用户注册</router-link> 
            <router-link to="/Category" class="nav-link module">分类管理</router-link> 
            <router-link to="/Dessert" class="nav-link module">甜点管理</router-link>  
            <router-link to="/Department" class="nav-link module">部门管理</router-link>  
            <router-link to="/Employee" class="nav-link module">员工管理</router-link>  
          </el-aside>
          <el-main><router-view /></el-main>
        </el-container>
      </div>
      <el-footer> Copyright 2024 </el-footer>
    </el-container>
  </div>
  <el-dialog v-model="loginDialogVisible" title="用户登录" width="400px">
    <el-form :model="loginForm" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="loginForm.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="loginForm.password" placeholder="请输入密码" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitLogin">登录</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<style scoped>
body{margin:0; font-family: 微软雅黑;} 
header,footer{ width: 1000px; height: 40px; background: slateblue;text-align: center; line-height:40px;
    color: azure; position: relative;}
#loginOut{position: absolute;right:3px;top:0px;}
#loginOut .el-button--text{background-color: transparent;color:white;font-size:15px;} 
#loginOut img{ width: 15px; } 
aside{width: 100px; height: 640px; background: #a6a6a6;padding-top: 20px;} 
.module{ text-decoration: none; background: black; color:white;
    display: block;  margin: 8px auto;  width: 80%;
    height: 35px; line-height: 35px; text-align: center; border-radius: 5px;}
.module:hover{ box-shadow: 3px 3px 3px} 
</style>
