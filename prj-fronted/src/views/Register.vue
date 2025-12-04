<template>
  <div class="register-page">
    <h2>用户注册</h2>
    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" class="register-form">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" placeholder="请输入密码" show-password />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" placeholder="请再次输入密码" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit">注册</el-button>
        <el-button @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import * as AuthService from '../api/Auth'

const formRef = ref()
const form = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: ['blur', 'submit'] }],
  password: [{ required: true, message: '请输入密码', trigger: ['blur', 'submit'] }],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: ['blur', 'submit'] },
    {
      validator: (_rule, value, callback) => {
        if (value !== form.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: ['blur', 'submit']
    }
  ]
}

function submit() {
  formRef.value.validate(valid => {
    if (!valid) {
      ElMessage.warning('注册信息有误，请先修正')
      return
    }
    const payload = {
      username: form.value.username,
      password: form.value.password
    }
    AuthService.register(payload)
      .then(resp => {
        if (resp.data.code === 200) {
          ElMessage.success('注册成功，密码已在服务器端加密保存')
          reset()
        } else {
          ElMessage.error(resp.data.msg || '注册失败')
        }
      })
      .catch(() => {
        ElMessage.error('注册异常，请检查服务端')
      })
  })
}

function reset() {
  form.value.username = ''
  form.value.password = ''
  form.value.confirmPassword = ''
}
</script>

<style scoped>
.register-page {
  max-width: 400px;
  margin: 40px auto;
}

.register-form {
  margin-top: 20px;
}
</style>
