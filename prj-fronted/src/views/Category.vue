<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from 'element-plus';
import * as CategoryService from '../api/Category'
import { Search, Refresh, Plus, Delete, Edit } from '@element-plus/icons-vue';

let titleOp = ref('新增')
const visibleDialog = ref(false)
const category = ref({})
const saveForm = ref(null)

const rules = {
    name: [{ required: true, message: '请输入分类名称', trigger: ['blur', 'submit'] },]
}

const handleAdd = () => {
    visibleDialog.value = true;
    titleOp.value = '新增分类';
    category.value = {};
    if(saveForm.value) saveForm.value.resetFields();
}

const handleEdit = (row) => {
    visibleDialog.value = true;
    titleOp.value = '编辑分类';
    category.value = { ...row };
}

function save() {
    saveForm.value.validate(valid => {
        if (!valid) {
            ElMessage.warning('请检查输入项')
        } else {
            if (category.value.id == undefined) {
                CategoryService.add(category.value).then(resp => {
                    if (resp.data.code == 200) {
                        ElMessage.success('新增分类成功');
                        category.value = {}; visibleDialog.value = false;
                        getList()
                    } else {
                        ElMessage.error('新增分类失败');
                    }
                }).catch(error => {
                    ElMessage.error('新增分类异常');
                })
            } else {
                CategoryService.edit(category.value).then(resp => {
                    if (resp.data.code == 200) {
                        ElMessage.success('编辑分类成功');
                        category.value = {}; visibleDialog.value = false;
                        getList() // Added refresh
                    } else {
                        ElMessage.error('编辑分类失败');
                    }
                }).catch(error => {
                   ElMessage.error('编辑分类异常');
                })
            }
        }
    })
}

/** 列表操作变量 */
let list = ref([])
let loading = ref(true)
let total = ref(0)
let queryParams = ref({
    pageNum: 1,
    pageSize: 5,
    name: null,
    descp: null,
})

/** 列表 */
function getList() {
    loading.value = true;
    CategoryService.list(queryParams.value).then(resp => {
        list.value = resp.data.rows
        total.value = resp.data.total
        loading.value = false;
    });
}

onMounted(() => {
    getList()
})

/** 分页控件上单击页码 */
const handleCurrentChange = (val) => {
    queryParams.value.pageNum = val
    getList()
}

// 查询
const handleQuery = () => {
    queryParams.value.pageNum = 1
    getList()
}
const resetQuery = () => {
    queryParams.value.name = null
    queryParams.value.descp = null
    handleQuery()
}

let ids = []
const handleSelectionChange = (selection) => {
    ids = selection.map(item => item.id)
}
// 删除
function handleDelete(row) {
    let ids2del = row.id || ids
    ElMessageBox.confirm('确认删除选中的分类？', '警告',
        { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' })
        .then(() => {
            CategoryService.del(ids2del)
                .then(() => {
                    getList()
                    ElMessage.success("删除成功")
                })
                .catch(() => {
                    ElMessage.warning("删除失败")
                })
        })
}
function handleDeleteBatch() {
    if (ids.length === 0) {
        ElMessage.warning("请选择要删除的项")
        return
    }
    handleDelete({ id: null })
}
</script>

<template>
    <div class="app-container slide-up">
        <el-card class="filter-card" shadow="hover">
            <el-form :model="queryParams" :inline="true" class="filter-form">
                <el-form-item label="名称">
                    <el-input v-model="queryParams.name" placeholder="请输入分类名称" @keyup.enter="handleQuery" clearable />
                </el-form-item>
                <el-form-item label="描述">
                    <el-input v-model="queryParams.descp" placeholder="请输入分类描述" @keyup.enter="handleQuery" clearable />
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
                    <span>分类列表</span>
                    <div class="header-actions">
                        <el-button type="primary" :icon="Plus" @click="handleAdd">新增分类</el-button>
                        <el-button type="danger" :icon="Delete" @click="handleDeleteBatch" :disabled="ids.length === 0">批量删除</el-button>
                    </div>
                </div>
            </template>

            <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" border stripe>
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column label="ID" align="center" prop="id" width="80" v-if="false" />
                <el-table-column label="分类名称" align="left" prop="name" width="200">
                    <template #default="scope">
                        <span style="font-weight: 500;">{{ scope.row.name }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="分类描述" align="left" prop="descp" min-width="200" show-overflow-tooltip/>
                <el-table-column label="操作" width="180" align="center" fixed="right">
                    <template #default="scope">
                        <el-button type="primary" link :icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
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
            <el-form :model="category" :rules="rules" ref="saveForm" label-width="80px">
                <el-form-item label="名称" prop="name">
                    <el-input v-model="category.name" placeholder="请输入分类名称"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="descp">
                    <el-input type="textarea" :rows="3" v-model="category.descp" placeholder="请输入分类描述"></el-input>
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