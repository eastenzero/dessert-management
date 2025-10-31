<template>
    <div>
      <h2>甜点信息</h2> 
      <!-- 查询表单：分类catId，名称name，描述descp，价格区间price1~price2 -->
      <el-form :model="queryParams" :inline="true" label-width="45px"> 
        <el-form-item label="分类" >
          <el-select v-model="queryParams.catId" placeholder="请选择分类" style="width: 120px">
              <el-option v-for="item in listCategory"
                :key="item.id" :label="item.name" :value="item.id" >
              </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="queryParams.name" placeholder="请输入名称" style="width: 120px"
            @keyup.enter.native="handleQuery" />
        </el-form-item> 
        <el-form-item label="描述">
          <el-input v-model="queryParams.descp" placeholder="请输入描述" style="width: 120px" 
            @keyup.enter.native="handleQuery"/>
        </el-form-item> 
        <el-form-item label="价格">
          <el-input v-model="queryParams.price1" placeholder="最低价" style="width: 70px" 
            @keyup.enter.native="handleQuery"/> ~
          <el-input v-model="queryParams.price2" placeholder="最高价" style="width: 70px" 
            @keyup.enter.native="handleQuery"/>
        </el-form-item> 
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row>
        <el-button type="danger" @click="handleDeleteBatch">删除</el-button>
        <el-button type="primary" @click="visibleDialog=true; titleOp='新增';">新增</el-button>
      </el-row>
      <el-dialog v-model="visibleDialog" :title="titleOp" width="400">
        <el-form :rules="saveRules" ref='saveForm' :model="dessert" >
            <el-row>
            <el-col :span="12">
                <el-form-item>
                    <el-input v-model="dessert.id" placeholder="ID" :disabled="true" />
                </el-form-item>
                <el-form-item prop="catId">
                    <el-select v-model="dessert.catId" placeholder="选择分类">
                    <el-option v-for="item in listCategory"
                        :key="item.id" :label="item.name" :value="item.id" /> 
                    </el-select>
                </el-form-item>
                <el-form-item prop="name" >
                    <el-input v-model="dessert.name" placeholder="甜点名称"/>
                </el-form-item>
                <el-form-item prop="price" >
                    <el-input v-model="dessert.price" placeholder="单价"/>
                </el-form-item> 
                <el-form-item prop="releaseDate" >   
                    <el-date-picker v-model="dessert.releaseDate" type="date" 
                    style="width:400px"
                    placeholder="选择发布日" value-format="YYYY-MM-DD" />                                    
                </el-form-item>                
            </el-col>
            <el-col :span="11" style="margin-left: 3px;">
                <el-form-item prop="photoUrl">                
                    <el-upload class="avatar-uploader" style="margin: 0 auto; "                
                        :action="baseURL + '/dessert/upload'"
                        :show-file-list="false"
                        :before-upload="beforeAvatarUpload"
                        :on-success="handleAvatarSuccess">                
                        <img v-if="imageUrl" :src="imageUrl" class="avatar" />   
                        <i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 13px;">
                        单击上传小于2M</i> 
                    </el-upload>                
                </el-form-item>
                <el-form-item prop="descp">
                    <el-input type="textarea" :rows="3" style="height: 90px;"
                        v-model="dessert.descp" placeholder="甜点描述" />
                </el-form-item>
                <el-button type="primary" @click="save">确认</el-button>
                </el-col>
            </el-row>        
        </el-form>
      </el-dialog>

      <!-- 列表 -->
      <el-table style="min-height:400px" v-loading="loading" :data="list"
          @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="60" align="center" />
        <el-table-column label="分类ID" align="left" prop="catId" width="80" v-if="false" />
        <el-table-column label="ID" align="left" prop="id" v-if="false" />
        <el-table-column align="left" label="图片" width="60">
            <template #default="scope">
                <img style="width:60px;height:50px" :src=" baseURL + scope.row.photoUrl">
            </template>
        </el-table-column>
        <el-table-column label="甜点名称" align="left" prop="name" width="100"  />
        <el-table-column label="所属分类" align="left" prop="categoryName" width="80"  />
        <el-table-column label="单价" align="left" prop="price" width="55"  />
        <el-table-column label="甜点描述" align="center" prop="descp" /> 
        <el-table-column label="发布日" prop="releaseDate" width="100"  :formatter="dateFormat" />   
        <!-- <el-table-column label="发布日" width="100" align="center" prop="releaseDate" :formatter="dateFormat" /> -->
        <el-table-column label="操作" width="200" align="center">
            <template #default="scope">  
                <el-button type="primary" 
                @click="visibleDialog=true; titleOp='编辑';
                dessert=scope.row; 
                imageUrl = baseURL + dessert.photoUrl; ">修改</el-button>
                <el-button type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
        </el-table-column>
      </el-table> 
      <!-- 分页 -->
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
  import * as DessertService from "../api/Dessert"; 
  import * as CategoryService from "../api/Category"; 
  import { reactive, ref } from "vue"; 
  import { ElMessage,ElMessageBox } from 'element-plus';
  //列表操作变量
  let baseURL=process.env.BASE_API  //在图片显示时URL=后端baseURL + row.photoURL
  let list=ref([]) //列表对象
  let loading=ref(true) //列表上的遮罩层开关 <el-table v-loading="loading" ... 
  let total=ref(0)  // 总行数
  let queryParams=ref({ //查询参数
      pageNum: 1,
      pageSize: 5,
      name: null,
      descp: null, 
      catId: 0,  //分类id
      price1:null,
      price2:null,
  })
  //初始化分类列表
  let listCategory=ref([])  //分类列表
  CategoryService.list(ref({pageNum: 1, pageSize: 10000,}) ).then(resp => { //list()来自api/category.js 
        listCategory.value = resp.data.rows 
        listCategory.value.unshift({id:0, name:'请选择',descp:'',}) 
  }); 
  /** 查询列表 */
  function getList() { 
      loading.value = true; //打开遮罩层
      DessertService.list(queryParams).then(resp => { //list()来自api/dessert.js
        list.value = resp.data.rows
        total.value = resp.data.total
        loading.value = false; //关闭遮罩层 
      });
  }
  getList()  //进入组件，立即执行查询列表
  /** 分页控件上单击页码 */
  const handleCurrentChange=(val)=>{
    queryParams.value.pageNum=val
    getList()
  }
  //查询表单 
  /** 搜索操作 */
  const handleQuery=()=>{
    queryParams.value.pageNum = 1;
    getList()
  }
  /** 重置操作 */
  const resetQuery=()=>{
    queryParams.value.name=null
    queryParams.value.descp=null
    queryParams.value.catId=0
    queryParams.value.price1=null
    queryParams.value.price2=null  
    handleQuery();
  }
  /** 格式化日期：YY-MM-DD */
  const dateFormat=(row,col)=>{
    let date=row[col.property]
    return date==undefined?'':date.substring(2);
  }

//   新增
    // import { ElMessage,ElMessageBox } from 'element-plus'; 
    let titleOp = ref('新增')
    const visibleDialog = ref(false) 
    const dessert = ref({})
    const saveForm = ref({})
    let imageUrl = ref('')

    const saveRules= {
        name:[{ required: true, message: '请输入甜点名称', trigger: ['submit'] }], 
        catId:[{ required: true, message: '请选择分类', trigger: ['submit'] }], 
        //对price变量设置多个校验规则
        price:[{ required: true, message: '请输入价格', trigger: ['submit'] } ,  
                {"pattern": /^[0-9]*$/,"message": "请输入整数", trigger: ['submit']}],
        releaseDate:[{ required: true, message: '请选择发布日', trigger: ['submit'] }],
    }
    
    const beforeAvatarUpload=(file)=>{ 
        const isLt2M = file.size / 1024 / 1024 < 2 
        if (!isLt2M) {
        ElMessage.error('上传图片大小不能超过 2M!');
        }
        return isLt2M; 
    } 
    const handleAvatarSuccess=(res, file)=>{ 
        imageUrl.value=baseURL+res 
        dessert.value.photoUrl=res
    }

    const save=()=>{     
        saveForm.value.validate((valid) => {
            if(!valid) {
            ElMessage.warning('新增数据有问题,请先修正') 
            return false
            }else{   
            if(dessert.value.id==undefined) { //新增
                DessertService.add(dessert).then(resp=>{  
                    if(resp.data.code==200){  
                      ElMessage.success('新增甜点，成功！');   
                      getList()
                      dessert.value={}; imageUrl.value=null; visibleDialog.value=false; 
                    }else{
                        ElMessage( { message: '新增甜点，失败！', type: 'error', duration:1200});
                        }                
                    }).catch(error=>{ 
                    if(error.response){ 
                        ElMessage( { message: '新增甜点，异常！', type: 'error', duration:1200}); } 
                    })
                }else{ 
                    // 编辑逻辑
                    DessertService.edit(dessert).then(resp=>{  
                        if(resp.data.code==200){   
                        ElMessage.success('编辑甜点，成功！');  
                        dessert.value={}; imageUrl.value=null; visibleDialog.value=false;
                        }else{
                        ElMessage( { message: '编辑甜点，失败！', type: 'error', duration:1200});
                        }                
                    }).catch(error=>{ 
                    if(error.response){ 
                        ElMessage( { message: '编辑甜点，异常！', type: 'error', duration:1200}); 
                    } 
                }) 
              } 
            }
        }) 
        } 

      // 删除
      function handleDelete(row){
          let ids2del=row.id||ids
          ElMessageBox.confirm('确认删除ID为'+ids2del+'的甜点？','警告',
              {confirmButtonText:'删除',cancelButtonText:'取消',type:'warning'})
              .then(()=>{
                  DessertService.del(ids2del)
                  .then(()=>{
                      getList()
                      ElMessage.success("删除成功")
                  })
                  .catch(()=>{
                      ElMessage.warning("删除失败")
                  })
              }) 
      }
      let ids=ref([])
      const handleSelectionChange=(selection)=>{
        ids=selection.map(row=>row.id)
      }
      function handleDeleteBatch(){
          handleDelete(ids)
      }

  </script>
  
<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9; border-radius: 4px;
    cursor: pointer; position: relative; overflow: hidden;  
  }
  .avatar-uploader .el-upload:hover { border-color: #409EFF; }
  .avatar-uploader-icon {
    font-size: 18px; color: #8c939d; width: 130px;
    height: 130px; line-height: 130px; text-align: center; 
  }
  .avatar { width: 130px; height: 130px; }
</style>