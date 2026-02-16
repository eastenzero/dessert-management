<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref, onMounted } from 'vue'
import * as EmployeeService from '../api/Employee'
import * as DepartmentService from '../api/Department'
import { Search, Refresh, Plus, Delete, Edit } from '@element-plus/icons-vue'

const titleOp = ref('新增')
const visibleDialog = ref(false)
const employee = ref({})
const saveForm = ref(null)

const rules = {
  name: [{ required: true, message: '请输入员工姓名', trigger: ['blur', 'submit'] }],
  deptId: [{ required: true, message: '请选择部门', trigger: ['change', 'submit'] }],
  phone: [
    {
      pattern: /^(|[0-9\-+()]{5,20})$/,
      message: '请输入合法联系电话',
      trigger: ['blur', 'submit']
    }
  ]
}

const deptOptions = ref([])

function loadDepartments() {
  DepartmentService.list(
    ref({
      pageNum: 1,
      pageSize: 1000,
      name: null,
      leader: null,
      phone: null
    })
  ).then(resp => {
    deptOptions.value = resp.data.rows || []
  })
}

function openCreate() {
  titleOp.value = '新增员工'
  employee.value = {}
  visibleDialog.value = true
  if(saveForm.value) saveForm.value.resetFields()
}

function openEdit(row) {
  titleOp.value = '编辑员工'
  employee.value = { ...row }
  visibleDialog.value = true
}

function save() {
  saveForm.value.validate(valid => {
    if (!valid) {
      ElMessage.warning('表单数据有问题，请先修正！')
    } else {
      const action = employee.value.id === undefined ? EmployeeService.add : EmployeeService.edit
      action(employee.value)
        .then(resp => {
          if (resp.data.code === 200) {
            ElMessage.success(titleOp.value + '成功！')
            employee.value = {}
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
  deptId: null // Changed from 0 to null for cleaner initial state if using placeholder
})

function getList() {
  loading.value = true
  // Handle 0 vs null logic if backend strictly requires 0, but usually null is ignored.
  // Original code used 0 for "all".
  // Let's stick to 0 if null causes issues, or handle it here.
  const params = { ...queryParams.value }
  if (params.deptId === null) params.deptId = 0
  
  EmployeeService.list(ref(params))
    .then(resp => {
      list.value = resp.data.rows
      total.value = resp.data.total
    })
    .catch(() => {
      ElMessage.error('查询员工数据失败')
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
  queryParams.value.deptId = null
  handleQuery()
}

const ids = ref([])

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
}

function handleDelete(row) {
  const ids2del = row.id || ids.value
  if (!ids2del || (Array.isArray(ids2del) && ids2del.length === 0)) {
    ElMessage.warning('请先选择要删除的员工')
    return
  }
  ElMessageBox.confirm('确认删除选中的员工？', '警告', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    EmployeeService.del(ids2del)
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

function getDeptNameById(id) {
  const item = deptOptions.value.find(d => d.id === id)
  return item ? item.name : 'Unknown'
}

onMounted(() => {
  loadDepartments()
  getList()
})
</script>

<template>
  <div class="app-container slide-up">
    <el-card class="filter-card" shadow="hover">
      <el-form :model="queryParams" :inline="true" class="filter-form">
        <el-form-item label="员工姓名">
          <el-input v-model="queryParams.name" placeholder="请输入员工姓名" @keyup.enter="handleQuery" clearable />
        </el-form-item>
        <el-form-item label="所属部门">
          <el-select v-model="queryParams.deptId" placeholder="全部部门" style="width: 160px" clearable>
            <!-- <el-option :key="0" :label="'全部'" :value="0" /> handled by clearable or backend handling 0 -->
            <el-option v-for="item in deptOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
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
          <span>员工列表</span>
          <div class="header-actions">
            <el-button type="primary" :icon="Plus" @click="openCreate">新增员工</el-button>
            <el-button type="danger" :icon="Delete" @click="handleDeleteBatch" :disabled="ids.length === 0">批量删除</el-button>
          </div>
        </div>
      </template>

      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" border stripe>
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="姓名" align="left" prop="name" width="120">
             <template #default="scope">
                <span style="font-weight: 500;">{{ scope.row.name }}</span>
            </template>
        </el-table-column>
        <el-table-column label="性别" align="center" prop="gender" width="80" />
        <el-table-column label="所属部门" align="center" width="140">
          <template #default="scope">
            <el-tag type="info" effect="plain">{{ getDeptNameById(scope.row.deptId) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="联系电话" align="center" prop="phone" width="160" />
        <el-table-column label="入职日期" align="center" prop="hireDate" width="120" />
        <el-table-column label="工资" align="right" prop="salary" width="120">
            <template #default="scope">
                {{ scope.row.salary ? `¥ ${scope.row.salary}` : '-' }}
            </template>
        </el-table-column>
        <el-table-column label="备注" align="left" prop="descp" min-width="150" show-overflow-tooltip/>
        <el-table-column label="操作" align="center" width="180" fixed="right">
          <template #default="scope">
            <el-button type="primary" link :icon="Edit" @click="openEdit(scope.row)">编辑</el-button>
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

    <el-dialog :title="titleOp" v-model="visibleDialog" width="500px" style="border-radius: 12px;">
      <el-form :model="employee" :rules="rules" ref="saveForm" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="employee.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="employee.gender" placeholder="请选择性别" style="width: 100%;">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-select v-model="employee.deptId" placeholder="请选择部门" style="width: 100%;">
            <el-option v-for="item in deptOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="employee.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker
            v-model="employee.hireDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择入职日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="工资">
          <el-input v-model="employee.salary" placeholder="请输入工资" type="number">
             <template #prefix>¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="employee.descp" placeholder="请输入备注" type="textarea" :rows="2"/>
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
