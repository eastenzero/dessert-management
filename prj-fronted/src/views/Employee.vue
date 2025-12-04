<template>
  <div class="employee-page">
    <h2>员工管理</h2>
    <el-form :model="queryParams" :inline="true" label-width="80px">
      <el-form-item label="员工姓名">
        <el-input v-model="queryParams.name" placeholder="请输入员工姓名" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所属部门">
        <el-select v-model="queryParams.deptId" placeholder="请选择部门" style="width: 160px">
          <el-option :key="0" :label="'全部'" :value="0" />
          <el-option v-for="item in deptOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
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

    <el-dialog :title="titleOp" v-model="visibleDialog" width="520px">
      <el-form :model="employee" :rules="rules" ref="saveForm" label-width="80px">
        <el-form-item>
          <el-input v-model="employee.id" v-if="false" :disabled="true" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="employee.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="employee.gender" placeholder="请选择性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-select v-model="employee.deptId" placeholder="请选择部门">
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
          <el-input v-model="employee.salary" placeholder="请输入工资" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="employee.descp" placeholder="请输入备注" />
        </el-form-item>
        <el-button type="primary" @click="save">确认</el-button>
      </el-form>
    </el-dialog>

    <el-table style="min-height:300px" v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="60" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="姓名" align="left" prop="name" width="120" />
      <el-table-column label="性别" align="center" prop="gender" width="80" />
      <el-table-column label="所属部门" align="center" width="140">
        <template #default="scope">
          {{ getDeptNameById(scope.row.deptId) }}
        </template>
      </el-table-column>
      <el-table-column label="联系电话" align="center" prop="phone" width="160" />
      <el-table-column label="入职日期" align="center" prop="hireDate" width="120" />
      <el-table-column label="工资" align="center" prop="salary" width="100" />
      <el-table-column label="备注" align="left" prop="descp" />
      <el-table-column label="操作" align="center" width="200">
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
import * as EmployeeService from '../api/Employee'
import * as DepartmentService from '../api/Department'

const titleOp = ref('新增')
const visibleDialog = ref(false)
const employee = ref({})
const saveForm = ref({})

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
  titleOp.value = '新增'
  employee.value = {}
  visibleDialog.value = true
}

function openEdit(row) {
  titleOp.value = '编辑'
  employee.value = { ...row }
  visibleDialog.value = true
}

function save() {
  saveForm.value.validate(valid => {
    if (!valid) {
      ElMessage.warning('表单数据有问题，请先修正！')
    } else {
      const action = employee.value.id === undefined ? EmployeeService.add : EmployeeService.edit
      action(employee)
        .then(resp => {
          if (resp.data.code === 200) {
            ElMessage.success(titleOp.value + '员工成功！')
            employee.value = {}
            visibleDialog.value = false
            getList()
          } else {
            ElMessage({ message: titleOp.value + '员工失败！', type: 'error', duration: 12000 })
          }
        })
        .catch(error => {
          if (error.response) {
            ElMessage({ message: titleOp.value + '员工异常！', type: 'error', duration: 120000 })
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
  deptId: 0
})

function getList() {
  loading.value = true
  EmployeeService.list(queryParams)
    .then(resp => {
      list.value = resp.data.rows
      total.value = resp.data.total
    })
    .catch(() => {
      ElMessage.error('查询员工数据失败，请检查服务端日志')
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
  queryParams.value.deptId = 0
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
  ElMessageBox.confirm('确认删除ID为' + ids2del + '的员工？', '警告', {
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
  return item ? item.name : ''
}

const created = () => {
  loadDepartments()
  getList()
}
created()
</script>

<style scoped>
.employee-page {
  max-width: 900px;
  margin: 0 auto;
}
</style>
