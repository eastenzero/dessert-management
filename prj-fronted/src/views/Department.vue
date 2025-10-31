<template>
  <div>
    <h2>部门管理</h2>
    <el-form :model="queryParams" :inline="true" label-width="80px">
      <el-form-item label="部门名称">
        <el-input v-model="queryParams.name" placeholder="请输入部门名称" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="负责人">
        <el-input v-model="queryParams.leader" placeholder="请输入负责人" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row>
      <el-button type="danger" @click="handleDeleteBatch">删除</el-button>
      <el-button type="primary" @click="openCreate">新增</el-button>
    </el-row>

    <el-dialog :title="titleOp" v-model="visibleDialog">
      <el-form :model="department" :rules="rules" ref="saveForm" label-width="80px">
        <el-form-item>
          <el-input v-model="department.id" v-if="false" :disabled="true" />
        </el-form-item>
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="department.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="department.leader" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="department.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="department.descp" placeholder="请输入描述" />
        </el-form-item>
        <el-button type="primary" @click="save">确认</el-button>
      </el-form>
    </el-dialog>

    <el-table style="min-height:300px" v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="60" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="部门名称" align="left" prop="name" width="160" />
      <el-table-column label="负责人" align="center" prop="leader" width="120" />
      <el-table-column label="联系电话" align="center" prop="phone" width="160" />
      <el-table-column label="描述" align="left" prop="descp" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" @click="openEdit(scope.row)">修改</el-button>
          <el-button type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-row type="flex" justify="center" align="middle">
      <el-pagination
        :current-page="queryParams.pageNum"
        :page-size="queryParams.pageSize"
        :total="total"
        layout="jumper, prev, pager, next, total"
        style="text-align: center;margin-top: 20px;"
        @current-change="handleCurrentChange"
      />
    </el-row>
  </div>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref } from 'vue'
import * as DepartmentService from '../api/Department'

const titleOp = ref('新增')
const visibleDialog = ref(false)
const department = ref({})

const rules = {
  name: [{ required: true, message: '请输入部门名称', trigger: ['blur', 'submit'] }],
  phone: [
    {
      pattern: /^(|[0-9\-+()]{5,20})$/,
      message: '请输入合法联系电话',
      trigger: ['blur', 'submit']
    }
  ]
}
const saveForm = ref({})

function openCreate() {
  titleOp.value = '新增'
  department.value = {}
  visibleDialog.value = true
}

function openEdit(row) {
  titleOp.value = '编辑'
  department.value = { ...row }
  visibleDialog.value = true
}

function save() {
  saveForm.value.validate(valid => {
    if (!valid) {
      ElMessage.warning('表单数据有问题，请先修正！')
    } else {
      const action = department.value.id === undefined ? DepartmentService.add : DepartmentService.edit
      action(department)
        .then(resp => {
          if (resp.data.code === 200) {
            ElMessage.success(titleOp.value + '部门成功！')
            department.value = {}
            visibleDialog.value = false
            getList()
          } else {
            ElMessage({ message: titleOp.value + '部门失败！', type: 'error', duration: 12000 })
          }
        })
        .catch(error => {
          if (error.response) {
            ElMessage({ message: titleOp.value + '部门异常！', type: 'error', duration: 120000 })
          }
        })
    }
  })
}

const list = ref([])
const loading = ref(true)
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 5,
  name: null,
  leader: null,
  phone: null
})

function getList() {
  loading.value = true
  DepartmentService.list(queryParams)
    .then(resp => {
      list.value = resp.data.rows
      total.value = resp.data.total
    })
    .catch(() => {
      ElMessage.error('查询部门数据失败，请检查服务端日志')
    })
    .finally(() => {
      loading.value = false
    })
}

const created = () => {
  getList()
}
created()

function handleCurrentChange(val) {
  queryParams.value.pageNum = val
  getList()
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.value.name = null
  queryParams.value.leader = null
  queryParams.value.phone = null
  handleQuery()
}

const ids = ref([])
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
}

function handleDelete(row) {
  const ids2del = row.id || ids.value
  if (!ids2del || (Array.isArray(ids2del) && ids2del.length === 0)) {
    ElMessage.warning('请先选择要删除的部门')
    return
  }
  ElMessageBox.confirm('确认删除ID为' + ids2del + '的部门？', '警告', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      DepartmentService.del(ids2del)
        .then(() => {
          getList()
          ElMessage.success('删除成功')
        })
        .catch(() => {
          ElMessage.warning('删除失败')
        })
    })
}

function handleDeleteBatch() {
  handleDelete({ id: ids.value })
}
</script>

<style scoped>
</style>
