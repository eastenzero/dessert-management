<script setup>
import { ElMessage } from 'element-plus'

document.title = "甜点信息管理系统";

const handleLogin = () => {
  ElMessage.info('请根据业务需求接入登录功能')
}

const handleLogout = () => {
  ElMessage.info('请根据业务需求接入退出功能')
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
          欢迎^^<el-button type="link"  
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
          </el-aside>
          <el-main><router-view /></el-main>
        </el-container>
      </div>
      <el-footer> Copyright © 2024 </el-footer>
    </el-container>
  </div>
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
