<script setup>
import * as DessertService from "../api/Dessert";
import * as CategoryService from "../api/Category";
import { reactive, ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, Refresh, Plus, Delete, Edit, Picture } from '@element-plus/icons-vue';

//列表操作变量
let baseURL = process.env.BASE_API  //在图片显示时URL=后端baseURL + row.photoURL
let list = ref([]) //列表对象
let loading = ref(true) //列表上的遮罩层开关
let total = ref(0)  // 总行数
let queryParams = ref({ //查询参数
    pageNum: 1,
    pageSize: 5,
    name: null,
    descp: null,
    catId: null,  //分类id
    price1: null,
    price2: null,
})
//初始化分类列表
let listCategory = ref([])  //分类列表

/** 查询列表 */
function getList() {
    loading.value = true;
    DessertService.list(queryParams.value).then(resp => {
        list.value = resp.data.rows
        total.value = resp.data.total
        loading.value = false;
    });
}

/** 分类初始化 */
const initCategories = () => {
    CategoryService.list(ref({ pageNum: 1, pageSize: 10000, })).then(resp => {
        listCategory.value = resp.data.rows
    });
}

onMounted(() => {
    getList()
    initCategories()
})

/** 分页控件上单击页码 */
const handleCurrentChange = (val) => {
    queryParams.value.pageNum = val
    getList()
}

/** 搜索操作 */
const handleQuery = () => {
    queryParams.value.pageNum = 1;
    getList()
}

/** 重置操作 */
const resetQuery = () => {
    queryParams.value.name = null
    queryParams.value.descp = null
    queryParams.value.catId = null
    queryParams.value.price1 = null
    queryParams.value.price2 = null
    handleQuery();
}

/** 格式化日期：YY-MM-DD */
const dateFormat = (row, col) => {
    let date = row[col.property]
    return date == undefined ? '' : date.substring(0, 10); // Changed to full YYYY-MM-DD for better look
}

//   新增/编辑
let titleOp = ref('新增')
const visibleDialog = ref(false)
const dessert = ref({})
const saveForm = ref(null)
let imageUrl = ref('')

const saveRules = {
    name: [{ required: true, message: '请输入甜点名称', trigger: ['submit'] }],
    catId: [{ required: true, message: '请选择分类', trigger: ['change'] }], // trigger changed to change for select
    price: [{ required: true, message: '请输入价格', trigger: ['submit'] },
    { "pattern": /^[0-9]*(\.[0-9]{1,2})?$/, "message": "请输入正确的价格", trigger: ['submit'] }], // Improved regex
    releaseDate: [{ required: true, message: '请选择发布日', trigger: ['change'] }], // trigger changed
}

const beforeAvatarUpload = (file) => {
    const isLt2M = file.size / 1024 / 1024 < 2
    if (!isLt2M) {
        ElMessage.error('上传图片大小不能超过 2M!');
    }
    return isLt2M;
}

const handleAvatarSuccess = (res, file) => {
    imageUrl.value = baseURL + res
    dessert.value.photoUrl = res
}

const save = () => {
    saveForm.value.validate((valid) => {
        if (!valid) {
            ElMessage.warning('请检查输入项')
            return false
        } else {
            if (dessert.value.id == undefined) { //新增
                DessertService.add(dessert.value).then(resp => {
                    if (resp.data.code == 200) {
                        ElMessage.success('新增甜点成功');
                        getList()
                        dessert.value = {}; imageUrl.value = null; visibleDialog.value = false;
                    } else {
                        ElMessage.error('新增甜点失败');
                    }
                }).catch(error => {
                    ElMessage.error('新增甜点异常');
                })
            } else {
                // 编辑逻辑
                DessertService.edit(dessert.value).then(resp => {
                    if (resp.data.code == 200) {
                        ElMessage.success('编辑甜点成功');
                        dessert.value = {}; imageUrl.value = null; visibleDialog.value = false;
                        getList() // Should refresh list after edit
                    } else {
                        ElMessage.error('编辑甜点失败');
                    }
                }).catch(error => {
                    ElMessage.error('编辑甜点异常');
                })
            }
        }
    })
}

// 删除
function handleDelete(row) {
    let ids2del = row.id || ids
    ElMessageBox.confirm('确认删除选中的甜点？', '警告',
        { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' })
        .then(() => {
            DessertService.del(ids2del)
                .then(() => {
                    getList()
                    ElMessage.success("删除成功")
                })
                .catch(() => {
                    ElMessage.warning("删除失败")
                })
        })
}

let ids = [] // Changed to array
const handleSelectionChange = (selection) => {
    ids = selection.map(row => row.id)
}
function handleDeleteBatch() {
    if (ids.length === 0) {
        ElMessage.warning("请选择要删除的项")
        return
    }
    handleDelete({ id: null }) // Pass null id to trigger batch delete with 'ids' variable
}

const handleAdd = () => {
    visibleDialog.value = true;
    titleOp.value = '新增甜点';
    dessert.value = {};
    imageUrl.value = '';
    // reset form validation if needed
    if(saveForm.value) saveForm.value.resetFields();
}

const handleEdit = (row) => {
    visibleDialog.value = true;
    titleOp.value = '编辑甜点';
    dessert.value = { ...row }; // Clone row
    imageUrl.value = baseURL + dessert.value.photoUrl;
}

</script>

<template>
    <div class="app-container slide-up">
        <el-card class="filter-card" shadow="hover">
            <el-form :model="queryParams" :inline="true" class="filter-form">
                <el-form-item label="分类">
                    <el-select v-model="queryParams.catId" placeholder="全部分类" style="width: 140px" clearable>
                        <el-option v-for="item in listCategory" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="名称">
                    <el-input v-model="queryParams.name" placeholder="甜点名称" style="width: 160px"
                        @keyup.enter="handleQuery" clearable />
                </el-form-item>
                <el-form-item label="价格区间">
                    <el-input v-model="queryParams.price1" placeholder="Min" style="width: 80px" type="number" />
                    <span class="range-separator">-</span>
                    <el-input v-model="queryParams.price2" placeholder="Max" style="width: 80px" type="number" />
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
                    <span>甜点列表</span>
                    <div class="header-actions">
                        <el-button type="primary" :icon="Plus" @click="handleAdd">新增甜点</el-button>
                        <el-button type="danger" :icon="Delete" @click="handleDeleteBatch" :disabled="ids.length === 0">批量删除</el-button>
                    </div>
                </div>
            </template>
            
            <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" border stripe>
                <el-table-column type="selection" width="50" align="center" />
                <el-table-column align="center" label="图片" width="100">
                    <template #default="scope">
                        <el-image 
                            style="width: 60px; height: 60px; border-radius: 8px;" 
                            :src="baseURL + scope.row.photoUrl"
                            :preview-src-list="[baseURL + scope.row.photoUrl]"
                            preview-teleported
                            fit="cover"
                        >
                            <template #error>
                                <div class="image-slot">
                                    <el-icon><Picture /></el-icon>
                                </div>
                            </template>
                        </el-image>
                    </template>
                </el-table-column>
                <el-table-column label="甜点名称" align="left" prop="name" min-width="120" show-overflow-tooltip>
                    <template #default="scope">
                        <span style="font-weight: 500;">{{ scope.row.name }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="所属分类" align="center" prop="categoryName" width="100">
                    <template #default="scope">
                        <el-tag type="success" effect="plain" v-if="scope.row.categoryName">{{ scope.row.categoryName }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="单价" align="center" prop="price" width="100">
                    <template #default="scope">
                        <span style="color: var(--color-brand-accent); font-weight: bold;">¥ {{ scope.row.price }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="描述" align="left" prop="descp" min-width="150" show-overflow-tooltip />
                <el-table-column label="发布日期" align="center" prop="releaseDate" width="120" :formatter="dateFormat" />
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

        <!-- Use append-to-body to avoid z-index issues -->
        <el-dialog v-model="visibleDialog" :title="titleOp" width="500px" style="border-radius: 12px;" append-to-body>
            <el-form :rules="saveRules" ref="saveForm" :model="dessert" label-width="80px" class="dialog-form">
                <el-form-item label="甜点图片" prop="photoUrl">
                    <el-upload class="avatar-uploader" :action="baseURL + '/dessert/upload'"
                        :show-file-list="false" :before-upload="beforeAvatarUpload" :on-success="handleAvatarSuccess">
                        <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="分类" prop="catId">
                    <el-select v-model="dessert.catId" placeholder="选择分类" style="width: 100%;">
                        <el-option v-for="item in listCategory" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="名称" prop="name">
                    <el-input v-model="dessert.name" placeholder="请输入甜点名称" />
                </el-form-item>
                <el-form-item label="价格" prop="price">
                    <el-input v-model="dessert.price" placeholder="请输入单价" type="number">
                        <template #prefix>¥</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="发布日期" prop="releaseDate">
                    <el-date-picker v-model="dessert.releaseDate" type="date" placeholder="选择发布日"
                        value-format="YYYY-MM-DD" style="width: 100%;" />
                </el-form-item>
                <el-form-item label="描述" prop="descp">
                    <el-input type="textarea" :rows="3" v-model="dessert.descp" placeholder="请输入甜点描述" />
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

.range-separator {
    margin: 0 8px;
    color: var(--color-text-secondary);
}

.table-card {
    min-height: 500px;
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

.avatar-uploader {
    border: 1px dashed var(--color-border);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 120px;
    height: 120px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: var(--transition-fast);
}

.avatar-uploader:hover {
    border-color: var(--color-brand-primary);
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
}

.avatar {
    width: 120px;
    height: 120px;
    object-fit: cover;
    display: block;
}

.image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
    font-size: 20px;
}
</style>