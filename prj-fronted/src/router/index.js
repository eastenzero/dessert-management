import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

// Layouts
import AdminLayout from '../layout/AdminLayout.vue'

// Views
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Home from '../views/Home.vue'
import Category from '../views/Category.vue'
import Dessert from '../views/Dessert.vue'
import Department from '../views/Department.vue'
import Employee from '../views/Employee.vue'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { public: true }
    },
    {
        path: '/register',
        name: 'Register',
        component: Register,
        meta: { public: true }
    },
    {
        path: '/',
        component: AdminLayout,
        children: [
            { path: '', name: 'Home', component: Home, meta: { title: '仪表盘' } },
            { path: 'category', name: 'Category', component: Category, meta: { title: '分类管理' } },
            { path: 'dessert', name: 'Dessert', component: Dessert, meta: { title: '甜点管理' } },
            { path: 'department', name: 'Department', component: Department, meta: { title: '部门管理' } },
            { path: 'employee', name: 'Employee', component: Employee, meta: { title: '员工管理' } }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// Navigation Guard
router.beforeEach((to, from, next) => {
    const isPublic = to.meta.public
    const isLoggedIn = !!localStorage.getItem('token')

    if (to.meta.title) {
        document.title = `${to.meta.title} - 甜点信息管理系统`
    } else {
        document.title = '甜点信息管理系统'
    }

    if (!isPublic && !isLoggedIn) {
        ElMessage.warning('请先登录')
        next('/login')
    } else if (isPublic && isLoggedIn && to.path === '/login') {
        next('/') // Redirect to home if already logged in
    } else {
        next()
    }
})

export default router
