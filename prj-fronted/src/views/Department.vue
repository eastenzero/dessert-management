<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref, onMounted } from 'vue'
import * as DepartmentService from '../api/Department'
import { Search, Refresh, Plus, Delete, Edit } from '@element-plus/icons-vue'

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
const saveForm = ref(null)

function openCreate() {
  titleOp.value = '新增部门'
  department.value = {}
  visibleDialog.value = true
  if(saveForm.value) saveForm.value.resetFields()
}

function openEdit(row) {
  titleOp.value = '编辑部门'
  department.value = { ...row }
  visibleDialog.value = true
}

function save() {
  saveForm.value.validate(valid => {
    if (!valid) {
      ElMessage.warning('表单数据有问题，请先修正！')
    } else {
      const action = department.value.id === undefined ? DepartmentService.add : DepartmentService.edit
      action(department.value)
        .then(resp => {
          if (resp.data.code === 200) {
            ElMessage.success(titleOp.value + '成功！')
            department.value = {}
            visibleDialog.value = false
            getList()
          } else {
            ElMessage.error(titleOp.value + '失败！')
          }
        })
        .catch(error => {
           ElMessage.error(titleOp.value + '异常！')
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
  DepartmentService.list(ref(queryParams.value))
    .then(resp => {
      list.value = resp.data.rows
      total.value = resp.data.total
    })
    .catch(() => {
      ElMessage.error('查询部门数据失败')
    })
    .finally(() => {
      loading.value = false
    })
}

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
  ElMessageBox.confirm('确认删除选中的部门？', '警告', {
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

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container slide-up">
    <el-card class="filter-card" shadow="hover">
      <el-form :model="queryParams" :inline="true" class="filter-form">
        <el-form-item label="部门名称">
          <el-input v-model="queryParams.name" placeholder="请输入部门名称" @keyup.enter="handleQuery" clearable />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="queryParams.leader" placeholder="请输入负责人" @keyup.enter="handleQuery" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>部门列表</span>
          <div class="header-actions">
            <el-button type="primary" :icon="Plus" @click="openCreate">新增部门</el-button>
            <el-button type="danger" :icon="Delete" @click="handleDeleteBatch" :disabled="ids.length === 0">批量删除</el-button>
          </div>
        </div>
      </template>

      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" border stripe>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="部门名称" align="left" prop="name" width="160">
             <template #default="scope">
                <span style="font-weight: 500;">{{ scope.row.name }}</span>
            </template>
        </el-table-column>
        <el-table-column label="负责人" align="center" prop="leader" width="120" />
        <el-table-column label="联系电话" align="center" prop="phone" width="160" />
        <el-table-column label="描述" align="left" prop="descp" min-width="150" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" width="180" fixed="right">
          <template #default="scope">
            <el-button type="primary" link :icon="Edit" @click="openEdit(scope.row)">修改</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

       <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-size="queryParams.pageSize"
          :total="total"
          layout="total, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
          background 
        />
      </div>
    </el-card>

    <el-dialog :title="titleOp" v-model="visibleDialog" width="450px" style="border-radius: 12px;">
      <el-form :model="department" :rules="rules" ref="saveForm" label-width="80px">
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
          <el-input type="textarea" :rows="3" v-model="department.descp" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
           <el-button @click="visibleDialog = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.app-container {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
}

.filter-card {
    margin-bottom: 20px;
}

.filter-form .el-form-item {
    margin-right: 16px;
    margin-bottom: 0;
}

.table-card {
    min-height: 400px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: center;
}
</style>
