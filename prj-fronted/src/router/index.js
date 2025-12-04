import {createRouter,createWebHistory} from 'vue-router' 
import Register from "../views/Register.vue"
import Category from "../views/Category.vue"
import Dessert from "../views/Dessert.vue"
import Department from "../views/Department.vue"
import Employee from "../views/Employee.vue"

// 路由规则，定义URL地址与组件之间的对应关系
const routes = [
    { path: '/', name:'Home', component: () => import('../views/Home.vue') }, 
    { path: '/Register', name:'Register', component:Register },  
    { path: '/Category', name:'Category', component:Category }, 
    { path: '/Dessert', name:'Dessert', component:Dessert },  
    { path: '/Department', name:'Department', component:Department },  
    { path: '/Employee', name:'Employee', component:Employee },  
]
const router = createRouter({
    history: createWebHistory(),
    routes: routes
}) 
export default router
