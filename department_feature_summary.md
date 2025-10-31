# 部门管理功能实现与代码说明

本文详细记录在“甜点信息管理系统”基础上模拟构建的“员工管理系统”模块——部门管理的完整实现，包含后端接口、前端页面及数据库脚本。数据库已配置为 `root/1234`，可直接启动服务进行联调。

---

## 1. 后端实现

### 1.1 通用控制器父类 `BaseController`
部门模块继承既有的 `BaseController`，其职责是统一分页处理与响应包装：

```java
// src/main/java/com/example/prjbackend/common/core/controller/BaseController.java
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void startPage() {
        PageDomain pageDomain = PageParamUtil.createPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = pageDomain.getOrderBy();
        PageHelper.startPage(pageNum, pageSize, orderBy);
    }

    protected TableDataInfo getDataByPage(List<?> list) {
        TableDataInfo tableData = new TableDataInfo();
        tableData.setCode(200);
        tableData.setMsg("查询成功");
        tableData.setRows(list);
        tableData.setTotal(new PageInfo(list).getTotal());
        return tableData;
    }

    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
```

### 1.2 领域对象 `Department`

```java
// src/main/java/com/example/prjbackend/domain/Department.java
@Data
public class Department {
    Long id;
    String name;
    String leader;
    String phone;
    String descp;
}
```

### 1.3 数据访问层 `DepartmentMapper`

```java
// src/main/java/com/example/prjbackend/mapper/DepartmentMapper.java
@Mapper
public interface DepartmentMapper {
    @Insert("insert into department(name, leader, phone, descp) values(#{name}, #{leader}, #{phone}, #{descp})")
    int insert(Department department);

    @Select("<script>select id, name, leader, phone, descp from department"
            + "<where>"
            + " <if test='name!=null and name!=""'>and name like concat('%',#{name},'%')</if>"
            + " <if test='leader!=null and leader!=""'>and leader like concat('%',#{leader},'%')</if>"
            + " <if test='phone!=null and phone!=""'>and phone like concat('%',#{phone},'%')</if>"
            + "</where> order by id desc" + "</script>")
    List<Department> list(Department department);

    @Update("<script>update department <set>"
            + "  <if test='name!=null'>name=#{name},</if>"
            + "  <if test='leader!=null'>leader=#{leader},</if>"
            + "  <if test='phone!=null'>phone=#{phone},</if>"
            + "  <if test='descp!=null'>descp=#{descp},</if>"
            + "</set> where id=#{id}</script>")
    int edit(Department department);

    @Delete("<script>delete from department where id in"
            + " <foreach collection='array' item='id' open='(' close=')' separator=','>#{id}</foreach>"
            + "</script>")
    int delete(Long[] ids);
}
```

### 1.4 业务层 `DepartmentService`

```java
// src/main/java/com/example/prjbackend/service/DepartmentService.java
@Service
public class DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;

    public int add(Department department) {
        return departmentMapper.insert(department);
    }

    public List<Department> list(Department department) {
        return departmentMapper.list(department);
    }

    public int edit(Department department) {
        return departmentMapper.edit(department);
    }

    public int delete(Long[] ids) {
        return departmentMapper.delete(ids);
    }
}
```

### 1.5 控制层 `DepartmentController`

```java
// src/main/java/com/example/prjbackend/controller/DepartmentController.java
@CrossOrigin
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController {
    @Autowired
    DepartmentService departmentService;

    @PostMapping
    public AjaxResult add(@RequestBody Department department) {
        return toAjax(departmentService.add(department));
    }

    @GetMapping
    public TableDataInfo list(Department department) {
        startPage();
        List<Department> list = departmentService.list(department);
        return getDataByPage(list);
    }

    @PutMapping
    public AjaxResult edit(@RequestBody Department department) {
        return toAjax(departmentService.edit(department));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(departmentService.delete(ids));
    }
}
```

### 1.6 数据库脚本

```sql
-- department_setup.sql
USE desserts;
DROP TABLE IF EXISTS department;
CREATE TABLE department (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  leader VARCHAR(100),
  phone VARCHAR(50),
  descp VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO department(name, leader, phone, descp)
  VALUES ('人力资源部', '王强', '010-88990001', '负责公司人力资源规划与员工发展'),
         ('研发部', '李敏', '010-88990002', '负责产品研发与技术支持'),
         ('财务部', '赵丽', '010-88990003', '负责公司财务预算和成本控制');
```

---

## 2. 前端实现

### 2.1 API 封装

```javascript
// src/api/Department.js
import axios from "../utils/request"

export function add(department) {
    return axios.post("/department", department.value)
}

export function list(queryParams) {
    return axios.get("/department", { params: queryParams.value })
}

export function edit(department) {
    return axios.put("/department", department.value)
}

export function del(ids2del) {
    return axios.delete("/department/" + ids2del)
}
```

### 2.2 路由与导航

```javascript
// src/router/index.js
import Department from "../views/Department.vue"

const routes = [
    { path: '/', name:'Home', component: () => import('../views/Home.vue') }, 
    { path: '/Register', name:'Register', component:Register },  
    { path: '/Category', name:'Category', component:Category }, 
    { path: '/Dessert', name:'Dessert', component:Dessert },  
    { path: '/Department', name:'Department', component:Department },  
]
```

```vue
<!-- src/App.vue 侧边导航新增入口 -->
<el-aside width="200px">
  <router-link to="/Register" class="nav-link module">用户注册</router-link> 
  <router-link to="/Category" class="nav-link module">分类管理</router-link> 
  <router-link to="/Dessert" class="nav-link module">甜点管理</router-link>  
  <router-link to="/Department" class="nav-link module">部门管理</router-link>  
</el-aside>
```

### 2.3 视图组件 `Department.vue`

```vue
<!-- src/views/Department.vue -->
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
        :pager-count="0"
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
  phone: [{ pattern: /^(|[0-9\-+()]{5,20})$/, message: '请输入合法联系电话', trigger: ['blur', 'submit'] }]
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
const queryParams = ref({ pageNum: 1, pageSize: 5, name: null, leader: null, phone: null })

function getList() {
  loading.value = true
  DepartmentService.list(queryParams).then(resp => {
    list.value = resp.data.rows
    total.value = resp.data.total
    loading.value = false
  })
}

getList()

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
```

---

## 3. 启动与验证

1. **后端**：在 `prj-backend` 目录执行 `mvn spring-boot:run`（确保 MySQL 已启动，账号 `root/1234`，数据库 `desserts` 已执行 `department_setup.sql` 脚本完成初始化）。
2. **前端**：在 `prj-fronted` 目录执行 `npm install`（首次）后运行 `npm run dev`，通过浏览器访问 Vite 提供的地址。
3. **联调验证**：
   - 进入“部门管理”页面，可按部门名称、负责人检索并分页浏览。
   - 点击“新增”、“修改”按钮触发表单校验和后端接口交互。
   - 选中多条记录后执行“删除”验证批量操作。

> 若需要自动化测试，可在 `prj-backend` 中运行 `mvn test` 确认后端构建状态，再结合前端端到端测试进一步保障质量。
