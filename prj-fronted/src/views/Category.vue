<template>
    <div>
      <h2>甜点分类</h2>   
      <el-form :model="queryParams" :inline="true" label-width="65px">
            <el-form-item label="名称">
                <el-input v-model="queryParams.name" placeholder="请输入分类名称"
                @keyup.enter.native="handleQuery" />
            </el-form-item> 
            <el-form-item label="描述">
                <el-input v-model="queryParams.descp" placeholder="请输入分类描述"  
                @keyup.enter.native="handleQuery"/>
            </el-form-item> 
            <el-form-item>
                <el-button type="primary"  @click="handleQuery">查询</el-button>
                <el-button  @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
      <el-row> 
        <el-button type="danger" @click="handleDeleteBatch">删除</el-button>
        <el-button type="primary" @click="visibleDialog=true; titleOp='新增';">新增</el-button>
      </el-row>
      <!-- 新增功能对话框 -->
      <el-dialog :title="titleOp" v-model="visibleDialog">
        <el-form :model="category" :rules="rules" ref="saveForm">
            <el-form-item>
              <el-input v-model="category.id" placeholder="ID" v-if="false" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item prop="name">
              <el-input v-model="category.name" placeholder="分类名称"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model="category.descp" placeholder="分类描述"></el-input>
            </el-form-item>
            <el-button type="primary" @click="save">确认</el-button>
        </el-form>
      </el-dialog> 

      <el-table style="min-height:300px" v-loading="loading" :data="list" 
            @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="60" align="center" />
        <el-table-column label="ID" align="center" prop="id" v-if="false" />
        <el-table-column label="分类名称" align="left" prop="name" width="100"  />
        <el-table-column label="分类描述" align="center" prop="descp"   /> 
        <el-table-column label="操作" align="center">
            <template #default="scope">  
                <!-- <el-button type="primary" @click="handleUpdate(scope.row)">修改</el-button> -->
                <el-button type="primary" 
                  @click="visibleDialog=true;titleOp='编辑';category=scope.row">修改</el-button>
                <el-button type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
        </el-table-column>
        </el-table> 

        <el-row type="flex" justify="center" align="middle"> 
        <el-pagination  
            :current-page="queryParams.pageNum"
            :page-size="queryParams.pageSize"
            :total="total" 
            :pager-count="0"  
            style="text-align: center;margin-top: 20px;"
            layout="jumper, prev, pager, next, total"
            @current-change="handleCurrentChange" />
        </el-row>

    </div>
  </template>
  
<script setup>
import axios from 'axios'
import { ElMessage,ElMessageBox } from 'element-plus';
import { reactive, ref } from "vue"; 
import * as CategoryService from '../api/Category'

let titleOp = ref('新增')
const visibleDialog=ref(false) 
const category=ref({}) //const category=reactive({})

const rules={
    name:[{required:true,message:'请输入分类名称',trigger:['blur','submit']},]
}
const saveForm=ref({})
 
function save(){     
  //console.log(category) //在控制台观察输入表单数据 
  saveForm.value.validate(valid=>{
    if(!valid){
        ElMessage.warning('新增分类数据有问题，请先修正！') 
    }else{
        if(category.value.id==undefined){
            //axios.post("http://localhost:8080/category",category.value).then(resp=>{ 
            CategoryService.add(category).then(resp=>{ 
                //console.log(resp) //在控制台观察响应数据resp
                if(resp.data.code==200){   
                    ElMessage.success('新增分类，成功！');  //element全局弹出框 
                    category.value={}; visibleDialog.value=false;
                    getList()
                }else{
                    ElMessage( { message: '新增分类，失败！', type: 'error', duration:12000});
                }                
            }).catch(error=>{ 
                if(error.response){
                    ElMessage( { message: '新增分类，异常！', type: 'error', duration:120000}); 
                } 
            })
        }else{
            CategoryService.edit(category).then(resp=>{ 
                //console.log(resp) //在控制台观察响应数据resp
                if(resp.data.code==200){   
                    ElMessage.success('编辑分类，成功！');  //element全局弹出框 
                    category.value={}; visibleDialog.value=false;
                }else{
                    ElMessage( { message: '编辑分类，失败！', type: 'error', duration:12000});
                }                
            }).catch(error=>{ 
                if(error.response){
                    ElMessage( { message: '编辑分类，异常！', type: 'error', duration:120000}); 
                } 
            })
        }
    }
  })
}  

/** 列表操作变量 */
let list=ref([]) //列表对象
let loading=ref(true) //列表上的遮罩层开关 <el-table v-loading="loading" ... 
let total=ref(0)  // 总行数
let queryParams=ref({ //查询参数
    pageNum: 1,
    pageSize: 5,
    name: null,
    descp: null, 
}) 
/** 列表 */
function getList() {
    loading.value = true; //打开遮罩层
    CategoryService.list(queryParams).then(resp => { //list()来自api/category.js
      // console.log(resp);
      list.value = resp.data.rows
      total.value = resp.data.total
      loading.value = false; //关闭遮罩层 
    });
}
const created=()=>{
  getList()
}
created() //进入组件，立即执行
/** 分页控件上单击页码 */
const handleCurrentChange=(val)=>{
  queryParams.value.pageNum=val
  getList()
}

// 查询
const handleQuery=()=>{
    queryParams.value.pageNum=1
    getList()
}
const resetQuery=()=>{
    queryParams.value.name=null
    queryParams.value.descp=null
    handleQuery()
}  

let ids=ref({}) 
const handleSelectionChange=(selection)=>{
    ids=selection.map(item=>item.id)  
}  
// 删除
function handleDelete(row){
    let ids2del=row.id||ids
    ElMessageBox.confirm('确认删除ID为'+ids2del+'的分类？','警告',
        {confirmButtonText:'删除',cancelButtonText:'取消',type:'warning'})
        .then(()=>{
            CategoryService.del(ids2del)
            .then(()=>{
                getList()
                ElMessage.success("删除成功")
            })
            .catch(()=>{
                ElMessage.warning("删除失败")
            })
        }) 
}
function handleDeleteBatch(){
    handleDelete(ids)
}
 
 
</script>
<style scoped>
</style>